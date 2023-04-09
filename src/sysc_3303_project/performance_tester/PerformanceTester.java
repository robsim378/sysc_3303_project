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
        Logger.getLogger().logNotification(getClass().getSimpleName(), requestData.toString());
        LocalTime startTime = requestData.getRequestTime();

        newLine += startTime.toString() + ",\t";
        newLine += endTime.toString() + ",\t";
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
    private PerformanceRequestData findPendingRequest(int sourceFloor) {
        for (PerformanceRequestData requestData : pendingRequests) {
            if (requestData.getSourceFloor() == sourceFloor) {
                return requestData;
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
        for (PerformanceRequestData requestData : ongoingRequests) {
            if (requestData.getDestinationFloor() == destinationFloor && requestData.getElevatorID() == elevatorID) {
                return requestData;
            }
        }
        return null;
    }

    /**
     * Creates a request with the specified data
     * @param requestTime   LocalTime, the time the request was made
     * @param destinationFloor  int, the destination floor of the request
     */
    private void startRequest(LocalTime requestTime, int sourceFloor, int destinationFloor) {
        pendingRequests.add(new PerformanceRequestData(requestTime, sourceFloor, destinationFloor));
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
        assert request != null;
        request.setElevatorID(elevatorID);
        ongoingRequests.add(request);
        Logger.getLogger().logNotification("TRY TO WRITE", "SCHEDULED");
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
        while (request != null) {
            ongoingRequests.remove(request);
            Logger.getLogger().logNotification("TRY TO WRITE", "SERVICED");
            outputToFile(SERVICED_TIME_FILEPATH, request, servicedTime);
            Logger.getLogger().logNotification(getClass().getSimpleName(), "Request: " + request + " serviced.");
            request = findOngoingRequest(destinationFloor, elevatorID);
        }
    }

    private void handleRequestRead(LocalTime requestTime, int sourceFloor, int destinationFloor) {
        Logger.getLogger().logDebug("handleRequestRead", "floor: " + destinationFloor);
//        Logger.getLogger().logDebug("handleRequestRead", "ongoingRequests: " + ongoingRequests);
//        Logger.getLogger().logDebug("handleRequestRead", "pendingRequests: " + pendingRequests);
        startRequest(requestTime, sourceFloor, destinationFloor);
    }

    private void handleRequestScheduled(LocalTime scheduledTime, int destinationFloor, int elevatorID) {
        Logger.getLogger().logDebug("handleRequestScheduled", "floor: " + destinationFloor + " elevatorID: " + elevatorID);
//        Logger.getLogger().logDebug("handleRequestScheduled", "ongoingRequests: " + ongoingRequests);
//        Logger.getLogger().logDebug("handleRequestScheduled", "pendingRequests: " + pendingRequests);
        createOngoingRequest(destinationFloor, elevatorID, scheduledTime);
    }

    private void handleRequestServiced(LocalTime servicedTime, int destinationFloor, int elevatorID) {
        Logger.getLogger().logDebug("handleRequestServiced", "floor: " + destinationFloor + " elevatorID: " + elevatorID);
//        Logger.getLogger().logDebug("handleRequestServiced", "ongoingRequests: " + ongoingRequests);
//        Logger.getLogger().logDebug("handleRequestServiced", "pendingRequests: " + pendingRequests);
        ongoingRequestServiced(destinationFloor, elevatorID, servicedTime);
    }

    @Override
    public void run() {
        while (true) {
            Event<PerformanceEventType> event = inputBuffer.getEvent();
            PerformancePayload payload = (PerformancePayload)event.getPayload();
            switch (event.getEventType()) {
                case REQUEST_READ -> handleRequestRead(payload.getRequestTime(), payload.getSourceFloor(), payload.getDestinationFloor());
                case REQUEST_SCHEDULED -> handleRequestScheduled(payload.getRequestTime(), payload.getDestinationFloor(), payload.getElevatorID());
                case REQUEST_SERVICED -> handleRequestServiced(payload.getRequestTime(), payload.getDestinationFloor(), payload.getElevatorID());
            }
        }
    }
}
