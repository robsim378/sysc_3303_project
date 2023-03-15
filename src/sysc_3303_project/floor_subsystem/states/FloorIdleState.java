/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.floor_subsystem.states;

import java.util.LinkedList;
import java.util.List;

import logging.Logger;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.RequestData;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
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
	 * Handles pressing a button on a floor
	 * @param requestData		RequestData, the data for the button request
	 * @return					FloorState, the next state
	 */
	@Override
	public FloorState handleButtonPressed(RequestData requestData) {
		Logger.getLogger().logNotification(this.getClass().getSimpleName(), "Sent request to scheduler: " + requestData.toString());

		context.pressButton(requestData.getDirection());
		context.lightButtonLamp(requestData.getDirection());
		
		// Send an event to the scheduler requesting an elevator.
		Event<Enum<?>> event = new Event<>(
				Subsystem.SCHEDULER,
				0,
				Subsystem.FLOOR,
				context.getFloorID(),
				SchedulerEventType.FLOOR_BUTTON_PRESSED,
				requestData.getDirection());
		context.getOutputBuffer().addEvent(event);

		// Adds the destination request to the list of requests waiting, which will be sent to an elevator when it arrives.
		context.getElevatorRequests().add(requestData.getDestinationFloor());

		context.lightButtonLamp(requestData.getDirection());

		return new FloorIdleState(this.context);
	}

	/**
	 * Handles an elevator arriving at a floor
	 * @param direction 		Direction, the direction the elevator will be going.
	 * @param elevatorID		int, the ID of the elevator that has arrived.
	 * @return					FloorState, the next state
	 */
	@Override
	public FloorState handleElevatorArrived(Direction direction, int elevatorID) {
		context.clearButtonLamps(direction);
		// Go through all the requests on the current floor, sending a button request to all the floors in the direction
		// the elevator is heading and removing them from the list.
		Logger.getLogger().logNotification(this.getClass().getSimpleName(), "Elevator " + elevatorID + " arrived at floor in direction " + direction);
		List<Integer> handledRequests = new LinkedList<>();
		for (int destination : context.getElevatorRequests()) {
			// Check if the request is in the elevator's path.
			if (direction == Direction.DOWN && destination <= context.getFloorID() || direction == Direction.UP && destination >= context.getFloorID()) {
				// Generate and send a button press request to the elevator
				Event<Enum<?>> event = new Event<>(
						Subsystem.ELEVATOR,
						elevatorID,
						Subsystem.FLOOR,
						context.getFloorID(),
						ElevatorEventType.ELEVATOR_BUTTON_PRESSED,
						destination);
				context.getOutputBuffer().addEvent(event);
				// Remove this request from the list.
				handledRequests.add(destination);
			}
		}
		for (int destination : handledRequests) {
			context.removeElevatorRequest(destination);
		}
		context.clearButtonLamps(direction);
		return new FloorIdleState(this.context);
	}

	@Override
	public FloorState handleElevatorDirection(Direction direction, int elevatorID) {
		this.context.clearDirectionalLamps(direction == Direction.UP? Direction.DOWN: Direction.UP, elevatorID);
		this.context.lightDirectionalLamp(direction, elevatorID);
		return new FloorIdleState(this.context);
	}

}
