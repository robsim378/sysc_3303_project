/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.floor_subsystem.states;

import logging.Logger;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.floor_subsystem.FloorSystem;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * @author Liam Gaudet
 * 
 * Idle state for the floor subsystem. Handle receiving button requests and then sends them to the scheduler.
 *
 */
public class FloorIdleState extends FloorState {

	private FloorSystem context;
	
	/**
	 * Constructor for the idle state. Initializes the context.
	 * @param context	FloorSystem, the context to use for this system
	 */
	public FloorIdleState(FloorSystem context) {
		this.context = context;
	}
	
	
	@Override
	public FloorState handleButtonPressed(RequestData requestData, EventBuffer<SchedulerEventType> schedulerBuffer) {
		Logger.getLogger().logNotification(this.getClass().getName(), "Sent request to scheduler: " + requestData.toString());

		Event<SchedulerEventType> event = new Event<SchedulerEventType>(SchedulerEventType.FLOOR_BUTTON_PRESSED, context, requestData);
		
		schedulerBuffer.addEvent(event);
		
		return new FloorIdleState(this.context);
	}

}
