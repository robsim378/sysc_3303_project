/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.floor_subsystem.physical_components;

import logging.Logger;

/**
 * Floor button for the floor requests
 * @author Liam
 *
 */
public class FloorButton {
	
	/**
	 * Name of the button
	 */
	private String name;
	
	/**
	 * Floor the button is on
	 */
	private int floor;
	
	/**
	 * Constructor
	 * @param name		String, name of button
	 * @param floor		int, floor button is on
	 */
	public FloorButton(String name, int floor) {
		this.name = name;
		this.floor = floor;
	}
	
	/**
	 * Press the button
	 */
	public void press() {
		Logger.getLogger().logDebug(this.getClass().getSimpleName(), name + " button pressed on floor " + floor);
	}

}
