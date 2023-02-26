/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package test.scheduler_subsystem.states;

import org.junit.jupiter.api.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.scheduler_subsystem.states.SchedulerProcessingState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerState;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ian Holmes
 *
 */
public class SchedulerProcessingStateTest {

    /**
     * Tests reaction when the valid event "handleElevatorDoorsClosedTest" is triggered
     */
    @Test
    public void handleElevatorDoorsClosedTest() {
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();

        Scheduler context = new Scheduler(elevatorBuffer, null);
        SchedulerState testState = new SchedulerProcessingState(context);

        Elevator e = new Elevator(null,null,0);

        SchedulerState newState = testState.handleElevatorDoorsClosed(e);

        Event<ElevatorEventType> testEvent = context.getElevatorBuffer().getEvent();

        assertEquals(ElevatorEventType.START_MOVING_IN_DIRECTION, testEvent.getEventType());
        assertEquals(Direction.DOWN, testEvent.getPayload());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleElevatorDoorsOpenTest" is triggered
     */
    @Test
    public void handleElevatorDoorsOpenTest() {
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();

        Scheduler context = new Scheduler(elevatorBuffer, null);

        RequestData testRequest1 = new RequestData(LocalTime.NOON, 3, Direction.DOWN, 0);
        RequestData testRequest2 = new RequestData(LocalTime.NOON, 0, Direction.DOWN, 5);

        context.addPendingRequest(testRequest1);
        context.addPendingRequest(testRequest2);
        context.markRequestInProgress(testRequest1);

        Elevator e = new Elevator(null,null,0);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorDoorsOpened(e);

        assertFalse(context.getInProgressRequests().contains(testRequest1));
        assertTrue(context.getInProgressRequests().contains(testRequest2));
        assertNull(newState);

        Event<ElevatorEventType> testEvent = context.getElevatorBuffer().getEvent();

        assertEquals(ElevatorEventType.CLOSE_DOORS, testEvent.getEventType());
        assertNull(testEvent.getPayload());
    }

    /**
     * Tests reaction when the valid event "handleElevatorStoppedTest" is triggered
     */
    @Test
    public void handleElevatorStoppedTest() {
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();

        Scheduler context = new Scheduler(elevatorBuffer, null);
        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorStopped(null, 0);

        Event<ElevatorEventType> testEvent = context.getElevatorBuffer().getEvent();

        assertEquals(ElevatorEventType.OPEN_DOORS, testEvent.getEventType());
        assertNull(testEvent.getPayload());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleElevatorApproachingFloorTest" is triggered and the elevator drops someone off
     */
    @Test
    public void handleElevatorApproachingFloorDropOffTest() {
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();

        Scheduler context = new Scheduler(elevatorBuffer, null);

        RequestData testRequest = new RequestData(LocalTime.NOON, 3, Direction.DOWN, 0);

        context.addPendingRequest(testRequest);
        context.markRequestInProgress(testRequest);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorApproachingFloor(null, 0);

        Event<ElevatorEventType> testEvent = context.getElevatorBuffer().getEvent();

        assertEquals(ElevatorEventType.STOP_AT_NEXT_FLOOR, testEvent.getEventType());
        assertNull(testEvent.getPayload());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleElevatorApproachingFloorTest" is triggered and the elevator picks someone up
     */
    @Test
    public void handleElevatorApproachingFloorPickUpTest() {
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();

        Scheduler context = new Scheduler(elevatorBuffer, null);

        RequestData testRequest = new RequestData(LocalTime.NOON, 0, Direction.DOWN, 5);

        context.addPendingRequest(testRequest);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorApproachingFloor(null, 0);

        Event<ElevatorEventType> testEvent = context.getElevatorBuffer().getEvent();

        assertEquals(ElevatorEventType.STOP_AT_NEXT_FLOOR, testEvent.getEventType());
        assertNull(testEvent.getPayload());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleElevatorApproachingFloorTest" is triggered and the elevator continues
     */
    @Test
    public void handleElevatorApproachingFloorNotStoppingTest() {
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();

        Scheduler context = new Scheduler(elevatorBuffer, null);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorApproachingFloor(null, 0);

        Event<ElevatorEventType> testEvent = context.getElevatorBuffer().getEvent();

        assertEquals(ElevatorEventType.CONTINUE_MOVING, testEvent.getEventType());
        assertNull(testEvent.getPayload());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleFloorButtonPressed" is triggered
     */
    @Test
    public void handleFloorButtonPressed() {
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();

        Scheduler context = new Scheduler(elevatorBuffer, null);
        SchedulerState testState = new SchedulerProcessingState(context);

        RequestData testInput = new RequestData(LocalTime.NOON, 2, Direction.DOWN, 4);

        SchedulerState newState = testState.handleFloorButtonPressed(testInput);

        assertTrue(context.getPendingRequests().contains(testInput));

        assertNull(newState);
    }
}
