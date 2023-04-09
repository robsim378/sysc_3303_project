package sysc_3303_project.performance_tester;

import java.io.Serializable;
import java.time.LocalTime;

public class PerformancePayload implements Serializable {

    private final int floorNumber;
    private final int elevatorID;

    private final LocalTime requestTime;

    /**
     * Constructor
     *
     * @param floorNumber int, floor number for the request
     * @param elevatorID  int, id of elevator for the request
     * @param requestTime LocalTime, the time
     */
    public PerformancePayload(int floorNumber, int elevatorID, LocalTime requestTime) {
        this.floorNumber = floorNumber;
        this.elevatorID = elevatorID;
        this.requestTime = requestTime;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getElevatorID() {
        return elevatorID;
    }

    public LocalTime getRequestTime() {
        return requestTime;
    }
}
