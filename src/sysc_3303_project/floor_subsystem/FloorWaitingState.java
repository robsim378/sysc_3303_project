package sysc_3303_project.floor_subsystem;

import logging.Logger;
import sysc_3303_project.Event;
import sysc_3303_project.EventBuffer;
import sysc_3303_project.RequestData;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

public class FloorWaitingState extends FloorState {

	private FloorSystem context;
	
	public FloorWaitingState(FloorSystem context) {
		this.context = context;
	}
	@Override
	public FloorState handleButtonPressed(RequestData requestData, EventBuffer<SchedulerEventType> schedulerBuffer) {
		Logger.getLogger().logNotification(this.getClass().getName(), "Sent request to scheduler: " + requestData.toString());

		Event<SchedulerEventType> event = new Event<SchedulerEventType>(SchedulerEventType.FLOOR_BUTTON_PRESSED, context, requestData);
		
		schedulerBuffer.addEvent(event);
		
		return new FloorWaitingState(this.context);
	}

}
