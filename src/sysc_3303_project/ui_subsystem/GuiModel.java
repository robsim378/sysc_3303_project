/**
 * 
 */
package sysc_3303_project.ui_subsystem;

import logging.Logger;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;

/**
 * @author Andrei Popescu
 *
 */
public class GuiModel {
	
	private class ElevatorInfo {
		
		private boolean[] floorLamps;
		private int position = 0;
		private DoorStatus doorStatus = DoorStatus.DOORS_OPEN;
		private Direction direction = null;
		private boolean hasDoorsFault = false;
		private boolean isShutdown = false;
		
		/**
		 * Initializes the model's representation of an Elevator.
		 * @param floors the total number of floors
		 */
		public ElevatorInfo(int floors) {
			floorLamps = new boolean[floors];
			for (int i = 0; i < floors; i++) {
				floorLamps[i] = false; 
			}
		}
	}
	
	private class FloorInfo {
		private boolean upButton;
		private boolean downButton;
		
		public FloorInfo(int floorNumber, int floors) {
			downButton = (floorNumber == 0) ? null : false;
			upButton = (floorNumber == floors - 1) ? null : false;
		}
	}
	
	private final EventBuffer<GuiEventType> inputBuffer;
	private final EventBuffer<Enum<?>> outputBuffer;
	private final ElevatorInfo[] elevators;
	private final FloorInfo[] floors;
	
	public GuiModel(int floorsCount, int elevatorsCount, EventBuffer<GuiEventType> inputBuffer, EventBuffer<Enum<?>> outputBuffer) {
		this.inputBuffer = inputBuffer;
		this.outputBuffer = outputBuffer;
		this.elevators = new ElevatorInfo[elevatorsCount];
		this.floors = new FloorInfo[floorsCount];
		for (int i = 0; i < elevatorsCount; i++) {
			this.elevators[i] = new ElevatorInfo(floorsCount);
		}
		for (int i = 0; i < floorsCount; i++) {
			this.floors[i] = new FloorInfo(i, floorsCount); 
		}
	}
	
	public boolean getFloorUpLamp(int floor) {
		return floors[floor].upButton;
	}
	
	public boolean getFloorDownLamp(int floor) {
		return floors[floor].downButton;
	}
	
	public int getElevatorPosition(int elevatorId) {
		return elevators[elevatorId].position;
	}
	
	public Direction getElevatorDirection(int elevatorId) {
		return elevators[elevatorId].direction;
	}
	
	public DoorStatus getElevatorDoorStatus(int elevatorId) {
		return elevators[elevatorId].doorStatus;
	}
	
	public boolean[] getElevatorButtonLamps(int elevatorId) {
		return elevators[elevatorId].floorLamps;
	}
	
	public boolean hasElevatorDoorsFault(int elevatorId) {
		return elevators[elevatorId].hasDoorsFault;
	}
	
	public boolean isElevatorShutdown(int elevatorId) {
		return elevators[elevatorId].isShutdown;
	}
	
	private void handleFloorLampStatusChange(int floor, FloorLampStatus lampStatus) {
		if (lampStatus.getDirection() == Direction.UP) {
			this.floors[floor].upButton = lampStatus.getStatus();
		} else {
			this.floors[floor].downButton = lampStatus.getStatus();
		}
	}
	
	private void handleDirectionalLampStatusChange(int elevatorId, Direction direction) {
		this.elevators[elevatorId].direction = direction;
	}
	
	private void handleElevatorAtFloor(int elevatorId, int floor) {
		this.elevators[elevatorId].position = floor;
	}
	
	private void handleElevatorDoorsFault(int elevatorId, boolean isBlocked) {
		this.elevators[elevatorId].hasDoorsFault = isBlocked;
	}
	
	private void handleElevatorShutdownFault(int elevatorId) {
		this.elevators[elevatorId].isShutdown = true;
	}
	
	private void handleElevatorLampStatusChange(int elevatorId, ElevatorLampStatus lampStatus) {
		this.elevators[elevatorId].floorLamps[lampStatus.getFloorNumber()] = lampStatus.getStatus();
	}
	
	private void handleDoorStatusChange(int elevatorId, DoorStatus status) {
		this.elevators[elevatorId].doorStatus = status;
	}
	
	public void eventLoop() {
		while (true) {
			Event<GuiEventType> evt = inputBuffer.getEvent();
			
			Logger.getLogger().logDebug(this.getClass().getSimpleName(), "Event: " + evt.getEventType());
			
			switch (evt.getEventType()) {
				case FLOOR_LAMP_STATUS_CHANGE -> handleFloorLampStatusChange(evt.getSourceID(), (FloorLampStatus) evt.getPayload());
				case DIRECTIONAL_LAMP_STATUS_CHANGE -> handleDirectionalLampStatusChange((int) evt.getSourceID(), (Direction) evt.getPayload());
				case ELEVATOR_AT_FLOOR -> handleElevatorAtFloor(evt.getSourceID(), (int) evt.getPayload());
				case ELEVATOR_DOORS_FAULT -> handleElevatorDoorsFault(evt.getSourceID(), (boolean) evt.getPayload());
				case ELEVATOR_SHUTDOWN_FAULT -> handleElevatorShutdownFault((int) evt.getPayload());
				case ELEVATOR_LAMP_STATUS_CHANGE -> handleElevatorLampStatusChange(evt.getSourceID(), (ElevatorLampStatus) evt.getPayload()); //convert to pair
				case ELEVATOR_DOOR_STATUS_CHANGE -> handleDoorStatusChange(evt.getSourceID(), (DoorStatus) evt.getPayload());
			}
		}
	}
}
