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
		Direction moveDirection = (floorNumber < context.getTargetFloor()) ? Direction.UP : Direction.DOWN;
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + elevatorId + " to start moving");
		context.getOutputBuffer().addEvent(new Event<Enum<?>>(
				Subsystem.ELEVATOR, elevatorId, 
				Subsystem.SCHEDULER, 0, 
				ElevatorEventType.START_MOVING_IN_DIRECTION, moveDirection));

		return null;
	}
	
	@Override
	public SchedulerState handleElevatorDoorsOpened(int elevatorId, int floorNumber) {
		for (RequestData request : context.getPendingRequests()) {
			if (request.getCurrentFloor() == floorNumber) {
				context.markRequestInProgress(request);
			}
		}
		for (RequestData request : context.getInProgressRequests()) {
			if (request.getDestinationFloor() == floorNumber) {
				context.completeRequest(request);
			}
		}
		if (context.hasRequests()) {
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
		boolean stopping = false;
		for (RequestData requestData : context.getInProgressRequests()) {
			if (requestData.getDestinationFloor() == floorNumber) {
				stopping = true;
				break;
			}
		}
		for (RequestData requestData : context.getPendingRequests()) {
			if (requestData.getCurrentFloor() == floorNumber) {
				stopping = true;
				break;
			}
		}
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
		// assign new request
		return null;
	}

}
