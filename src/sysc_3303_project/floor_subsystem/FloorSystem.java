/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package sysc_3303_project.floor_subsystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalTime;
import java.util.ArrayList;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;
import logging.Logger;
import sysc_3303_project.DelayTimerThread;
import sysc_3303_project.Event;
import sysc_3303_project.EventBuffer;
import sysc_3303_project.RequestData;

/**
 * @author Liam Gaudet
 * Represents the floor subsystem of the project. Includes parsing data from the request file,
 *   sending request information to the Scheduler and receiving requests from the scheduler.
 */
public class FloorSystem implements Runnable{

	
	private FloorState state;

    private String textFileLocation;
    private EventBuffer<FloorEventType> eventBuffer;
    private EventBuffer<SchedulerEventType> schedulerBuffer;

    /**
     * Constructor for the Floor System class.
     * @param scheduler			Scheduler, the scheduler to communicate with
     * @param textFileLocation	String, the text tile to use for parsing
     */
    public FloorSystem (EventBuffer<SchedulerEventType> schedulerBuffer, String textFileLocation){
        this.schedulerBuffer = schedulerBuffer;
        this.textFileLocation = textFileLocation;
        eventBuffer = new EventBuffer<>();
        state = new FloorWaitingState(this);
    }
    
    public EventBuffer<FloorEventType> getEventBuffer() {
    	return eventBuffer;
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
    			
    			Logger.getLogger().logNotification(this.getClass().getName(), "FloorSystem: Parsing line from text file");
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
    
    public void beginLoop() {
    	
    	while(true) {
    		
    		Event<FloorEventType> event = eventBuffer.getEvent();
    		
    		this.state.doExit();
    		
    		FloorEventType eventType = event.getEventType();
    		
    		Logger.getLogger().logNotification(this.getClass().getName(), "FloorSystem: Received event of type: " + eventType);
    		
    		switch(eventType) {
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
    		
    		Logger.getLogger().logNotification(this.getClass().getName(), "FloorSystem: Delaying request to queue");
    		Event<FloorEventType> event = new Event<FloorEventType>(FloorEventType.BUTTON_PRESSED, this, data);
    		DelayTimerThread<FloorEventType> runnableMethod = new DelayTimerThread<FloorEventType>(time, event, this.eventBuffer);
    		
    		new Thread(runnableMethod).start();
    		
    	}
    	
    	Logger.getLogger().logNotification(this.getClass().getName(), "FloorSystem: Beginning state loop");
    	beginLoop();
    	
    	Logger.getLogger().logNotification(this.getClass().getName(), "Terminating program.");
    	
    }
}
