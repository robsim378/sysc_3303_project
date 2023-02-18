/**
 * 
 */
package sysc_3303_project.scheduler_subsystem;

import logging.Logger;
import sysc_3303_project.Direction;
import sysc_3303_project.Elevator;
import sysc_3303_project.ElevatorSubsystem.*;
import sysc_3303_project.Event;
import sysc_3303_project.RequestData;
import sysc_3303_project.Scheduler;

/**
 * @author apope
 *
 */
public class ProcessingSchedulerState extends SchedulerState {

	public ProcessingSchedulerState(Scheduler context) {
		super(context);
	}
	
	@Override
	public SchedulerState handleElevatorDoorsClosed(Elevator e) {
		Direction moveDirection = (e.getFloor() < context.getTargetFloor()) ? Direction.UP : Direction.DOWN;
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator to start moving");
		context.getElevatorBuffer().addEvent(
				new Event<>(ElevatorEventType.START_MOVING_IN_DIRECTION, context, moveDirection));
		return null;
	}
	
	@Override
	public SchedulerState handleElevatorDoorsOpened(Elevator e) {
		int currentFloor = e.getFloor();
		for (RequestData request : context.getPendingRequests()) {
			if (request.getCurrentFloor() == currentFloor) {
				context.markRequestInProgress(request);
			}
		}
		for (RequestData request : context.getInProgressRequests()) {
			if (request.getDestinationFloor() == currentFloor) {
				context.completeRequest(request);
			}
		}
		if (context.hasRequests()) {
			return null;
		} else {
			return new WaitingSchedulerState(context);
		}
		
	}
	
	@Override
	public SchedulerState handleElevatorStopped(Elevator e, int floor) {
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator to open doors");
		context.getElevatorBuffer().addEvent(new Event<>(ElevatorEventType.OPEN_DOORS, context, null));
		return null;
	}
	
	@Override
	public SchedulerState handleElevatorApproachingFloor(Elevator e, int floor) {
		boolean stopping = false;
		for (RequestData requestData : context.getInProgressRequests()) {
			if (requestData.getDestinationFloor() == floor) {
				stopping = true;
				break;
			}
		}
		for (RequestData requestData : context.getPendingRequests()) {
			if (requestData.getCurrentFloor() == floor) {
				stopping = true;
				break;
			}
		}
		if (stopping) {
			Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator to stop at next floor " + floor);
			context.getElevatorBuffer().addEvent(new Event<>(ElevatorEventType.STOP_AT_NEXT_FLOOR, context, null));
		} else {
			Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator to NOT stop at next floor" + floor);
			context.getElevatorBuffer().addEvent(new Event<>(ElevatorEventType.CONTINUE_MOVING, context, null));
		}
		return null;
	}
	
	@Override
	public SchedulerState handleFloorButtonPressed(RequestData request) {
		context.addPendingRequest(request);
		return null;
	}

}
