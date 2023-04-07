package sysc_3303_project.performance_tester;

import logging.Logger;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * @author Robert Simionescu, Ian Holmes
 *
 * System for measuring the performance of the system. Measures the time taken to service a request.
 */
public class PerformanceTester implements Runnable {

    /**
     * The filepath for the output file containing the time taken to service a request
     */
    public static final String SERVICED_TIME_FILEPATH = System.getProperty("user.dir") + "\\src\\sysc_3303_project\\performance_tester\\output\\service_times.csv";

    /**
     * The filepath for the output file containing the time taken to schedule a request
     */
    public static final String SCHEDULED_TIME_FILEPATH = System.getProperty("user.dir") + "\\src\\sysc_3303_project\\performance_tester\\output\\schedule_times.csv";

    /**
     * EventBuffer for receiving performance data
     */
    private final EventBuffer<PerformanceEventType> inputBuffer;

    /**
     * List of requests that have not finished being serviced.
     */
    private final ArrayList<PerformanceRequestData> ongoingRequests;

    /**
     * List of requests that have been read from the input file but not yet scheduled
     */
    private final ArrayList<PerformanceRequestData> pendingRequests;

    public PerformanceTester(EventBuffer<PerformanceEventType> inputBuffer) {
        this.inputBuffer = inputBuffer;
        ongoingRequests = new ArrayList<>();
        pendingRequests = new ArrayList<>();

        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SCHEDULED_TIME_FILEPATH, true)))) {
            out.println("request made,request scheduled,time elapsed");
            Logger.getLogger().logNotification(getClass().getSimpleName(), SCHEDULED_TIME_FILEPATH + " created.");
        } catch (IOException e) {
            Logger.getLogger().logError(getClass().getSimpleName(), SCHEDULED_TIME_FILEPATH + " creation failed.");
            e.printStackTrace();
        }
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SERVICED_TIME_FILEPATH, true)))) {
            out.println("request scheduled,request serviced,time elapsed");
            Logger.getLogger().logNotification(getClass().getSimpleName(), SERVICED_TIME_FILEPATH + " created.");
        } catch (IOException e) {
            Logger.getLogger().logError(getClass().getSimpleName(), SERVICED_TIME_FILEPATH + " creation failed.");
            e.printStackTrace();
        }

