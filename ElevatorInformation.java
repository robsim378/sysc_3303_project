/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */
package sysc_3303_project.gui_subsystem.model;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.gui_subsystem.transfer_data.DoorStatus;

/**
 * Elevator information
 * @author Andrei and Liam
 */
public class ElevatorInformation {
	// Relevant elevator data
	private boolean[] floorLamps;
	private int position = 0;
	private DoorStatus doorStatus = DoorStatus.DOORS_OPEN;
	private Direction direction = null;
	private boolean hasDoorsFault = false;
	private boolean isShutdown = false;

	/**
	 * Initializes the model's representation of an Elevator.
	 */
	public ElevatorInformation() {
		setFloorLamps(new boolean[ResourceManager.get().getInt("count.floors")]);
		for (int i = 0; i < getFloorLamps().length; i++) {
			getFloorLamps()[i] = false; 
		}
	}
	
	/**
	 * Getter
	 * @return int, elevator position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Getter
	 * @return Direction, elevator direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Getter
	 * @return DoorStatus, status of door
	 */
	public DoorStatus getDoorStatus() {
		return doorStatus;
	}

	/**
	 * Setter
	 * @param doorStatus	DoorStatus, new status of door
	 */
	public void setDoorStatus(DoorStatus doorStatus) {
		this.doorStatus = doorStatus;
	}

	/**
	 * Getter
	 * @return	boolean[], floor lamps as array
	 */
	public boolean[] getFloorLamps() {
		return floorLamps;
	}

	/**
	 * Setter
	 * @param floorLamps	boolean[], new floor lamps status
	 */
	public void setFloorLamps(boolean[] floorLamps) {
		this.floorLamps = floorLamps;
	}

	/**
	 * Getter
	 * @return	boolean, if door has faults
	 */
	public boolean isHasDoorsFault() {
		return hasDoorsFault;
	}

	/**
	 * Setter
	 * @param hasDoorsFault	boolean, if door has fault
	 */
	public void setHasDoorsFault(boolean hasDoorsFault) {
		this.hasDoorsFault = hasDoorsFault;
	}

	/**
	 * Getter
	 * @return	boolean, elevator is shut down
	 */
	public boolean isShutdown() {
		return isShutdown;
	}

	/**
	 * Setter
	 * @param isShutdown	boolean, if elevator is shut down
	 */
	public void setShutdown(boolean isShutdown) {
		this.isShutdown = isShutdown;
	}

	/**
	 * Setter
	 * @param direction	Direction, new direction of elevator
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Setter
	 * @param position	int, new position of elevator
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	public int getCurrentFloor() {
		int floorHeight = ResourceManager.get().getInt("floor.height");
		int position = getPosition();
		int currentFloor = position / floorHeight;
		return currentFloor;
	}


}
