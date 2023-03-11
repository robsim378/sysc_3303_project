/**
 * SYSC3303 Project
 * Group 1
 * @author khalid merai 101159203 & Andrei Popescu 101143798
 * @version 3.0
 */

package test.scheduler_subsystem;


import org.junit.Before;
import org.junit.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.events.RequestData;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.scheduler_subsystem.ElevatorTracker;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
    	schedulerBuffer = new EventBuffer<>();
        scheduler = new Scheduler(schedulerBuffer, outputBuffer);
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
    }
    /**
     * Test case to check if multiple requests can be added to the scheduler.
     */
    @Test
    public void testAssignMultipleRequests() throws Exception {
    	int elevatorId = scheduler.assignLoadRequest(8, Direction.DOWN);
        //tiebreakers dictate that elevator 0 must be selected
        assertEquals(0, elevatorId);
        
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
    public void testDirectionToMove() {
    	scheduler.getTracker().updateElevatorDirection(0, Direction.UP);
    	scheduler.getTracker().addLoadRequest(0, 8, Direction.DOWN);
    	assertEquals(Direction.UP, scheduler.directionToMove(0));
    	
    	scheduler.getTracker().updateElevatorDirection(1, Direction.UP);
    	scheduler.getTracker().updateElevatorFloor(1, 5);
    	scheduler.getTracker().addUnloadRequest(1, 3);
    	assertEquals(Direction.DOWN, scheduler.directionToMove(1));
    	
    	scheduler.getTracker().unloadElevator(1, 3);
    	scheduler.getTracker().updateElevatorFloor(1, 3);
    	assertEquals(null, scheduler.directionToMove(1));
    }
    
    @Test
    public void testShouldStop() {
    	scheduler.getTracker().updateElevatorDirection(0, Direction.UP);
    	scheduler.getTracker().addLoadRequest(0, 8, Direction.DOWN);
    	scheduler.getTracker().addLoadRequest(0, 6, Direction.DOWN);
    	scheduler.getTracker().addLoadRequest(0, 4, Direction.UP);
    	assertFalse(scheduler.shouldStop(0, 0)); //no request
    	assertTrue(scheduler.shouldStop(0, 4)); //load request in correct direction
    	scheduler.getTracker().addUnloadRequest(0, 7);
    	assertFalse(scheduler.shouldStop(0, 6)); //load request in incorrect direction
    	assertTrue(scheduler.shouldStop(0, 7)); //unload request
    	assertTrue(scheduler.shouldStop(0, 8)); //load request in incorrect direction but no further requests
    	scheduler.getTracker().updateElevatorDirection(1, Direction.DOWN);
    	assertTrue(scheduler.shouldStop(1, 0)); //bottom floor
    }

    @Test
    public void testStateMachine() throws Exception {
    	Thread schedulerThread = new Thread(scheduler);
    	schedulerThread.start();
    	Field elevatorTrackerField = Scheduler.class.getDeclaredField("tracker");
        elevatorTrackerField.setAccessible(true);
        ElevatorTracker tracker = (ElevatorTracker) elevatorTrackerField.get(scheduler);
        Event<Enum<?>> evt;
        
    	schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.FLOOR, 8, SchedulerEventType.FLOOR_BUTTON_PRESSED, Direction.DOWN));
    	TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.CLOSE_DOORS, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID()); //request assigned to elevator 0
        
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_DOORS_CLOSED, 0));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.START_MOVING_IN_DIRECTION, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        assertEquals(Direction.UP, evt.getPayload()); //elevator going up
        assertEquals(Direction.UP, tracker.getElevatorDirection(0));
        
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_APPROACHING_FLOOR, 1));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.CONTINUE_MOVING, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        assertEquals(1, tracker.getElevatorFloor(0));
        
        //assign extra requests partway
        tracker.updateElevatorFloor(0, 4); //skip ahead for testing: approaching floors 2-4 will work the same as the previous test case
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.FLOOR, 5, SchedulerEventType.FLOOR_BUTTON_PRESSED, Direction.UP));
        TimeUnit.MILLISECONDS.sleep(500);
        assertFalse(tracker.hasRequests(1));
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.FLOOR, 2, SchedulerEventType.FLOOR_BUTTON_PRESSED, Direction.UP));
    	TimeUnit.MILLISECONDS.sleep(500);
    	assertTrue(tracker.hasRequests(1));
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.CLOSE_DOORS, (ElevatorEventType) evt.getEventType());
        assertEquals(1, evt.getDestinationID()); //request assigned to elevator 1; elevator 0 receives no additional events
        
    	schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_APPROACHING_FLOOR, 5));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.STOP_AT_NEXT_FLOOR, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        assertEquals(5, tracker.getElevatorFloor(0));
    	
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_STOPPED, 5));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.OPEN_DOORS, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_DOORS_OPENED, 5));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof FloorEventType);
        assertEquals(FloorEventType.PASSENGERS_LOADED, (FloorEventType) evt.getEventType());
        assertEquals(5, evt.getDestinationID());
        assertEquals(Direction.UP, (Direction) evt.getPayload());
        evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.CLOSE_DOORS, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        assertEquals(Direction.UP, tracker.getElevatorDirection(0));
        
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_BUTTON_PRESSED, 6));
        TimeUnit.MILLISECONDS.sleep(500);
        
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_DOORS_CLOSED, 5));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.START_MOVING_IN_DIRECTION, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        assertEquals(Direction.UP, evt.getPayload()); //elevator going up
        assertEquals(Direction.UP, tracker.getElevatorDirection(0));
        
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_APPROACHING_FLOOR, 6));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.STOP_AT_NEXT_FLOOR, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
    	
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_STOPPED, 6));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.OPEN_DOORS, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_DOORS_OPENED, 6));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.PASSENGERS_UNLOADED, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        assertEquals(6, (int) evt.getPayload());
        assertTrue(tracker.hasRequests(0));
        evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.CLOSE_DOORS, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        assertEquals(Direction.UP, tracker.getElevatorDirection(0));
        
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_DOORS_CLOSED, 6));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.START_MOVING_IN_DIRECTION, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
        assertEquals(Direction.UP, evt.getPayload()); //elevator going up
        assertEquals(Direction.UP, tracker.getElevatorDirection(0));
        
        tracker.updateElevatorFloor(0, 7);
        schedulerBuffer.addEvent(new Event<>(Subsystem.SCHEDULER, 0, Subsystem.ELEVATOR, 0, SchedulerEventType.ELEVATOR_APPROACHING_FLOOR, 8));
        TimeUnit.MILLISECONDS.sleep(500);
    	evt = outputBuffer.getEvent();
        assertTrue(evt.getEventType() instanceof ElevatorEventType);
        assertEquals(ElevatorEventType.STOP_AT_NEXT_FLOOR, (ElevatorEventType) evt.getEventType());
        assertEquals(0, evt.getDestinationID());
    }
}
