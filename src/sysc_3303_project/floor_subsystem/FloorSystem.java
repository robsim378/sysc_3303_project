/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.floor_subsystem;

import java.util.ArrayList;
import logging.Logger;
import sysc_3303_project.floor_subsystem.states.FloorState;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.events.RequestData;
import sysc_3303_project.floor_subsystem.states.FloorIdleState;

/**
 * @author Liam Gaudet and Robert Simionescu
 * Represents the floor subsystem of the project. Includes parsing data from the request file,
 * sending request information to the Scheduler and receiving requests from the scheduler.
 */
public class FloorSystem implements Runnable{

	/**
	 * Current state of the floor
	 */
	private FloorState state;

	/**
	 * Whether or not the floor's lamp is illuminated.
	 */
	private Lamps lamps;

	/**
	 * The ID of the floor. Also the floor number.
	 */
	private int floorID;

	/**
	 * The list of currently pending requests at this floor.
	 */
	private ArrayList<Integer> elevatorRequests;

    /**
     * Event buffer for incoming messages to the floor system
     */
    private EventBuffer<Enum<?>> outputBuffer;
    
    /**
     * Event buffer for submitting actions to the scheduler
     */
    private EventBuffer<FloorEventType> inputBuffer;

    /**
     * Constructor for the Floor System class.
	 * @param floorID 		int, the ID/floor number of the floor
	 * @param inputBuffer	EventBuffer<FloorEventType>, the buffer for incoming events to this floor.
	 * @param outputBuffer 	EventBuffer<Enum<?>>, the buffer for outgoing events from this floor.
     */
    public FloorSystem (int floorID, EventBuffer<FloorEventType> inputBuffer, EventBuffer<Enum<?>> outputBuffer) {
        this.inputBuffer = inputBuffer;
        this.outputBuffer = outputBuffer;
		this.floorID = floorID;
		elevatorRequests = new ArrayList<>();
		lamps = new Lamps();
        state = new FloorIdleState(this);
    }

	/**
	 * Add a request to the list of pending destinations.
	 * @param destinationFloor	int, the floor to add a request for.
	 */
	public void addElevatorRequest(int destinationFloor) {
		elevatorRequests.add(destinationFloor);
	}

	/**
	 * Get the list of pending floor requests
	 * @return	ArrayList<Integer>, the list of floors that people are waiting to go to.
	 */
	public ArrayList<Integer> getElevatorRequests() {
		return elevatorRequests;
	}

	/**
	 * Remove a floor request from the list once it has been serviced.
	 * @param destinationFloor	int, the floor number of the request to remove from the list
	 */
	public void removeElevatorRequest(Integer destinationFloor) {
		elevatorRequests.remove(destinationFloor);
	}

	/**
	 * Gets the current state. Should probably only be used for testing.
	 * @return FloorState, the current state of the floor.
	 */
	public FloorState getState() {
		return state;
	}

	/**
	 * Get the input buffer of the floor.
	 * @return	EventBuffer<FloorEventType>, the floor's input buffer
	 */
	public EventBuffer<FloorEventType> getInputBuffer() {
		return inputBuffer;
	}

	/**
	 * Get the output buffer of the floor.
	 * @return	EventBuffer<Enum<?>>, the floor's output buffer
	 */
	public EventBuffer<Enum<?>> getOutputBuffer() {
		return outputBuffer;
	}

	/**
	 * Get the ID of this floor
	 * @return	int, the floorID of this floor.
	 */
	public int getFloorID() {
		return floorID;
	}

	/**
	 * Gets the status of the up directional lamp.
	 * @return  Boolean, true if the up lamp is on, false if it is off.
	 */
	public boolean getUpDirectionalLampStatus() {
		return lamps.getUpDirectionalLampStatus();
	}

	/**
	 * Gets the status of the down directional lamp.
	 * @return  Boolean, true if the down lamp is on, false if it is off.
	 */
	public boolean getDownDirectionalLampStatus() {
		return lamps.getDownDirectionalLampStatus();
	}

	/**
	 * Gets the status of the up button lamp.
	 * @return  Boolean, true if the up lamp is on, false if it is off.
	 */
	public boolean getUpButtonLampStatus() {
		return lamps.getUpButtonLampStatus();
	}

	/**
	 * Gets the status of the down button lamp.
	 * @return  Boolean, true if the down lamp is on, false if it is off.
	 */
	public boolean getDownButtonLampStatus() {
		return lamps.getDownButtonLampStatus();
	}

	/**
	 * Lights the directional lamp for the given direction
	 * @param direction     Direction, the directional lamp to light.
	 */
	public void lightDirectionalLamp(Direction direction) {
		lamps.lightDirectionalLamp(direction);
	}

	/**
	 * Lights the button lamp for the given direction
	 * @param direction     Direction, the button lamp to light.
	 */
	public void lightButtonLamp(Direction direction) {
		lamps.lightButtonLamp(direction);
	}

	/**
	 * Turns off all directional lamps.
	 * @param direction     Direction, the directional lamp to turn off.
	 */
	public void clearDirectionalLamps(Direction direction) {
		lamps.clearDirectionalLamps(direction);
	}

	/**
	 * Turns off all button lamps.
	 * @param direction     Direction, the button lamp to turn off.
	 */
	public void clearButtonLamps(Direction direction) {
		lamps.clearButtonLamps(direction);
	}

	/**
     * Repetitively processes the floor system state pattern
     */
    public void beginLoop() {
    	
    	// Infinitely looping
    	while(true) {
    		
    		// Wait to receive an event
    		Event<FloorEventType> event = inputBuffer.getEvent();

    		Logger.getLogger().logNotification(this.getClass().getName(), "Event: " + event.getEventType() + ", State: " + state.getClass().getName());    		

    		// Go through the exit process for the state, transfer to the new state, and enact the entry activity for the new state
    		this.state.doExit();
    		    		
    		switch(event.getEventType()) {
    			case BUTTON_PRESSED:
    				this.state = this.state.handleButtonPressed((RequestData) event.getPayload());
    				break;
				case PASSENGERS_LOADED:
					// For a PASSENGERS_LOADED message, the sourceID is the ID of the elevator that has arrived,
					// not the scheduler that sent the message.
					this.state = this.state.handleElevatorArrived((Direction) event.getPayload(), event.getSourceID());
					break;
    			default:
    				throw new IllegalArgumentException();
    			}
    		
    		this.state.doEntry();
    		
    		
    	}
    }

    /**
     * Runnable method of the Floor Subsystem.
     */
    @Override
    public void run() {
    	Logger.getLogger().logNotification(this.getClass().getName(), "Floor thread started");

    	Logger.getLogger().logNotification(this.getClass().getName(), "FloorSystem: Beginning state loop");
    	beginLoop();
    	
    	
    }


}
