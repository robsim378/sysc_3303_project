/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.scheduler_subsystem.states;

import logging.Logger;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.common.Subsystem;
import sysc_3303_project.elevator_subsystem.*;
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
		Direction moveDirection = context.directionToMove(elevatorId);
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to start moving");
		context.getOutputBuffer().addEvent(new Event<Enum<?>>(
				Subsystem.ELEVATOR, elevatorId, 
				Subsystem.SCHEDULER, 0, 
				ElevatorEventType.START_MOVING_IN_DIRECTION, moveDirection));

		return null;
	}
	
	@Override
	public SchedulerState handleElevatorDoorsOpened(int elevatorId, int floorNumber) {
		contextTracker.unloadElevator(elevatorId, floorNumber);
		contextTracker.loadElevator(elevatorId, floorNumber);
		if (context.getTracker().hasRequests(elevatorId)) {
			context.getOutputBuffer().addEvent(new Event<Enum<?>>(
					Subsystem.ELEVATOR, elevatorId, 
					Subsystem.SCHEDULER, 0, 
					ElevatorEventType.CLOSE_DOORS, null));
			return null;
		} else {
			return new SchedulerWaitingState(context);
		}
		
	}
	
	@Override
	public SchedulerState handleElevatorStopped(int elevatorId, int floorNumber) {
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to open doors");
		context.getOutputBuffer().addEvent(new Event<Enum<?>>(
				Subsystem.ELEVATOR, elevatorId, 
				Subsystem.SCHEDULER, 0, 
				ElevatorEventType.OPEN_DOORS, null));
		return null;
	}
	
	@Override
	public SchedulerState handleElevatorApproachingFloor(int elevatorId, int floorNumber) {
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
		return null;
	}

	@Override
	public SchedulerState handleElevatorButtonPressed(int elevatorId, int floorNumber) {
		contextTracker.addUnloadRequest(elevatorId, floorNumber);
		return null;
	}
}
