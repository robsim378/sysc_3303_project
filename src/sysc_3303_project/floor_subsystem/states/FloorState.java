/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.floor_subsystem.states;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.RequestData;
import sysc_3303_project.common.state.State;

/**
 * 
 * @author Liam Gaudet
 * 
 * Abstract class for floor system states
 *
 */
public abstract class FloorState implements State {

	@Override
	public void doEntry() {}

	@Override
	public void doExit() {}
	
	/**
	 * Handles pressing a button a floor
	 * @param requestData		RequestData, the data for the button request
	 * @return					FloorState, the next state
	 */
	public abstract FloorState handleButtonPressed(RequestData requestData);

	/**
	 * Handles an elevator arriving at a floor
	 * @param direction 		Direction, the direction the elevator will be going.
	 * @return					FloorState, the next state
	 */
	public abstract FloorState handleElevatorArrived(Direction direction, int elevatorID);
	
	/**
	 * Handles the direction of the elevator for the floor's directional lamp
	 * @param direction		Direction of the elevator
	 * @param elevatorID	Elevator lamp to update
	 * @return				FloorState, Next state
	 */
	public abstract FloorState handleElevatorDirection(Direction direction, int elevatorID);
}
