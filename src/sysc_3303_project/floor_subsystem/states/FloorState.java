/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.floor_subsystem.states;

import logging.Logger;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.common.State;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

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
	 * @param outputBuffer		EventBuffer, the location to send the data to.
	 * @return					FloorState, the next state
	 */
	public abstract FloorState handleButtonPressed(RequestData requestData, EventBuffer<SchedulerEventType> outputBuffer);

	/**
	 * Handles an elevator arriving at a floor
	 * @param direction 		Direction, the direction the elevator will be going.
	 * @param outputBuffer		EventBuffer, the location too send data to.
	 * @return					FloorState, the next state
	 */
	public abstract FloorState handleElevatorArrived(Direction direction, int elevatorID, EventBuffer<ElevatorEventType> outputBuffer);
}
