/**
 * 
 */
package sysc_3303_project.ui_subsystem;

import java.io.Serializable;

import sysc_3303_project.common.Direction;

/**
 * @author Andrei Popescu
 *
 */
public class FloorLampStatus implements Serializable {
	
	private final Direction direction;
	private final boolean status;
	
	public FloorLampStatus(Direction direction, boolean status) {
		if (direction == null) {
			throw new IllegalArgumentException("Floor lamp direction cannot be null!");
		}
		this.direction = direction;
		this.status = status;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public boolean getStatus() {
		return status;
	}
}
