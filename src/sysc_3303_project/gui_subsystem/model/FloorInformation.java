/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package sysc_3303_project.gui_subsystem.model;

import sysc_3303_project.common.configuration.ResourceManager;

/**
 * Floor information
 * @author Andrei, Liam
 *
 */
public class FloorInformation {
	// Relevant floor information
	private Boolean upButton;
	private Boolean downButton;
	
	/**
	 * Cconstructor for floor count
	 * @param floorNumber
	 */
	public FloorInformation(int floorNumber) {
		setDownButton((floorNumber == 0) ? null : false);
		setUpButton((floorNumber == ResourceManager.get().getInt("count.floors") - 1) ? null : false);
	}

	/**
	 * Getter for up botton
	 * @return	Boolean, up button status
	 */
	public Boolean getUpButton() {
		return upButton;
	}

	/**
	 * Getter for down button
	 * @return	Boolean, down button status
	 */
	public Boolean getDownButton() {
		return downButton;
	}

	/**
	 * Setter
	 * @param upButton	Boolean, new up button status
	 */
	public void setUpButton(Boolean upButton) {
		this.upButton = upButton;
	}
	
	/**
	 * Setter
	 * @param downButton	Boolean, new down button status
	 */
	public void setDownButton(Boolean downButton) {
		this.downButton = downButton;
	}

}
