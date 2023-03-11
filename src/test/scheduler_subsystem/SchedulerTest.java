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
import sysc_3303_project.common.Subsystem;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.scheduler_subsystem.ElevatorTracker;
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
    private EventBuffer<Enum<?>> outputBuffer;

    /**
     * Initializes the scheduler used for testing.
     */

    @Before
    public void setUp() {
    	outputBuffer = new EventBuffer<Enum<?>>();
        scheduler = new Scheduler(schedulerBuffer, outputBuffer);
        schedulerBuffer = scheduler.getInputBuffer();
    }

    /**
     * Tests that the scheduler can return its elevator tracker.
     */

    @Test
    public void testGetTracker() {
        assertTrue(scheduler.getTracker() instanceof ElevatorTracker);
    }


    /**
     * Tests that the scheduler can correctly assign a request to idle elevators.
     */

    @Test
    public void testAssignRequest() {

        int elevatorId = scheduler.assignLoadRequest(8, Direction.DOWN);
        //tiebreakers dictate that elevator 0 must be selected
        assertEquals(0, elevatorId);
        Event<Enum<?>> evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.CLOSE_DOORS, (ElevatorEventType) evt.getEventType());
        schedulerBuffer.addEvent(new Event<SchedulerEventType>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_DOORS_CLOSED, 0));
        evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.START_MOVING_IN_DIRECTION, (ElevatorEventType) evt.getEventType());
        assertEquals(Direction.UP, (Direction) evt.getPayload());
    }
    /**
     * Test case to check if multiple requests can be added to the scheduler.
     */
    @Test
    public void testAddMultipleRequests() throws Exception {
    	int elevatorId = scheduler.assignLoadRequest(8, Direction.DOWN);
        //tiebreakers dictate that elevator 0 must be selected
        assertEquals(0, elevatorId);
        Event<Enum<?>> evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.CLOSE_DOORS, (ElevatorEventType) evt.getEventType());
        schedulerBuffer.addEvent(new Event<SchedulerEventType>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_DOORS_CLOSED, 0));
        evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.START_MOVING_IN_DIRECTION, (ElevatorEventType) evt.getEventType());
        assertEquals(Direction.UP, (Direction) evt.getPayload());
        
        Field elevatorTrackerField = Scheduler.class.getDeclaredField("tracker");
        elevatorTrackerField.setAccessible(true);
        ElevatorTracker tracker = (ElevatorTracker) elevatorTrackerField.get(scheduler);
        tracker.updateElevatorDirection(0, Direction.UP);
        tracker.updateElevatorFloor(0, 4);
        
        elevatorId = scheduler.assignLoadRequest(6, Direction.UP);
        assertEquals(0, elevatorId);
        elevatorId = scheduler.assignLoadRequest(4, Direction.UP);
        assertEquals(1, elevatorId);
    }

    @Test
    public void testStateMachine() throws Exception {
    	Thread schedulerThread = new Thread(scheduler);
    	schedulerThread.start();
    	
    	Field elevatorTrackerField = Scheduler.class.getDeclaredField("tracker");
        elevatorTrackerField.setAccessible(true);
        ElevatorTracker tracker = (ElevatorTracker) elevatorTrackerField.get(scheduler);
        
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
