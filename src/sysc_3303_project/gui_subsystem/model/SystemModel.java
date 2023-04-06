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
 * Backing system model
 * @author Andrei, Liam
 *
 */
public class SystemModel {
	
	/**
	 * Backing information for floor and elevator information
	 */
	private final ElevatorInformation[] elevators;
	private final FloorInformation[] floors;
	
	/**
	 * Constructor for the system model
	 */
	public SystemModel(){
		ResourceManager mngr = ResourceManager.get();
		int elevatorsCount = mngr.getInt("count.elevators");
		int floorsCount = mngr.getInt("count.floors");
		
		this.elevators = new ElevatorInformation[elevatorsCount];
		this.floors = new FloorInformation[floorsCount];
		for (int i = 0; i < elevatorsCount; i++) {
			this.elevators[i] = new ElevatorInformation();
		}
		for (int i = 0; i < floorsCount; i++) {
			this.floors[i] = new FloorInformation(i); 
		}
		}

	/**
	 * Get floors
	 * @return	FloorInformation[], floor information array
	 */
	public FloorInformation[] getFloors() {
		return floors;
	}

	/**
	 * Get elevators
	 * @return	ElevatorInformation[], elevator information array
	 */
	public ElevatorInformation[] getElevators() {
		return elevators;
	}
	
	/**
	 * Gets the floor's "up" button lamp status
	 * @param floor		int, floor to check
	 * @return			Boolean, if lamp is lit
	 */
	public Boolean getFloorUpLamp(int floor) {
		return getFloors()[floor].getUpButton();
	}
	/**
	 * Gets the floor's "down" button lamp status
	 * @param floor		int, floor to check
	 * @return			Boolean, if lamp is lit
	 */
	public Boolean getFloorDownLamp(int floor) {
		return getFloors()[floor].getDownButton();
	}
	public void setElevators(ElevatorInformation[] elevators) {
		System.arraycopy(elevators, 0, this.elevators, 0, elevators.length);
	}

	/**
	 * Gets the elevators position
	 * @param elevatorId	int, elevator to check
	 * @return				int, floor
	 */
	public int getElevatorPosition(int elevatorId) {
		return getElevators()[elevatorId].getPosition();
	}
	
	/**
	 * Direction an elvator is going
	 * @param elevatorId	int, elevator
	 * @return				Direction, direction elevator is moving
	 */
	public Direction getElevatorDirection(int elevatorId) {
		return isElevatorShutdown(elevatorId) ? null : getElevators()[elevatorId].getDirection();
	}
	
	/**
	 * Status of an elevator's door
	 * @param elevatorId	int, elevator to check
	 * @return				DoorStatus, status of los door
	 */
	public DoorStatus getElevatorDoorStatus(int elevatorId) {
		return getElevators()[elevatorId].getDoorStatus();
	}
	
	/**
	 * Returns all button lamps associated with an elevator
	 * @param elevatorId	int, elevator to check
	 * @return				boolean[], lamps on the elevator
	 */
	public boolean[] getElevatorButtonLamps(int elevatorId) {
		return getElevators()[elevatorId].getFloorLamps();
	}
	
	/**
	 * Checks if an elevator's door has done goofed
	 * @param elevatorId	int, elevator to check
	 * @return				boolean, if the elevator door is faulted
	 */
	public boolean hasElevatorDoorsFault(int elevatorId) {
		return getElevators()[elevatorId].isHasDoorsFault();
	}
	
	/**
	 * Checks if an elevator has shut down
	 * @param elevatorId	int, elevator to check
	 * @return				boolean, if it has shutdown
	 */
	public boolean isElevatorShutdown(int elevatorId) {
		return getElevators()[elevatorId].isShutdown();
	}
}
