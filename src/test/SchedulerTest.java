/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package test;


import static org.junit.Assert.*;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import sysc_3303_project.Direction;
import sysc_3303_project.RequestData;
import sysc_3303_project.Scheduler;

public class SchedulerTest {

    private Scheduler scheduler;

    @Before
    public void setUp() {
        scheduler = new Scheduler();
    }

    @Test
    public void testHasRequests() {
        assertFalse(scheduler.hasRequests());

        LocalTime time = LocalTime.now();
        scheduler.addRequest(new RequestData(time, 1, Direction.UP, 3));

        assertTrue(scheduler.hasRequests());
    }

    @Test
    public void testAddRequest() {
        assertFalse(scheduler.hasRequests());

        LocalTime time = LocalTime.now();
        RequestData request = new RequestData(time, 1, Direction.UP, 3);
        scheduler.addRequest(request);

        assertTrue(scheduler.hasRequests());
        assertEquals(request, scheduler.getRequest());
    }

    @Test
    public void testHasResponses() {
        assertFalse(scheduler.hasResponses());

        LocalTime time = LocalTime.now();
        RequestData response = new RequestData(time, 1, Direction.UP, 3);
        scheduler.addResponse(response);

        assertTrue(scheduler.hasResponses());
    }

    @Test
    public void testAddResponse() {
        assertFalse(scheduler.hasResponses());

        LocalTime time = LocalTime.now();
        RequestData response = new RequestData(time, 1, Direction.UP, 3);
        scheduler.addResponse(response);

        assertTrue(scheduler.hasResponses());
        assertEquals(response, scheduler.getResponse());
    }
}