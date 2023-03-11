/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.scheduler_subsystem.states;

import logging.Logger;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.configuration.SystemProperties;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.elevator_subsystem.*;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.floor_subsystem.FloorSystem;

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
		contextTracker.updateElevatorFloor(elevatorId, floorNumber);
		Direction moveDirection = context.directionToMove(elevatorId);
		contextTracker.updateElevatorDirection(elevatorId, moveDirection);
		if (moveDirection != null) { // if there are no requests: shouldn't happen but don't break the system if it does
			Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to start moving");
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.START_MOVING_IN_DIRECTION, moveDirection));
		} else { //failsafe, idle the elevator (if there are no requests)
			Logger.getLogger().logError(context.getClass().getName(), "Unexpectedly no requests for " + elevatorId + ", open doors again");
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.OPEN_DOORS, null));
		}
		return null;
	}
	
	@Override
	public SchedulerState handleElevatorDoorsOpened(int elevatorId, int floorNumber) {
		contextTracker.updateElevatorFloor(elevatorId, floorNumber);
		int unloadCount = contextTracker.unloadElevator(elevatorId, floorNumber);
		boolean loaded = contextTracker.loadElevator(elevatorId, floorNumber);
		for (int i = 0; i < unloadCount; i++) {
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.PASSENGERS_UNLOADED, floorNumber));
			Logger.getLogger().logNotification(context.getClass().getName(), "Unloading passenger at floor " + floorNumber + " from elevator " + elevatorId);
		}
		if (loaded) {
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.FLOOR, floorNumber, 
					Subsystem.SCHEDULER, 0, 
					FloorEventType.PASSENGERS_LOADED, contextTracker.getElevatorDirection(elevatorId)));
			Logger.getLogger().logNotification(context.getClass().getName(), "Loading passengers at floor " + floorNumber + " into elevator " + elevatorId);
		}
		if (loaded || unloadCount > 0 || contextTracker.hasRequests(elevatorId)) { //if we expect more requests close doors (the requests may not have come in yet)
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.CLOSE_DOORS, null));
			Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to close doors");
			return null;
		} else {
			contextTracker.updateElevatorDirection(elevatorId, null); //elevator now idle
			for (int i = 0; i < SystemProperties.MAX_ELEVATOR_NUMBER; i++) {
				if (contextTracker.getElevatorRequestCount(i) > 0) return null;
			}
			return new SchedulerWaitingState(context);
		}
	}
	
	@Override
	public SchedulerState handleElevatorStopped(int elevatorId, int floorNumber) {
		contextTracker.updateElevatorFloor(elevatorId, floorNumber);
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to open doors");
		context.getOutputBuffer().addEvent(new Event<Enum<?>>(
				Subsystem.ELEVATOR, elevatorId, 
				Subsystem.SCHEDULER, 0, 
				ElevatorEventType.OPEN_DOORS, null));
		return null;
	}
	
	@Override
	public SchedulerState handleElevatorApproachingFloor(int elevatorId, int floorNumber) {
		contextTracker.updateElevatorFloor(elevatorId, floorNumber);
		//stop if there is a request (load in correct direction or unload) at the floor
		boolean stopping = contextTracker.hasLoadRequestInDirection(elevatorId, floorNumber, contextTracker.getElevatorDirection(elevatorId))
				|| (contextTracker.countUnloadRequests(elevatorId, floorNumber) > 0);
		//also stop if reaching the top or bottom floor - shouldn't happen but failsafe
		stopping = stopping ||
				(floorNumber == FloorSystem.MAX_FLOOR_NUMBER && contextTracker.getElevatorDirection(elevatorId)== Direction.DOWN) ||
				(floorNumber == 0 && contextTracker.getElevatorDirection(elevatorId)== Direction.UP);
		if (stopping) {
			Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to stop at next floor " + floorNumber);
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.STOP_AT_NEXT_FLOOR, null));
		} else {
			Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to NOT stop at next floor " + floorNumber);
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.CONTINUE_MOVING, null));
		}
		return null;
	}
	
	@Override
	public SchedulerState handleFloorButtonPressed(int floorNumber, Direction direction) {
		int assignedElevator = context.assignLoadRequest(floorNumber, direction);
		if (contextTracker.getElevatorDirection(assignedElevator) == null) {
			Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + assignedElevator + " to close doors");
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, assignedElevator, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.CLOSE_DOORS, null));
		}
		return null;
	}

	@Override
	public SchedulerState handleElevatorButtonPressed(int elevatorId, int floorNumber) {
		contextTracker.addUnloadRequest(elevatorId, floorNumber);
		return null;
	}
}
