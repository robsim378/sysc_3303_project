/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.floor_subsystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalTime;
import java.util.ArrayList;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;
import logging.Logger;
import sysc_3303_project.floor_subsystem.states.FloorState;
import sysc_3303_project.common.DelayTimerThread;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.floor_subsystem.states.FloorIdleState;

/**
 * @author Liam Gaudet
 * Represents the floor subsystem of the project. Includes parsing data from the request file,
 *   sending request information to the Scheduler and receiving requests from the scheduler.
 */
public class FloorSystem implements Runnable{
	
	public static final int MAX_FLOOR_NUMBER = 10;
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
	private ArrayList<int> elevatorRequests;

    /**
     * Event buffer for incoming messages to the floor system
     */
    private EventBuffer<FloorEventType> outputBuffer;
    
    /**
     * Event buffer for submitting actions to the scheduler
     */
    private EventBuffer<Enum<?>> inputBuffer;

    /**
     * Constructor for the Floor System class.
     * @param scheduler			Scheduler, the scheduler to communicate with
     * @param textFileLocation	String, the text tile to use for parsing
     */
    public FloorSystem (int floorID, EventBuffer<FloorEventType> inputBuffer, EventBuffer<Enum<?>> outputBuffer) {
        this.inputBuffer = inputBuffer;
        this.textFileLocation = textFileLocation;
        this.outputBuffer = this.outputBuffer;
		this.floorID = floorID;
        state = new FloorIdleState(this);
    }

	public void addElevatorRequest(int destinationFloor) {
		elevatorRequests.add(destinationFloor);
	}

	public ArrayList<int> getElevatorRequests() {
		return elevatorRequests;
	}

	public void removeElevatorRequest(int destinationFloor) {
		elevatorRequests.remove(destinationFloor);
	}

	/**
	 * Get the ID of this floor
	 * @return	int, the floorID of this floor.
	 */
	public int getFloorID() {
		return floorID;
	}

	/**
	 * Gets the status of the up lamp.
	 * @return  Boolean, true if the up lamp is on, false if it is off.
	 */
	public boolean getUpLampStatus() {
		return lamps.getUpLampStatus();
	}

	/**
	 * Gets the status of the down lamp.
	 * @return  Boolean, true if the down lamp is on, false if it is off.
	 */
	public boolean getDownLampStatus() {
		return lamps.getDownLampStatus();
	}

	/**
	 * Lights the directional lamp for the given direction
	 * @param direction     Direction, the directional lamp to light.
	 */
	public lightLamp(Direction direction) {
		lamps.lightLamp(Direction direction);
	}

	/**
	 * Turns off all lamps.
	 */

	public clearLamps() {
		lamps.clearLamps();
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
    				this.state = this.state.handleButtonPressed((RequestData) event.getPayload(), outputBuffer);
    				break;
				case PASSENGERS_LOADED:
					// For a PASSENGERS_LOADED message, the sourceID is the ID of the elevator that has arrived,
					// not the scheduler that sent the message.
					this.state = this.state.handleElevatorArrived((Direction) event.getPayload(), event.getSourceID(), outputBuffer)
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
