/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package test.elevator_subsystem.states;

import org.junit.jupiter.api.Test;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
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
        assertNull(testEvent.getPayload());
        assertTrue(newState instanceof ElevatorDoorsClosedState);
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
}
