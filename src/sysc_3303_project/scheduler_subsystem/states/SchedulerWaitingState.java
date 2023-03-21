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

/**
 * @author Andrei Popescu
 * The SchedulerWaitingState represents a Scheduler state where the Scheduler
 * has no active request and is waiting for one to process.
 */
public class SchedulerWaitingState extends SchedulerState {
	
	/**
	 * Creates a new SchedulerWaitingState.
	 * @param context the Scheduler this state is tied to
	 */
	public SchedulerWaitingState(Scheduler context) {
		super(context);
	}
	
	@Override
	public SchedulerState handleFloorButtonPressed(int floorNumber, Direction direction) {
		// assign request and get assigned elevator ID
		LoadRequest request = new LoadRequest(floorNumber, direction);
		int assignedElevator = context.assignLoadRequest(request);
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
		return new SchedulerProcessingState(context);
	}
	
	@Override
	public SchedulerState handleElevatorButtonPressed(int elevatorId, int floorNumber) {
		contextTracker.addUnloadRequest(elevatorId, floorNumber);
		return new SchedulerProcessingState(context);
	}
	
	public SchedulerState handleElevatorBlocked(int elevatorId) {
		List<LoadRequest> toAssign = contextTracker.shutdownElevator(elevatorId);
		for (LoadRequest request : toAssign) { //reassign the requests by sending the floor button presses to the scheduler again
			context.getInputBuffer().addEvent(new Event<>(
					Subsystem.FLOOR, request.floor,
					Subsystem.SCHEDULER, 0,
					SchedulerEventType.FLOOR_BUTTON_PRESSED, request.direction));
		}
		return null;
	}
}
