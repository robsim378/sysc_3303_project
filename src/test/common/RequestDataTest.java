/**
 * SYSC3303 Project
 * Group 1
 * @author khalid merai 101159203
 * @version 2.0
 */
package test.common;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.RequestData;

/**
 * Test class for RequestData
 */
public class RequestDataTest {
    private static final String VALID_REQUEST_LINE = "12:34:56.789 1 UP 5";
    private static final String REQUEST_LINE_WITH_ERROR = "12:34:56.789 1 UP 5 1";
    /**
     * Tests that getting the request time returns the correct time.
     */
    @Test
    public void testGetRequestTime() {
        LocalTime time = LocalTime.of(12, 0, 0);
        RequestData request = new RequestData(time, 1, Direction.UP, 5,2);
        assertEquals(time, request.getRequestTime());
    }


    /**
     * Tests that getting the floor number from which the request was sent returns the correct floor.
     */
    @Test
    public void testGetCurrentFloor() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5,1);
        assertEquals(1, request.getCurrentFloor());
    }

    /**
     * Tests that getting the direction of the request returns the correct direction.
     */
    @Test
    public void testGetDirection() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5,1);
        assertEquals(Direction.UP, request.getDirection());
    }

    /**
     * Tests that getting the destination floor of the request returns the correct direction.
     */
    @Test
    public void testGetDestinationFloor() {
        RequestData request = new RequestData(LocalTime.of(12, 0, 0), 1, Direction.UP, 5,3);
        assertEquals(5, request.getDestinationFloor());
    }
    /**
     * Tests the creation of a valid request and ensures that all fields are correctly set.
     */
    @Test
    public void testValidRequestCreation() {
        RequestData requestData = RequestData.of(VALID_REQUEST_LINE);

        assertEquals(requestData.getRequestTime(), LocalTime.of(12, 34, 56, 789000000));
        assertEquals(requestData.getCurrentFloor(), 1);
        assertEquals(requestData.getDirection(), Direction.UP);
        assertEquals(requestData.getDestinationFloor(), 5);
        assertFalse(requestData.hasError());
    }


    /**
     * Test request creation with an error.
     */
    @Test
    public void testRequestCreationWithError() {
        RequestData requestData = RequestData.of(REQUEST_LINE_WITH_ERROR);

        assertEquals(requestData.getRequestTime(), LocalTime.of(12, 34, 56, 789000000));
        assertEquals(requestData.getCurrentFloor(), 1);
        assertEquals(requestData.getDirection(), Direction.UP);
        assertEquals(requestData.getDestinationFloor(), 5);
        assertTrue(requestData.hasError());
        assertEquals(requestData.getError(), 1);
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        RequestData requestData = RequestData.of(VALID_REQUEST_LINE);

        String expectedString = "12:34:56.789 1 UP 5";
        assertEquals(requestData.toString(), expectedString);

        requestData = RequestData.of(REQUEST_LINE_WITH_ERROR);
        expectedString = "12:34:56.789 1 UP 5 1";
        assertEquals(requestData.toString(), expectedString);
    }

    /**
     * Test the equals method.
     */
    @Test
    public void testEquals() {
        RequestData requestData1 = RequestData.of(VALID_REQUEST_LINE);
        RequestData requestData2 = RequestData.of(VALID_REQUEST_LINE);
        assertEquals(requestData1, requestData2);

        RequestData requestData3 = RequestData.of(REQUEST_LINE_WITH_ERROR);
        assertFalse(requestData1.equals(requestData3));
    }

    /**
     * Test the hashCode method.
     */
    @Test
    public void testHashCode() {
        RequestData requestData1 = RequestData.of(VALID_REQUEST_LINE);
        RequestData requestData2 = RequestData.of(VALID_REQUEST_LINE);
        assertEquals(requestData1.hashCode(), requestData2.hashCode());

        RequestData requestData3 = RequestData.of(REQUEST_LINE_WITH_ERROR);
        assertNotEquals(requestData1.hashCode(), requestData3.hashCode());
    }
}
