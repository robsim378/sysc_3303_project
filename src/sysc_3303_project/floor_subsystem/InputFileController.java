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

import logging.Logger;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.DelayTimerThread;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.RequestData;

/**
 * @author Robert Simionescu, Liam Gaudet
 * Reads the input file and sends its contents as events to their destinations.
 */
public class InputFileController implements Runnable {

    /**
     * Filepath to get request data from
     */
    private String textFileLocation;

    /**
     * The list of floors in the system
     */
    private ArrayList<FloorSystem> floors;

    /**
     * Constructor for the FloorMessageController
     * @param textFileLocation  String, the location of the input file.
     * @param floors    ArrayList<FloorSystem>, the floors in the system.
     */
    public InputFileController(String textFileLocation, ArrayList<FloorSystem> floors) {
        this.textFileLocation = textFileLocation;
        this.floors = floors;
    }

    /**
     * Parses data from the text file location specified by the class
     * @return	ArrayList<RequestData>, a list of requests parsed from the classes text file
     */
    private ArrayList<RequestData> parseData(){
        ArrayList<RequestData> data = new ArrayList<RequestData>();

        BufferedReader br = null;

        Logger.getLogger().logNotification(this.getClass().getSimpleName(), "FloorSystem: Starting to parse data from the text file");
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
     * Runnable method of the FloorMessageController Subsystem. Parses data from the test text file. and sends it to the
     * floors that need it, which then send it to the scheduler at the appropriate times.
     */
    @Override
    public void run() {
        Logger.getLogger().logNotification(this.getClass().getSimpleName(), "Input File Controller thread started");

        // Get and parse the input file
        ArrayList<RequestData> requestListFromTextFile = new ArrayList<RequestData>();
        requestListFromTextFile = parseData();

        // For every event in the input file, send it to the appropriate floor.
        for (RequestData data: requestListFromTextFile){


            // Generate the event to send to the floor
            Event<FloorEventType> event = new Event<FloorEventType>(
                    Subsystem.FLOOR,
                    -1, // This event is being sent from the input file, so it does not have a real source. This is just a placeholder since it will never actually be read.
                    Subsystem.FLOOR,
                    data.getCurrentFloor(),
                    FloorEventType.BUTTON_PRESSED,
                    data
		    );

            // Get the time to send the request
            LocalTime requestTime = data.getRequestTime();
            int time = requestTime.getHour()*60*60*1000 + requestTime.getMinute()*60*1000 + requestTime.getSecond()*1000 + (requestTime.getNano()/(1000 * 1000));

            // Create a thread to send the request at the specified time.
            DelayTimerThread<FloorEventType> runnableMethod = new DelayTimerThread<FloorEventType>(time, event, floors.get(data.getCurrentFloor()).getInputBuffer());
            new Thread(runnableMethod).start();
        }
    }

}