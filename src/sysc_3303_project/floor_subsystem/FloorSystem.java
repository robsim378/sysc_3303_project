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
import sysc_3303_project.common.events.DelayedEvent;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.events.RequestData;
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

	/**
	 *
	 * @param data
	 */
	public void addRequest(RequestData data) {
		// Generate the event to send to the scheduler
		event = new Event<SchedulerEventType>(
				Subsystem.SCHEDULER,
				schedulerid,
				Subsystem.FLOOR,
				floorID,
				SchedulerEventType.FLOOR_BUTTON_PRESSED,
				data.getDirection();
		)

		// Get the time to send the request
		LocalTime requestTime = data.getRequestTime();
		requestTime.getHour()*60*60*1000 + requestTime.getMinute()*60*1000 + requestTime.getSecond()*1000 + (requestTime.getNano()/(1000 * 1000));

		// Create a thread to send the request at the specified time.
		DelayTimerThread<FloorEventType> runnableMethod = new DelayTimerThread<FloorEventType>(time, event, this.outputBuffer);
		new Thread(runnableMethod).start();

		// Add the elevator buttons to press to the list.
		elevatorRequests.add(data.getDestinationFloor);
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
    				this.state = this.state.handleButtonPressed((RequestData) event.getPayload(), inputBuffer);
    				break;
				case PASSENGERS_LOADED:
					lamps.clearLamps();
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
