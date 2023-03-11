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

	/**
	 * Handles pressing a button a floor
	 * @param requestData		RequestData, the data for the button request
	 * @param outputBuffer		EventBuffer, the location to send the data to.
	 * @return					FloorState, the next state
	 */
	@Override
	public FloorState handleButtonPressed(RequestData requestData, EventBuffer<SchedulerEventType> outputBuffer) {
		Logger.getLogger().logNotification(this.getClass().getName(), "Sent request to scheduler: " + requestData.toString());

		// Send an event to the scheduler requesting an elevator.
		Event<SchedulerEventType> event = new Event<SchedulerEventType>(
				Subsystem.SCHEDULER,
				0,
				Subsystem.FLOOR,
				context.getFloorID(),
				SchedulerEventType.FLOOR_BUTTON_PRESSED,
				requestData.getDirection());
		outputBuffer.addEvent(event);

		// Adds the destination request to the list of requests waiting, which will be sent to an elevator when it arrives.
		context.elevatorRequests.add(requestData.getDestinationFloor());
		
		return new FloorIdleState(this.context);
	}

	/**
	 * Handles an elevator arriving at a floor
	 * @param direction 		Direction, the direction the elevator will be going.
	 * @param outputBuffer		EventBuffer, the location too send data to.
	 * @return					FloorState, the next state
	 */
	@override
	public FloorState handleElevatorArrived(Direction direction, int elevatorID, EventBuffer<ElevatorEventType> outputBuffer) {
		// Go through all the requests on the current floor, sending a button request to all the floors in the direction
		// the elevator is heading and removing them from the list.
		for (int destination : context.getElevatorRequests()) {
			// Check if the request is in the elevator's path.
			if (direction == DOWN && destination <= context.getFloorID() || direction == UP && destination >= context.getFloorID()) {
				// Generate and send a button press request to the elevator
				Event<ElevatorEventType> event = new Event<ElevatorEventType>(
						Subsystem.ELEVATOR,
						elevatorID,
						Subsystem.FLOOR,
						context.getFloorID(),
						ElevatorEventType.ELEVATOR_BUTTON_PRESSED,
						destination);
				outputBuffer.addEvent(event);
				// Remove this request from the list.
				context.removeRequest(destination);
			}
		}
		return new FloorIdleState(this.context);
	}

}
