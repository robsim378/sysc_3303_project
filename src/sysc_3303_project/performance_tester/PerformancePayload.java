/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */
package sysc_3303_project.performance_tester;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * The payload for performance events.
 *
 * @author Ian Holmes, Robert Simionescu
 */
public record PerformancePayload(int sourceFloor, int destinationFloor, int elevatorID, LocalTime requestTime) implements Serializable {

    /**
     * Constructor
     *
     * @param sourceFloor      int, source floor number for the request
     * @param destinationFloor int, destination floor number for the request
     * @param elevatorID       int, id of elevator for the request
     * @param requestTime      LocalTime, the time
     */
    public PerformancePayload {
    }
}
