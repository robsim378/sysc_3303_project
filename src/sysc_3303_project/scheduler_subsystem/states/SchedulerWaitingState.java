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
		int assignedElevator = context.assignLoadRequest(floorNumber, direction);
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator " + assignedElevator + " to close doors");
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
}
