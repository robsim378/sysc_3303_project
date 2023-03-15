/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */


package sysc_3303_project.floor_subsystem.physical_components;

import logging.Logger;

/**
 * Floor lamp for floor buttons
 * @author Liam
 *
 */
public class FloorLamp {
	
	/**
	 * If lamp is on
	 */
	private boolean isOn;
	
	/**
	 * Name of button
	 */
	private String name;
	
	/**
	 * Floor the button is on
	 */
	private int floor;

	/**
	 * Constructor
	 * @param name		String, name of lamp
	 * @param floor		int, floor lamp is on
	 */
	public FloorLamp(String name, int floor) {
		isOn = false;
		this.name = name;
		this.floor = floor;
	}
	
	/**
	 * Getter for lamp state
	 * @return	boolean, if button is turned on
	 */
	public boolean isTurnedOn() {
		return isOn;
	}
	
	/**
	 * Setter for "on" state
	 */
	public void turnOn() {
		isOn = true;
		Logger.getLogger().logDebug(this.getClass().getSimpleName(), name + " lamp turned to \"on\" in floor " + floor);
	}
	
	/**
	 * Setter for "off" state
	 */
	public void turnOff() {
		isOn = false;
		Logger.getLogger().logDebug(this.getClass().getSimpleName(), name + "lamp turned to \"off\" in floor " + floor);
	}

}
