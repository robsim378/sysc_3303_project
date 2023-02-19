/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
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

	/**
	 * Current state of the floor
	 */
	private FloorState state;

	/**
	 * Location to get request data from
	 */
    private String textFileLocation;
    
    /**
     * Event buffer for actions made to the floor system
     */
    private EventBuffer<FloorEventType> eventBuffer;
    
    /**
     * Event buffer for submitting actions to the scheduler
     */
    private EventBuffer<SchedulerEventType> schedulerBuffer;

    /**
     * Constructor for the Floor System class.
     * @param scheduler			Scheduler, the scheduler to communicate with
     * @param textFileLocation	String, the text tile to use for parsing
     */
    public FloorSystem (EventBuffer<SchedulerEventType> schedulerBuffer, EventBuffer<FloorEventType> eventBuffer, String textFileLocation){
        this.schedulerBuffer = schedulerBuffer;
        this.textFileLocation = textFileLocation;
        this.eventBuffer = eventBuffer;
        state = new FloorIdleState(this);
    }
        
    /**
     * Parses data from the text file location specified by the class
     * @return	ArrayList<RequestData>, a list of requests parsed from the classes text file
     */
    private ArrayList<RequestData> parseData(){
    	ArrayList<RequestData> data = new ArrayList<RequestData>();
    	
    	BufferedReader br = null;
    	
    	Logger.getLogger().logNotification(this.getClass().getName(), "FloorSystem: Starting to parse data from the text file");
    	try {
    		br = new BufferedReader(new FileReader(textFileLocation));
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	// For each line in the text file, generate a request from it
    	try {
    		while(true) {
    			
    			//Logger.getLogger().logNotification(this.getClass().getName(), "FloorSystem: Parsing line from text file");
    			String line = br.readLine();
    			
    			if(line == null)
    				break;
    			
    			// If the provided line is invalid and throws an error during generation, catch and do not add
    			try {
        			data.add(RequestData.of(line));
    			} catch(Exception e) {
    				e.printStackTrace();
    			}
    			
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return data;
    }
    
    /**
     * Repetitively processes the floor system state pattern
     */
    public void beginLoop() {
    	
    	// Infinitely looping
    	while(true) {
    		
    		// Wait to receive an event
    		Event<FloorEventType> event = eventBuffer.getEvent();

    		Logger.getLogger().logNotification(this.getClass().getName(), "Event: " + event.getEventType() + ", State: " + state.getClass().getName());    		

    		// Go through the exit process for the state, transfer to the new state, and enact the entry activity for the new state
    		this.state.doExit();
    		    		
    		switch(event.getEventType()) {
    		case BUTTON_PRESSED:
    			this.state = this.state.handleButtonPressed((RequestData) event.getPayload(), schedulerBuffer);
    			break;
    		default:
    			throw new IllegalArgumentException();
    		}
    		
    		this.state.doEntry();
    		
    		
    	}
    }

    /**
     * Runnable method of the Floor Subsystem. Parses data from the test text file. Then
     *   repeatedly sends requests to the scheduler and receives responses from containing
     *   the original request data
     */
    @Override
    public void run() {
    	Logger.getLogger().logNotification(this.getClass().getName(), "Floor thread started");
    	
    	ArrayList<RequestData> requestListFromTextFile = new ArrayList<RequestData>();
    	
    	requestListFromTextFile = parseData();
    	
    	for (RequestData data: requestListFromTextFile){
    		LocalTime requestTime = data.getRequestTime();
    		
    		int time = requestTime.getHour()*60*60*1000 + requestTime.getMinute()*60*1000 + requestTime.getSecond()*1000 + (requestTime.getNano()/(1000 * 1000));
    		
    		Event<FloorEventType> event = new Event<FloorEventType>(FloorEventType.BUTTON_PRESSED, this, data);
    		DelayTimerThread<FloorEventType> runnableMethod = new DelayTimerThread<FloorEventType>(time, event, this.eventBuffer);
    		
    		new Thread(runnableMethod).start();
    		
    	}
    	
    	Logger.getLogger().logNotification(this.getClass().getName(), "FloorSystem: Beginning state loop");
    	beginLoop();
    	
    	
    }
}
