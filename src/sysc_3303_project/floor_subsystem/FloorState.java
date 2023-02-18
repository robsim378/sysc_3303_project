package sysc_3303_project.floor_subsystem;

import logging.Logger;
import sysc_3303_project.EventBuffer;
import sysc_3303_project.RequestData;
import sysc_3303_project.SchedulerEventType;
import sysc_3303_project.State;

public abstract class FloorState implements State {

	@Override
	public void doEntry() {
		Logger.getLogger().logNotification(this.getClass().getName(), "No entry activity");
	}

	@Override
	public void doExit() {
		Logger.getLogger().logNotification(this.getClass().getName(), "No exit activity");
	}
	
	public abstract FloorState handleButtonPressed(RequestData requestData, EventBuffer<SchedulerEventType> schedulerBuffer);
	
}
