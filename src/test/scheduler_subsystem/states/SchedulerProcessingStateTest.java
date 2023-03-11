/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package test.scheduler_subsystem.states;

import org.junit.jupiter.api.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.events.RequestData;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.scheduler_subsystem.states.SchedulerProcessingState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerWaitingState;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ian Holmes
 *
 */
public class SchedulerProcessingStateTest extends SchedulerStateTest {

    /**
     * Tests reaction when the valid event "handleElevatorDoorsClosedTest" is triggered
     */
	@Override
    @Test
    public void handleElevatorDoorsClosedTest() {
        EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();
    	
        Scheduler context = new Scheduler(null, outputBuffer);
        context.getTracker().addLoadRequest(0, 8, Direction.DOWN);
        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorDoorsClosed(0, 0);

        Event<Enum<?>> testEvent = outputBuffer.getEvent();

        assertEquals(ElevatorEventType.START_MOVING_IN_DIRECTION, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertEquals(Direction.UP, testEvent.getPayload());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleElevatorDoorsOpenTest" is triggered
     */
	@Override
    @Test
    public void handleElevatorDoorsOpenTest() {
		EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		context.getTracker().addLoadRequest(0, 8, Direction.DOWN);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorDoorsOpened(0, 0);

        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.CLOSE_DOORS, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertNull(newState); //all requests done but a load occurred so we expect more
    }
	
	/**
     * Tests reaction when the valid event "handleElevatorDoorsOpenTest" is triggered and unloads passengers
     */
	@Test
    public void handleElevatorDoorsOpenUnloadTest() {
		EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		context.getTracker().addUnloadRequest(0, 8);
		context.getTracker().addUnloadRequest(0, 8);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorDoorsOpened(0, 8);

        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.PASSENGERS_UNLOADED, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertEquals(8, testEvent.getPayload());
        testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.PASSENGERS_UNLOADED, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertEquals(8, testEvent.getPayload());
        assertTrue(newState instanceof SchedulerWaitingState); //all requests done and expect no more
    }
	
	/**
     * Tests reaction when the valid event "handleElevatorDoorsOpenTest" is triggered and loads passengers
     */
	@Test
    public void handleElevatorDoorsOpenLoadTest() {
		EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		context.getTracker().addLoadRequest(0, 8, Direction.DOWN);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorDoorsOpened(0, 8);
        
        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(FloorEventType.PASSENGERS_LOADED, testEvent.getEventType());
        assertEquals(8, testEvent.getDestinationID());
        assertNull(newState);
        testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.CLOSE_DOORS, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleElevatorStoppedTest" is triggered
     */
	@Override
    @Test
    public void handleElevatorStoppedTest() {
		EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		context.getTracker().addLoadRequest(0, 8, Direction.DOWN);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorStopped(0, 8); //floor doesn't matter here

        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.OPEN_DOORS, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertNull(newState);
    }
	
	
	@Override
	@Test
	public void handleElevatorApproachingFloorTest() {
		//Not valid anymore, three other tests to do below
	}

    /**
     * Tests reaction when the valid event "handleElevatorApproachingFloorTest" is triggered and the elevator drops someone off
     */
    @Test
    public void handleElevatorApproachingFloorDropOffTest() {
    	EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		context.getTracker().addUnloadRequest(0, 8);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorApproachingFloor(0, 8);

        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.STOP_AT_NEXT_FLOOR, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertNull(newState);

    }

    /**
     * Tests reaction when the valid event "handleElevatorApproachingFloorTest" is triggered and the elevator picks someone up
     */
    @Test
    public void handleElevatorApproachingFloorPickUpTest() {
    	EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		context.getTracker().addLoadRequest(0, 8, Direction.DOWN);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorApproachingFloor(0, 8);

        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.STOP_AT_NEXT_FLOOR, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleElevatorApproachingFloorTest" is triggered and the elevator continues
     */
    @Test
    public void handleElevatorApproachingFloorNotStoppingTest() {
    	EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		context.getTracker().addLoadRequest(0, 8, Direction.DOWN);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorApproachingFloor(0, 7);

        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.CONTINUE_MOVING, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertNull(newState);
    }

    /**
     * Tests reaction when the valid event "handleFloorButtonPressed" is triggered
     */
	@Override
    @Test
    public void handleFloorButtonPressedTest() {
		EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleFloorButtonPressed(8, Direction.DOWN);

        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.CLOSE_DOORS, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertNull(newState);
        assertTrue(context.getTracker().hasRequests(0));
    }
	
	/**
     * Tests reaction when the valid event "handleElevatorButtonPressed" is triggered
     */
	@Override
    @Test
    public void handleElevatorButtonPressedTest() {
		EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		assertFalse(context.getTracker().hasRequests(0));

        SchedulerState testState = new SchedulerProcessingState(context);

        SchedulerState newState = testState.handleElevatorButtonPressed(0, 6);

        assertNull(newState);
        assertTrue(context.getTracker().hasRequests(0));
    }
}
