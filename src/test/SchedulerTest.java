/**
 * SYSC3303 Project
 * Group 1
 * @author khalid merai 101159203
 * @version 1.0
 */

package test;


import org.junit.Before;
import org.junit.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.scheduler_subsystem.Scheduler;

import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Test class for Scheduler
 */

public class SchedulerTest {
    private Scheduler scheduler;

    /**
     * Initializes the scheduler used for testing.
     */

    @Before
    public void setUp() {
        scheduler = new Scheduler(new EventBuffer<ElevatorEventType>(), new EventBuffer<FloorEventType>());
    }

    /**
     * Tests that the scheduler can correctly determine if it has any pending requests.
     */

    @Test
    public void testHasRequests() {
        assertFalse(scheduler.hasRequests());

        LocalTime time = LocalTime.now();
        scheduler.addPendingRequest(new RequestData(time, 1, Direction.UP, 3));

        assertTrue(scheduler.hasRequests());
    }

    /**
     * Checks whether the scheduler can correctly determine if it has any pending responses.
     */

    @Test
    public void testHasResponses() {
        assertFalse(scheduler.hasRequests());

        LocalTime time = LocalTime.now();
        RequestData response = new RequestData(time, 1, Direction.UP, 3);
        scheduler.addPendingRequest(response);

        assertTrue(scheduler.hasRequests());
    }


    /**
     * Tests that the scheduler can correctly handle having requests added.
     */

    @Test
    public void testAddResponse() {
        assertFalse(scheduler.hasRequests());

        LocalTime time = LocalTime.now();
        RequestData response = new RequestData(time, 1, Direction.UP, 3);
        scheduler.addPendingRequest(response);

        assertTrue(scheduler.hasRequests());
        assertEquals(response, scheduler.getPendingRequests().get(0));
    }
    /**
     * Test case to check if multiple requests can be added to the scheduler.
     */
    @Test
    public void testAddMultipleRequests() {
        // Check that the scheduler initially has no pending requests.
        assertFalse(scheduler.hasRequests());

        // Add two requests with different properties to the scheduler.
        LocalTime time1 = LocalTime.now();
        RequestData request1 = new RequestData(time1, 1, Direction.UP, 3);
        scheduler.addPendingRequest(request1);

        LocalTime time2 = LocalTime.now();
        RequestData request2 = new RequestData(time2, 2, Direction.DOWN, 1);
        scheduler.addPendingRequest(request2);

        // Check that the scheduler now has the two requests in its pending list and in the correct order.
        assertTrue(scheduler.hasRequests());
        assertEquals(request1, scheduler.getPendingRequests().get(0));
        assertEquals(request2, scheduler.getPendingRequests().get(1));
    }

    /**
     * Test case to check if multiple responses can be added to the scheduler.
     */
    @Test
    public void testAddMultipleResponses() {
        // Check that the scheduler initially has no pending requests.
        assertFalse(scheduler.hasRequests());

        // Add two responses with different properties to the scheduler.
        LocalTime time1 = LocalTime.now();
        RequestData response1 = new RequestData(time1, 1, Direction.UP, 3);
        scheduler.addPendingRequest(response1);

        LocalTime time2 = LocalTime.now();
        RequestData response2 = new RequestData(time2, 2, Direction.DOWN, 1);
        scheduler.addPendingRequest(response2);

        // Check that the scheduler now has the two responses in its pending list and in the correct order.
        assertTrue(scheduler.hasRequests());
        assertEquals(response1, scheduler.getPendingRequests().get(0));
        assertEquals(response2, scheduler.getPendingRequests().get(1));
    }


}
