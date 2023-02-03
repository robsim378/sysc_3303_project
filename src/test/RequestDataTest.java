/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import sysc_3303_project.Direction;
import sysc_3303_project.RequestData;

public class RequestDataTest {

    @Test
    public void testGetRequestTime() {
        LocalTime time = LocalTime.of(12, 0, 0);
        RequestData request = new RequestData(time, 1, Direction.UP, 5);
        assertEquals(time, request.getRequestTime());
    }

    @Test
    public void testGetCurrentFloor() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5);
        assertEquals(1, request.getCurrentFloor());
    }

    @Test
    public void testGetDirection() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5);
        assertEquals(Direction.UP, request.getDirection());
    }

    @Test
    public void testGetDestinationFloor() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5);
        assertEquals(5, request.getDestinationFloor());
    }
}