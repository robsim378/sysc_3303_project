/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */
package sysc_3303_project.gui_subsystem.transfer_data;

import java.io.Serializable;

/**
 * Lamp status for elevator.
 * Effectively a pair of data for floor number and status
 * @author Andrei
 *
 */
public class ElevatorLampStatus implements Serializable {
	
	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Data
	 */
	private final int floorNumber;
	private final boolean status;
	
	/**
	 * Constructor
	 * @param floorNumber	int, floor number for pair
	 * @param status		boolean, status of lamp for pair
	 */
	public ElevatorLampStatus(int floorNumber, boolean status) {
		this.floorNumber = floorNumber;
		this.status = status;
	}
	
	/**
	 * Getter for floor number
	 * @return	int, floor number
	 */
	public int getFloorNumber() {
		return floorNumber;
	}
	
	/**
	 * Getter for status
	 * @return	boolean, lamp status
	 */
	public boolean getStatus() {
		return status;
	}
}
