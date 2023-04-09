package sysc_3303_project.performance_tester;
import java.time.LocalTime;

/**
 * @author Robert Simionescu, Ian Holmes
 *
 * Format for keeping track of ongoing requests.
 */
public class PerformanceRequestData {

    /**
     * The time the request began
     */
    private final LocalTime requestTime;

    /**
     * The destination floor of the request
     */
    private final int destinationFloor;

    /**
     * The elevator servicing the request.
     */
    private int elevatorID;

    /**
     * Constructor for a PerformanceRequestData
     * @param requestTime   LocalTime, the time the request was made
     * @param destinationFloor  int, the destination floor of the request.
     */
    public PerformanceRequestData(LocalTime requestTime, int destinationFloor){
        this.requestTime = requestTime;
        this.destinationFloor = destinationFloor;
    }

    /**
     * Getter for the request time
     * @return  LocalTime, the time the request was made
     */
    public LocalTime getRequestTime() {
        return requestTime;
    }

    /**
     * Getter for the destination floor
     * @return  int, the destination floor of the request
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /**
     * Setter for the elevator ID
     * @param elevatorID    int, the ID of the elevator servicing the request.
     */
    public void setElevatorID(int elevatorID) {
        this.elevatorID = elevatorID;
    }

    /**
     * Getter for the elevator ID
     * @return  int, the ID of the elevator servicing the request.
     */
    public int getElevatorID() {
        return elevatorID;
    }

}
