/**
 * 
 */
package sysc_3303_project.scheduler_subsystem;

import logging.Logger;
import sysc_3303_project.ElevatorSubsystem.*;
import sysc_3303_project.Event;
import sysc_3303_project.RequestData;

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
