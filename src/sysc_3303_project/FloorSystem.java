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

public class FloorSystem implements Runnable{

    private Scheduler scheduler;
    
    private String textFileLocation;

    public FloorSystem (Scheduler scheduler, String textFileLocation){
        this.scheduler = scheduler;
        this.textFileLocation = textFileLocation;
    }
    
    private ArrayList<RequestData> parseData(String textFileLocation){
    	ArrayList<RequestData> data = new ArrayList<RequestData>();
    	
    	BufferedReader br = null;
    	
    	System.out.println("FloorSystem: Starting to parse data from the text file");
    	try {
    		br = new BufferedReader(new FileReader(textFileLocation));
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	try {
    		while(true) {
    			
    			System.out.println("FloorSystem: Parsing line from text file");
    			String line = br.readLine();
    			
    			if(line == null)
    				break;
    			
    			data.add(RequestData.of(line));
    			
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return data;
    }

    @Override
    public void run() {
    	System.out.println("Floor thread started");
    	
    	ArrayList<RequestData> requestListFromTextFile = new ArrayList<RequestData>();
    	
    	requestListFromTextFile = parseData(textFileLocation);
    	
    	for (RequestData data: requestListFromTextFile){
    		System.out.println(String.format("FloorSystem: Sending request data \"%s\" to scheduler", data.toString()));
    		scheduler.addRequest(data);
    		System.out.println("FloorSystem: Awaiting response from scheduler");
    		
    		RequestData responseData = scheduler.getResponse();
    		System.out.println(String.format("FloorSystem: Recieved request data \"%s\" from scheduler", responseData.toString()));
    	}
    	System.out.println("All test cases accomplished! Terminating the program.");
    	System.exit(0);
    	
    }
}
