/**
 * 
 */
package scheduler_subsystem;

import logging.Logger;
import sysc_3303_project.Elevator;
import sysc_3303_project.ElevatorEventType;
import sysc_3303_project.Event;
import sysc_3303_project.RequestData;
import sysc_3303_project.Scheduler;

/**
 * @author apope
 *
 */
public class WaitingSchedulerState extends SchedulerState {
	
	public WaitingSchedulerState(Scheduler context) {
		super(context);
	}
	
	@Override
	public SchedulerState handleFloorButtonPressed(RequestData request) {
		context.addPendingRequest(request);
		//here we would assign the request to an elevator but there's only one elevator
		Logger.getLogger().logNotification(context.getClass().getName(), "Ordering elevator to close doors");
		context.getElevatorBuffer().addEvent(new Event<>(ElevatorEventType.CLOSE_DOORS, context, null));
		return new ProcessingSchedulerState(context);
	}
}
