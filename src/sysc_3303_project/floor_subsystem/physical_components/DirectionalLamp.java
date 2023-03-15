/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.floor_subsystem.physical_components;

import logging.Logger;
import sysc_3303_project.common.Direction;

/**
 * A directional lamp for the floor subsystem
 * @author Liam
 *
 */
public class DirectionalLamp {
	
	/**
	 * If the lamp is turned on
	 */
	private boolean isOn;
	
	/**
	 * Elevator number, for logging
	 */
	private int elevatorNum;
	
	/**
	 * Direction lamp faces, for logging
	 */
	private Direction direction;
	
	/**
	 * Floor the lamp is on, for debugging
	 */
	private int floor;
	
	/**
	 * Constructor
	 * @param elevatorNum		int, elevator number
	 * @param direction			Direction, direction lamp points
	 * @param floor				int, floor lamp is on
	 */
	public DirectionalLamp(int elevatorNum, Direction direction, int floor) {
		isOn = false;
		this.elevatorNum = elevatorNum;
		this.direction = direction;
		this.floor = floor;
	}
	
	/**
	 * Getter for lamp state
	 * @return boolean, if lamp is turned on
	 */
	public boolean isTurnedOn() {
		return isOn;
	}
	
	/**
	 * Setter for on state
	 */
	public void turnOn() {
		isOn = true;
		Logger.getLogger().logDebug(this.getClass().getSimpleName(), "Elevator #" + elevatorNum + " " + direction.toString() + "lamp turned to \"on\" in floor " + floor);

	}
	
	/**
	 * Setter for off state
	 */
	public void turnOff() {
		isOn = false;
		Logger.getLogger().logDebug(this.getClass().getSimpleName(), "Elevator #" + elevatorNum + " " + direction.toString() + "lamp turned to \"off\" in floor " + floor);

	}

}
