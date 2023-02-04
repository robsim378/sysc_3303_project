/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package sysc_3303_project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Liam Gaudet
 * Represents the floor subsystem of the project. Includes parsing data from the request file,
 *   sending request information to the Scheduler and receiving requests from the scheduler.
 */
public class FloorSystem implements Runnable{

    private Scheduler scheduler;
    
    private String textFileLocation;

    /**
     * Constructor for the Floor System class.
     * @param scheduler			Scheduler, the scheduler to communicate with
     * @param textFileLocation	String, the text tile to use for parsing
     */
    public FloorSystem (Scheduler scheduler, String textFileLocation){
        this.scheduler = scheduler;
        this.textFileLocation = textFileLocation;
    }
    
    /**
     * Parses data from the text file location specified by the class
     * @return	ArrayList<RequestData>, a list of requests parsed from the classes text file
     */
    private ArrayList<RequestData> parseData(){
    	ArrayList<RequestData> data = new ArrayList<RequestData>();
    	
    	BufferedReader br = null;
    	
    	System.out.println("FloorSystem: Starting to parse data from the text file");
    	try {
    		br = new BufferedReader(new FileReader(textFileLocation));
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	// For each line in the text file, generate a request from it
    	try {
    		while(true) {
    			
    			System.out.println("FloorSystem: Parsing line from text file");
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
     * Runnable method of the Floor Subsystem. Parses data from the test text file. Then
     *   repeatedly sends requests to the scheduler and receives responses from containing
     *   the original request data
     */
    @Override
    public void run() {
    	System.out.println("Floor thread started");
    	
    	ArrayList<RequestData> requestListFromTextFile = new ArrayList<RequestData>();
    	
    	requestListFromTextFile = parseData();
    	
    	for (RequestData data: requestListFromTextFile){
    		System.out.println(String.format("FloorSystem: Sending request data \"%s\" to scheduler", data.toString()));
    		scheduler.addRequest(data);
    		System.out.println("FloorSystem: Awaiting response from scheduler");
    		
    		RequestData responseData = scheduler.getResponse();
    		System.out.println(String.format("FloorSystem: Recieved request data \"%s\" from scheduler", responseData.toString()));
    	}
    	System.out.println("All test cases accomplished! Terminating program.");
    	System.exit(0);
    	
    }
}
