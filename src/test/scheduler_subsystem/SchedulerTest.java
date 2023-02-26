/**
 * SYSC3303 Project
 * Group 1
 * @author khalid merai 101159203
 * @version 1.0
 */

package test.scheduler_subsystem;


import org.junit.Before;
import org.junit.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for Scheduler
 */

public class SchedulerTest {
    private Scheduler scheduler;
    private EventBuffer<SchedulerEventType> schedulerBuffer;
    private EventBuffer<ElevatorEventType> elevatorBuffer;

    /**
     * Initializes the scheduler used for testing.
     */

    @Before
    public void setUp() {
    	elevatorBuffer = new EventBuffer<ElevatorEventType>();
        scheduler = new Scheduler(elevatorBuffer, new EventBuffer<FloorEventType>());
        schedulerBuffer = scheduler.getEventBuffer();
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

    @Test
    public void testStateMachine() throws Exception {
    	Thread schedulerThread = new Thread(scheduler);
    	schedulerThread.start();
    	
    	RequestData requestData = new RequestData(LocalTime.NOON, 1, Direction.UP, 3);
    	Elevator dummyElevator = new Elevator(new EventBuffer<>(), new EventBuffer<>(), 0);
    	Field elevatorFloorField = dummyElevator.getClass().getDeclaredField("elevatorFloor");
    	elevatorFloorField.setAccessible(true);
    	elevatorFloorField.set(dummyElevator, 0);
    	//floor button is pressed in waiting state (default): expect to order elevator to close doors
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.FLOOR_BUTTON_PRESSED, this, requestData));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.CLOSE_DOORS);
    	assertTrue(scheduler.hasRequests());
    	assertTrue(scheduler.getPendingRequests().contains(requestData));
    	//elevators doors closed in processing state: expect to order elevator to move (up)
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_DOORS_CLOSED, dummyElevator, null));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.START_MOVING_IN_DIRECTION);
    	//elevators approaching target floor in processing state: expect to order elevator to stop
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_APPROACHING_FLOOR, dummyElevator, 1));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.STOP_AT_NEXT_FLOOR);
    	//elevators stopped at target floor: expect to order elevator to open doors
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_STOPPED, dummyElevator, 1));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.OPEN_DOORS);
    	elevatorFloorField.set(dummyElevator, 1);
    	//elevators doors opened: expect requests updated
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_DOORS_OPENED, dummyElevator, 1));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.CLOSE_DOORS);
    	assertFalse(scheduler.getPendingRequests().contains(requestData));
    	assertTrue(scheduler.getInProgressRequests().contains(requestData));
    	//elevators doors closed in processing state: expect to order elevator to move (up)
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_DOORS_CLOSED, dummyElevator, null));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.START_MOVING_IN_DIRECTION);
    	//elevators approaching floor in processing state: expect to order elevator to keep moving
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_APPROACHING_FLOOR, dummyElevator, 2));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.CONTINUE_MOVING);
    	elevatorFloorField.set(dummyElevator, 2);
    	//elevators approaching target floor in processing state: expect to order elevator to stop
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_APPROACHING_FLOOR, dummyElevator, 3));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.STOP_AT_NEXT_FLOOR);
    	elevatorFloorField.set(dummyElevator, 3);
    	//elevators stopped at target floor: expect to order elevator to open doors
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_STOPPED, dummyElevator, 1));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertEquals(elevatorBuffer.getEvent().getEventType(), ElevatorEventType.OPEN_DOORS);
    	//elevators doors opened: expect requests updated
    	schedulerBuffer.addEvent(new Event<>(SchedulerEventType.ELEVATOR_DOORS_OPENED, dummyElevator, 1));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertFalse(scheduler.hasRequests());
    }
}
