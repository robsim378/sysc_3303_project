/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */
package sysc_3303_project.gui_subsystem;

import java.util.LinkedList;
import java.util.List;

import logging.Logger;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.gui_subsystem.model.SystemModel;
import sysc_3303_project.gui_subsystem.transfer_data.DoorStatus;
import sysc_3303_project.gui_subsystem.transfer_data.ElevatorLampStatus;
import sysc_3303_project.gui_subsystem.transfer_data.FloorLampStatus;
import sysc_3303_project.gui_subsystem.view.GuiView;

/**
 * @author Andrei Popescu
 *
 */
public class GuiContext implements Runnable{
	
	/**
	 * Input buffer to read from
	 */
	private final EventBuffer<GuiEventType> inputBuffer;
	
	/**
	 * Storage for elevator and floor information
	 */
	private final SystemModel model;
	
	/**
	 * List of all views to publish information to
	 */
	private List<GuiView> registeredViews;
	
	/**
	 * Constructs a GUI model with an input buffer to recieve information from 
	 * @param inputBuffer	EventBuffer<>, buffer to process info from
	 */
	public GuiContext(EventBuffer<GuiEventType> inputBuffer) {
		this.registeredViews = new LinkedList<>();
		this.inputBuffer = inputBuffer;
		
		this.model = new SystemModel();
	}
	
	/**
	 * Getter for the system model
	 * @return	SystemModel, model
	 */
	public SystemModel getModel() {
		return model;
	}
	
	/**
	 * Adds a view to the model
	 * @param view		GuiView, view to add
	 */
	public void addView(GuiView view) {
		registeredViews.add(view);
	}
	
	/**
	 * Handles a floor's lamp changing
	 * @param floor			int, floor being changed
	 * @param lampStatus	FloorLampStatus, button to change to value
	 */
	public void handleFloorLampStatusChange(int floor, FloorLampStatus lampStatus) {
		if (lampStatus.getDirection() == Direction.UP) {
			model.getFloors()[floor].setUpButton(lampStatus.getStatus());
		} else {
			model.getFloors()[floor].setDownButton(lampStatus.getStatus());
		}
		for (GuiView view: registeredViews) view.updateFloorPanel(floor);
	}
	
	/**
	 * Behaviour for directional lamp changes
	 * @param elevatorId	int, ID of elevator
	 * @param direction		Direction, direction to set
	 */
	public void handleDirectionalLampStatusChange(int elevatorId, Direction direction) {
		model.getElevators()[elevatorId].setDirection(direction);

		for (GuiView view: registeredViews) view.updateElevatorDirectionalLamps(elevatorId);
	}
	
	/**
	 * Handles an elevator position changing
	 * @param elevatorId	int, elevator to adjust
	 * @param floor			int, floor to set
	 */
	public void handleElevatorAtFloor(int elevatorId, int floor) {
		model.getElevators()[elevatorId].setPosition(floor);
		for (GuiView view: registeredViews) view.updateElevatorPanel(elevatorId);
	}
	
	/**
	 * Handles a fault happening on the door
	 * @param elevatorId	int, ID of elevator
	 * @param isBlocked		if the elevator door is blocked
	 */
	public void handleElevatorDoorsFault(int elevatorId, boolean isBlocked) {
		model.getElevators()[elevatorId].setHasDoorsFault(isBlocked);
		for (GuiView view: registeredViews) view.updateElevatorPanel(elevatorId);
	}
	
	/**
	 * Handles an elevator shutdown
	 * @param elevatorId	int, ID of elevator to shutdown
	 */
	public void handleElevatorShutdownFault(int elevatorId) {
		model.getElevators()[elevatorId].setShutdown(true);
		for (GuiView view: registeredViews) view.updateElevatorDirectionalLamps(elevatorId);
	}
	
	/**
	 * Handles elevator button lamp status changing
	 * @param elevatorId	int, elevator ID
	 * @param lampStatus	ElevatorLampStatus, lamp to update to
	 */
	public void handleElevatorLampStatusChange(int elevatorId, ElevatorLampStatus lampStatus) {
		model.getElevators()[elevatorId].getFloorLamps()[lampStatus.getFloorNumber()] = lampStatus.getStatus();
		for (GuiView view: registeredViews) view.updateElevatorPanel(elevatorId);
	}
	
	/**
	 * Handles the door status changing
	 * @param elevatorId	int, ID of elevator to update
	 * @param status		DoorStatus, status to change elevator to
	 */
	public void handleDoorStatusChange(int elevatorId, DoorStatus status) {
		model.getElevators()[elevatorId].setDoorStatus(status);
		for (GuiView view: registeredViews) view.updateElevatorPanel(elevatorId);
	}
	
	/**
	 * Main event loop for the GUI backing model
	 */
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

	@Override
	public void run() {
		eventLoop();
	}
}
