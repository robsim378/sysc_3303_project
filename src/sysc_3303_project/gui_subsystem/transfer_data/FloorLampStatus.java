/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */
package sysc_3303_project.gui_subsystem.transfer_data;

import java.io.Serializable;

import sysc_3303_project.common.Direction;

/**
 * Basically a pair for data transfer
 * @author Andrei Popescu
 *
 */
public class FloorLampStatus implements Serializable {
	
	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Data to pass
	 */
	private final Direction direction;
	private final boolean status;
	
	/**
	 * Constructor
	 * @param direction		Direction, direction
	 * @param status		boolean, lamp status
	 */
	public FloorLampStatus(Direction direction, boolean status) {
		if (direction == null) {
			throw new IllegalArgumentException("Floor lamp direction cannot be null!");
		}
		this.direction = direction;
		this.status = status;
	}
	
	/**
	 * Getter for direction
	 * @return	Direction, direction of lamp
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Getter for lamp status
	 * @return	boolean, new lamp status
	 */
	public boolean getStatus() {
		return status;
	}
}
