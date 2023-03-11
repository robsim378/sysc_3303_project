/**
 * SYSC3303 Project
 * Group 1
 * @author khalid merai 101159203
 * @version 1.0
 */
package test.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.RequestData;

/**
 * Test class for RequestData
 */
public class RequestDataTest {
    /**
     * Tests that getting the request time returns the correct time.
     */
    @Test
    public void testGetRequestTime() {
        LocalTime time = LocalTime.of(12, 0, 0);
        RequestData request = new RequestData(time, 1, Direction.UP, 5);
        assertEquals(time, request.getRequestTime());
    }

    /**
     * Tests that getting the floor number from which the request was sent returns the correct floor.
     */
    @Test
    public void testGetCurrentFloor() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5);
        assertEquals(1, request.getCurrentFloor());
    }

    /**
     * Tests that getting the direction of the request returns the correct direction.
     */
    @Test
    public void testGetDirection() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5);
        assertEquals(Direction.UP, request.getDirection());
    }

    /**
     * Tests that getting the destination floor of the request returns the correct direction.
     */
    @Test
    public void testGetDestinationFloor() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5);
        assertEquals(5, request.getDestinationFloor());
    }
}