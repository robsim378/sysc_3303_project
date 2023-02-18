package sysc_3303_project.floor_subsystem;

import sysc_3303_project.Event;
import sysc_3303_project.EventBuffer;
import sysc_3303_project.FloorSystem;
import sysc_3303_project.RequestData;
import sysc_3303_project.SchedulerEventType;

public class FloorWaitingState extends FloorState {

	private FloorSystem context;
	
	public FloorWaitingState(FloorSystem context) {
		this.context = context;
	}
	@Override
	public FloorState handleButtonPressed(RequestData requestData, EventBuffer<SchedulerEventType> schedulerBuffer) {
		
		Event<SchedulerEventType> event = new Event<SchedulerEventType>(SchedulerEventType.FLOOR_BUTTON_PRESSED, context, requestData);
		
		//schedulerBuffer.addEvent(event);
		
		return new FloorWaitingState(this.context);
	}

}
