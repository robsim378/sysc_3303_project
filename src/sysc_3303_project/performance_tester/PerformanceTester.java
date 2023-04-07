package sysc_3303_project.performance_tester;

import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.floor_subsystem.FloorEventType;

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
    public static final String REQUEST_TIME_FILEPATH = "";

    /**
     * The filepath for the output file containing the time taken to schedule a request
     */
    public static final String SCHEDULER_TIME_FILEPATH = "";

    /**
     * EventBuffer for receiving performance data
     */
    private EventBuffer<PerformanceEventType> inputBuffer;

    /**
     * List of requests that have not finished being serviced.
     */
    private ArrayList<PerformanceRequestData> ongoingRequests;

    /**
     * List of requests that have been read from the input file but not yet scheduled
     */
    private ArrayList<PerformanceRequestData> pendingRequests;

    public PerformanceTester(EventBuffer<PerformanceEventType> inputBuffer) {
        this.inputBuffer = inputBuffer;

    }

    private void outputToFile(String filePath, PerformanceRequestData data, LocalTime servicedTime) {

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
        outputToFile(SCHEDULER_TIME_FILEPATH, request, scheduledTime);
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
        outputToFile(REQUEST_TIME_FILEPATH, request, servicedTime);

        while (request != null) {
            request = findOngoingRequest(destinationFloor, elevatorID);
            ongoingRequests.remove(request);
            outputToFile(REQUEST_TIME_FILEPATH, request, servicedTime);
        }
    }

    private void handleRequestRead(LocalTime requestTime, int destinationFloor) {
        startRequest(requestTime, destinationFloor);
    }

    private void handleRequestScheduled(LocalTime scheduledTime, int destinationFloor, int elevatorID) {
        createOngoingRequest(destinationFloor, elevatorID, scheduledTime);
    }

    private void handleRequestServiced(LocalTime scheduledTime, int destinationFloor, int elevatorID) {
        ongoingRequestServiced(destinationFloor, elevatorID, scheduledTime);
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
