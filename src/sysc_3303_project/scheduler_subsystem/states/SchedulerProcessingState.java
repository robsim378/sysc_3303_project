/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.scheduler_subsystem.states;

import java.util.List;

import logging.Logger;
import sysc_3303_project.scheduler_subsystem.LoadRequest;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.elevator_subsystem.*;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.gui_subsystem.GuiEventType;

/**
 * @author Andrei Popescu
 * The SchedulerProcessingState represents a Scheduler state where the Scheduler
 * has an active request that it is processing.
 */
public class SchedulerProcessingState extends SchedulerState {

	/**
	 * Creates a new SchedulerProcessingState.
	 * @param context the Scheduler this state is tied to
	 */
	public SchedulerProcessingState(Scheduler context) {
		super(context);
	}
	
	@Override
	public SchedulerState handleElevatorDoorsClosed(int elevatorId, int floorNumber) {
		context.getFaultDetector().clearTimers(elevatorId);
		contextTracker.updateElevatorFloor(elevatorId, floorNumber);
		boolean hasRequestsAtCurrentFloor = contextTracker.countUnloadRequests(elevatorId, floorNumber) > 0
				|| contextTracker.hasLoadRequestInDirection(elevatorId, floorNumber, contextTracker.getElevatorDirection(elevatorId));
		if (hasRequestsAtCurrentFloor) {
			Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to open doors");
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.OPEN_DOORS, null));
			context.getFaultDetector().addTimer(elevatorId, 1000); //doors open timer
			return null;
		}
		Direction moveDirection = context.directionToMove(elevatorId);
		contextTracker.updateElevatorDirection(elevatorId, moveDirection);
		if (moveDirection != null) { // if there are no requests: shouldn't happen but don't break the system if it does
			Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Ordering elevator " + elevatorId + " to start moving");
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.START_MOVING_IN_DIRECTION, moveDirection));
			context.getFaultDetector().addTimer(elevatorId, 1000); //expect reach next floor
		} else { //failsafe, idle the elevator (if there are no requests)
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.SET_IDLE, null)); //set direction to null but do nothing
			Logger.getLogger().logError(context.getClass().getName(), "Ordering elevator " + elevatorId + " to open doors");
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.OPEN_DOORS, null));
			context.getFaultDetector().addTimer(elevatorId, 1000); //doors open timer
		}
		return null;
	}
	
	@Override
	public SchedulerState handleElevatorDoorsOpened(int elevatorId, int floorNumber) {
		context.getFaultDetector().clearTimers(elevatorId);
		contextTracker.updateElevatorFloor(elevatorId, floorNumber);
		int unloadCount = contextTracker.unloadElevator(elevatorId, floorNumber);
		Direction loadDirection = contextTracker.loadElevator(elevatorId, floorNumber);
		boolean loaded = loadDirection != null;
		for (int i = 0; i < unloadCount; i++) {
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.PASSENGERS_UNLOADED, floorNumber));
			Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Unloading passenger at floor " + floorNumber + " from elevator " + elevatorId);
		}
		if (loaded) {
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.FLOOR, floorNumber, 
					Subsystem.SCHEDULER, elevatorId, //use the elevator ID since it is more meaningful
					FloorEventType.PASSENGERS_LOADED, loadDirection));
			Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Loading passengers at floor " + floorNumber + " into elevator " + elevatorId);
		}
		if (loaded || contextTracker.hasRequests(elevatorId)) { //if we expect more requests close doors (the requests may not have come in yet)
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.CLOSE_DOORS, null));
			Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Ordering elevator " + elevatorId + " to close doors");
			context.getFaultDetector().addTimer(elevatorId, 1000); //doors close timer
			return null;
		} else {
			Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Elevator " + elevatorId + " is idle, keep doors open");
			contextTracker.updateElevatorDirection(elevatorId, null); //elevator now idle
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.SET_IDLE, null)); //set direction to null but do nothing
			for (int i : contextTracker.getElevatorIds()) {
				if (contextTracker.getElevatorRequestCount(i) > 0) return null;
			}
			return new SchedulerWaitingState(context);
		}
	}
	
	@Override
	public SchedulerState handleElevatorStopped(int elevatorId, int floorNumber) {
		context.getFaultDetector().clearTimers(elevatorId);
		contextTracker.updateElevatorFloor(elevatorId, floorNumber);
		Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Ordering elevator " + elevatorId + " to open doors");
		context.getOutputBuffer().addEvent(new Event<Enum<?>>(
				Subsystem.ELEVATOR, elevatorId, 
				Subsystem.SCHEDULER, 0, 
				ElevatorEventType.OPEN_DOORS, null));
		context.getFaultDetector().addTimer(elevatorId, 1000); //open doors timer
		return null;
	}
	
	@Override
	public SchedulerState handleElevatorApproachingFloor(int elevatorId, int floorNumber) {
		context.getFaultDetector().clearTimers(elevatorId);
		boolean stopping = context.shouldStop(elevatorId, floorNumber);
		if (stopping) {
			Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Ordering elevator " + elevatorId + " to stop at next floor " + floorNumber);
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.STOP_AT_NEXT_FLOOR, null));
			context.getFaultDetector().addTimer(elevatorId, 1000); //this timer should maybe be lower
		} else {
			Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Ordering elevator " + elevatorId + " to NOT stop at next floor " + floorNumber);
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.CONTINUE_MOVING, null));
			context.getFaultDetector().addTimer(elevatorId, 1000);
		}
		return null;
	}
	
	@Override
	public SchedulerState handleFloorButtonPressed(int floorNumber, Direction direction) {
		LoadRequest request = new LoadRequest(floorNumber, direction);
		int assignedElevator = context.assignLoadRequest(request);
		if (contextTracker.getElevatorDirection(assignedElevator) == null) {
			if (contextTracker.getElevatorFloor(assignedElevator) == floorNumber) {
				contextTracker.loadElevator(assignedElevator, floorNumber);
				context.getOutputBuffer().addEvent(new Event<Enum<?>>(
						Subsystem.FLOOR, floorNumber, 
						Subsystem.SCHEDULER, assignedElevator, //use the elevator ID since it is more meaningful
						FloorEventType.PASSENGERS_LOADED, direction));
				Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Loading passengers at floor " + floorNumber + " into elevator " + assignedElevator);
			}
			Logger.getLogger().logNotification(context.getClass().getSimpleName(), "Ordering elevator " + assignedElevator + " to close doors");
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, assignedElevator, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.CLOSE_DOORS, null));

		}
		context.getFaultDetector().addTimer(assignedElevator, 1000); //doors close timer
		return null;
	}

	@Override
	public SchedulerState handleElevatorButtonPressed(int elevatorId, int floorNumber) {
		contextTracker.addUnloadRequest(elevatorId, floorNumber);
		return null;
	}
	
	@Override
	public SchedulerState handleElevatorBlocked(int elevatorId) {
		context.getFaultDetector().clearTimers(elevatorId);
		Logger.getLogger().logError(context.getClass().getSimpleName(), "Elevator " + elevatorId + " is blocked!!!");
		List<LoadRequest> toAssign = contextTracker.shutdownElevator(elevatorId);
		context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.GUI, 0,
                Subsystem.SCHEDULER, 0,
                GuiEventType.ELEVATOR_SHUTDOWN_FAULT, elevatorId));
		for (LoadRequest request : toAssign) { //reassign the requests by sending the floor button presses to the scheduler again
			context.getInputBuffer().addEvent(new Event<>(
					Subsystem.SCHEDULER, 0,
					Subsystem.FLOOR, request.floor,
					SchedulerEventType.FLOOR_BUTTON_PRESSED, request.direction));
		}
		return null;
	}
}
