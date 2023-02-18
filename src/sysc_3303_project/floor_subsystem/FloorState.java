package sysc_3303_project.floor_subsystem;

import sysc_3303_project.EventBuffer;
import sysc_3303_project.RequestData;
import sysc_3303_project.State;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

public abstract class FloorState implements State {

	@Override
	public void doEntry() {

	}

	@Override
	public void doExit() {

	}
	
	public abstract FloorState handleButtonPressed(RequestData requestData, EventBuffer<SchedulerEventType> schedulerBuffer);
	
}
