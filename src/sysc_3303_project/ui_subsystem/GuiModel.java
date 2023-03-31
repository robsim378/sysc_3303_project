/**
 * 
 */
package sysc_3303_project.ui_subsystem;

import logging.Logger;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;

/**
 * @author apope
 *
 */
public class GuiModel {
	
	private final EventBuffer<GuiEventType> inputBuffer;
	private final EventBuffer<Enum<?>> outputBuffer;
	
	public GuiModel(EventBuffer<GuiEventType> inputBuffer, EventBuffer<Enum<?>> outputBuffer) {
		this.inputBuffer = inputBuffer;
		this.outputBuffer = outputBuffer;
	}
	
	private void handleFloorLampStatusChange(int floor, boolean isLit) {
		
	}
	
	private void handleDirectionalLampStatusChange(int elevatorId, Direction direction) {
		
	}
	
	private void handleElevatorAtFloor(int elevatorId, int floor) {
		
	}
	
	private void handleElevatorDoorsFault(int elevatorId, boolean isBlocked) {
		
	}
	
	private void handleElevatorShutdownFault(int elevatorId) {
		
	}
	
	private void handleElevatorLampStatusChange(int elevatorId, ElevatorLampStatus lampStatus) {
		
	}
	
	private void handleDoorStatusChange(int elevatorId, DoorStatus status) {
		
	}
	
	public void eventLoop() {
		while (true) {
			Event<GuiEventType> evt = inputBuffer.getEvent();
			
			Logger.getLogger().logDebug(this.getClass().getSimpleName(), "Event: " + evt.getEventType());
			
			switch (evt.getEventType()) {
				case FLOOR_LAMP_STATUS_CHANGE -> handleFloorLampStatusChange(evt.getSourceID(), (boolean) evt.getPayload());
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
