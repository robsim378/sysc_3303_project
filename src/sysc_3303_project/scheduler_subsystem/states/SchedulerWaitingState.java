/**
 * 
 */
package sysc_3303_project.scheduler_subsystem.states;

import logging.Logger;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.RequestData;
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
	public SchedulerState handleFloorButtonPressed(RequestData request) {
		context.addPendingRequest(request);
		//here we would assign the request to an elevator but there's only one elevator
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator to close doors");
		context.getElevatorBuffer().addEvent(new Event<>(ElevatorEventType.CLOSE_DOORS, context, null));
		return new SchedulerProcessingState(context);
	}
}
