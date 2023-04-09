package sysc_3303_project.performance_tester;

import java.io.Serializable;
import java.time.LocalTime;

public class PerformancePayload implements Serializable {

    private final int sourceFloor;
    private final int destinationFloor;
    private final int elevatorID;

    private final LocalTime requestTime;

    /**
     * Constructor
     *
     * @param sourceFloor int, source floor number for the request
     * @param destinationFloor int, destination floor number for the request
     * @param elevatorID  int, id of elevator for the request
     * @param requestTime LocalTime, the time
     */
    public PerformancePayload(int sourceFloor, int destinationFloor, int elevatorID, LocalTime requestTime) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.elevatorID = elevatorID;
        this.requestTime = requestTime;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public int getElevatorID() {
        return elevatorID;
    }

    public LocalTime getRequestTime() {
        return requestTime;
    }
}
