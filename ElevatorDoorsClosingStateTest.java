/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package sysc_3303_project.elevator_subsystem;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosedState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosingState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorDoorsClosingStateTest extends ElevatorStateTest{

    /**
     * Tests reaction when the valid event "closeDoorsTimer" is triggered
     */
    @Override
    @Test
    public void testCloseDoorsTimer() {
        EventBuffer<Enum<?>> schedulerBuffer = new EventBuffer<>();
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();

        Elevator testContext = new Elevator(schedulerBuffer, contextBuffer, 0);

        ElevatorState testState = new ElevatorDoorsClosingState(testContext);

        ElevatorState newState = testState.closeDoorsTimer();

        Event<Enum<?>> testEvent = testContext.getOutputBuffer().getEvent();

        assertEquals(SchedulerEventType.ELEVATOR_DOORS_CLOSED, testEvent.getEventType());
        assertEquals(0, testEvent.getPayload());
        assertTrue(newState instanceof ElevatorDoorsClosedState);
        
        assertFalse(testContext.getDoor().getIsOpen());
    }

    /**
     * Tests on entry for state
     */
    @Test
    public void testOnEntry() {
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();
        Elevator context = new Elevator(null, contextBuffer, 0);
        ElevatorState testState = new ElevatorDoorsClosingState(context);

        testState.doEntry();

        Event<ElevatorEventType> newEvent = contextBuffer.getEvent();

        assertEquals(ElevatorEventType.CLOSE_DOORS_TIMER, newEvent.getEventType());
        assertNull(newEvent.getPayload());
    }
    /**
     * Tests the handleElevatorButtonPressed method of the ElevatorDoorsClosingState class
     * When elevator doors are closing and an elevator button is pressed, the event should be sent to the scheduler.
     * The state of the elevator should not change.
     */
    @Test
    public void testHandleElevatorButtonPressed() {
        EventBuffer<Enum<?>> schedulerBuffer = new EventBuffer<>();
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();

        Elevator testContext = new Elevator(schedulerBuffer, contextBuffer, 0);
        testContext.setFloor(2);

        ElevatorState testState = new ElevatorDoorsClosingState(testContext);

        ElevatorState newState = testState.handleElevatorButtonPressed(5);

        Event<Enum<?>> testEvent = testContext.getOutputBuffer().getEvent();

        assertEquals(SchedulerEventType.ELEVATOR_LAMP_STATUS_CHANGE, testEvent.getEventType());
        assertEquals(5, testEvent.getPayload());
        assertTrue(newState == null);
    }

    /**
     * Tests reaction when the elevator doors are blocked.
     * The blocked doors counter should be incremented by one, and the state should remain unchanged.
     */
    @Test
    public void testHandleDoorsBlocked() {
        EventBuffer<Enum<?>> schedulerBuffer = new EventBuffer<>();
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();

        Elevator testContext = new Elevator(schedulerBuffer, contextBuffer, 0);

        ElevatorState testState = new ElevatorDoorsClosingState(testContext);

        ElevatorState newState = testState.handleDoorsBlocked();

        assertEquals(1, testContext.getBlockedDoorsCounter());
        assertTrue(newState == null);
    }
    
    @Test
    public void testHandleDoorsBlockedDetected() {
        EventBuffer<Enum<?>> schedulerBuffer = new EventBuffer<>();
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();

        Elevator testContext = new Elevator(schedulerBuffer, contextBuffer, 0);

        ElevatorState testState = new ElevatorDoorsClosingState(testContext);

        ElevatorState newState = testState.handleDoorsBlockedDetected();

        assertTrue(newState == null);
    }
    
    @Override
	protected ElevatorState getState(Elevator context) {
		return new ElevatorDoorsClosingState(context);
	}
}