//        try {
//            FileWriter myWriter = new FileWriter(SCHEDULED_TIME_FILEPATH);
//            myWriter.write("request made,request scheduled,time elapsed");
//            myWriter.close();
//            Logger.getLogger().logNotification(getClass().getSimpleName(), SCHEDULED_TIME_FILEPATH + " created.");
//        } catch (IOException e) {
//            Logger.getLogger().logError(getClass().getSimpleName(), SCHEDULED_TIME_FILEPATH + " creation failed.");
//            e.printStackTrace();
//        }
//        try {
//            FileWriter myWriter = new FileWriter(SERVICED_TIME_FILEPATH);
//            myWriter.write("request scheduled,request serviced,time elapsed");
//            myWriter.close();
//            Logger.getLogger().logNotification(getClass().getSimpleName(), SERVICED_TIME_FILEPATH + " created.");
//        } catch (IOException e) {
//            Logger.getLogger().logError(getClass().getSimpleName(), SERVICED_TIME_FILEPATH + " creation failed.");
//            e.printStackTrace();
//        }
    }

    private void outputToFile(String filePath, PerformanceRequestData requestData, LocalTime endTime) {
        String newLine = "";
        LocalTime startTime = requestData.getRequestTime();

        newLine += startTime.toString() + ",";
        newLine += endTime.toString() + ",";
        newLine += Duration.between(startTime, endTime);

        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            out.println(newLine);
            Logger.getLogger().logDebug(getClass().getSimpleName(), "appended new line to " + filePath);
        } catch (IOException e) {
            Logger.getLogger().logError(getClass().getSimpleName(), "failed to write to " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Finds the first pending request to the specified destination floor
     * @param destinationFloor  int, the destination floor of the request
     * @return  PerformanceRequestData, the first request made to that floor.
     */
    private PerformanceRequestData findPendingRequest(int destinationFloor) {
        for (PerformanceRequestData i : pendingRequests) {
            if (i.getDestinationFloor() == destinationFloor) {
                return i;
            }
        }
        return null;
    }

    /**
     * Finds the first ongoing request to the specified destination floor for the specified elevator.
     * @param destinationFloor  int, the destination floor of the request
     * @param elevatorID    int, the ID of the elevator servicing the request.
     * @return  PerformanceRequestData, the first request matching the elevator and destination floor.
     */
    private PerformanceRequestData findOngoingRequest(int destinationFloor, int elevatorID){
        for (PerformanceRequestData i : ongoingRequests) {
            if (i.getDestinationFloor() == destinationFloor && i.getElevatorID() == elevatorID) {
                return i;
            }
        }
        return null;
    }

    /**
     * Creates a request with the specified data
     * @param requestTime   LocalTime, the time the request was made
     * @param destinationFloor  int, the destination floor of the request
     */
    private void startRequest(LocalTime requestTime, int destinationFloor) {
        pendingRequests.add(new PerformanceRequestData(requestTime, destinationFloor));
    }

    /**
     * Finds the first pending request for the specified destination floor, adds an elevatorID and moves it to the list
     * of ongoing requests and adds it to the scheduler output file.
     * @param destinationFloor  int, the destination floor of the request
     * @param elevatorID    int, the ID of the elevator servicing the request
     */
    private void createOngoingRequest(int destinationFloor, int elevatorID, LocalTime scheduledTime) {

        PerformanceRequestData request = findPendingRequest(destinationFloor);
        pendingRequests.remove(request);
        request.setElevatorID(elevatorID);
        ongoingRequests.add(request);
        outputToFile(SCHEDULED_TIME_FILEPATH, request, scheduledTime);
    }

    /**
     * Marks all requests with the specified destinationFloor and elevatorID as serviced and adds them to the event output
     * file with the time they were serviced.
     * @param destinationFloor  int, the destination floor of the request
     * @param elevatorID    int, the ID of the elevator servicing the request
     */
    private void ongoingRequestServiced(int destinationFloor, int elevatorID, LocalTime servicedTime) {

        PerformanceRequestData request = findOngoingRequest(destinationFloor, elevatorID);
        ongoingRequests.remove(request);
        outputToFile(SERVICED_TIME_FILEPATH, request, servicedTime);

        while (request != null) {
            request = findOngoingRequest(destinationFloor, elevatorID);
            ongoingRequests.remove(request);
            outputToFile(SERVICED_TIME_FILEPATH, request, servicedTime);
        }
    }

    private void handleRequestRead(LocalTime requestTime, int destinationFloor) {
        startRequest(requestTime, destinationFloor);
    }

    private void handleRequestScheduled(LocalTime scheduledTime, int destinationFloor, int elevatorID) {
        createOngoingRequest(destinationFloor, elevatorID, scheduledTime);
    }

    private void handleRequestServiced(LocalTime servicedTime, int destinationFloor, int elevatorID) {
        ongoingRequestServiced(destinationFloor, elevatorID, servicedTime);
    }

    @Override
    public void run() {
        while (true) {
            Event<PerformanceEventType> event = inputBuffer.getEvent();
            switch (event.getEventType()) {
                case REQUEST_READ -> handleRequestRead((LocalTime)event.getPayload(), event.getSourceID());
                case REQUEST_SCHEDULED -> handleRequestScheduled((LocalTime)event.getPayload(), event.getSourceID(), event.getDestinationID());
                case REQUEST_SERVICED -> handleRequestServiced((LocalTime)event.getPayload(), event.getSourceID(), event.getDestinationID());
            }
        }
    }
}
