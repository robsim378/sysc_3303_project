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
public class ElevatorDoorsClosingStateTest {

    /**
     * Tests reaction when the valid event "closeDoorsTimer" is triggered
     */
    @Test
    public void testCloseDoorsTimer() {
        EventBuffer<SchedulerEventType> schedulerBuffer = new EventBuffer<>();
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();

        Elevator testContext = new Elevator(schedulerBuffer, contextBuffer, 0);

        ElevatorState testState = new ElevatorDoorsClosingState(testContext);

        ElevatorState newState = testState.closeDoorsTimer();

        Event<SchedulerEventType> testEvent = testContext.getSchedulerBuffer().getEvent();

        assertEquals(SchedulerEventType.ELEVATOR_DOORS_CLOSED, testEvent.getEventType());
        assertNull(testEvent.getPayload());
        assertTrue(newState instanceof ElevatorDoorsClosedState);
    }

    /**
     * Tests reaction when the invalid event "closeDoors" is triggered
     */
    @Test
    public void testCloseDoorsEvent() {
        ElevatorState testState = new ElevatorDoorsClosingState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoors);

        String expectedMessage = "closeDoors() must be called from the ElevatorDoorsOpenState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "openDoors" is triggered
     */
    @Test
    public void testOpenDoorsEvent() {
        ElevatorState testState = new ElevatorDoorsClosingState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoors);

        String expectedMessage = "openDoors() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "setDirection" is triggered
     */
    @Test
    public void testSetDirectionEvent() {
        ElevatorState testState = new ElevatorDoorsClosingState(null);

        Exception e = assertThrows(IllegalStateException.class, () -> {
            testState.setDirection(null);
        });

        String expectedMessage = "setDirection() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "stopAtNextFloor" is triggered
     */
    @Test
    public void testStopAtNextFloorEvent() {
        ElevatorState testState = new ElevatorDoorsClosingState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::stopAtNextFloor);

        String expectedMessage = "stopAtNextFloor() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "continueMoving" is triggered
     */
    @Test
    public void testContinueMovingEvent() {
        ElevatorState testState = new ElevatorDoorsClosingState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::continueMoving);

        String expectedMessage = "continueMoving() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "openDoorsTimer" is triggered
     */
    @Test
    public void testOpenDoorsTimer() {
        ElevatorState testState = new ElevatorDoorsClosingState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoorsTimer);

        String expectedMessage = "openDoorsTimer() must be called from the ElevatorDoorsOpeningState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "travelThroughFloorsTimer" is triggered
     */
    @Test
    public void testTravelThroughTimeTest() {
        ElevatorState testState = new ElevatorDoorsClosingState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::travelThroughFloorsTimer);

        String expectedMessage = "travelThroughFloorsTimer() must be called from the ElevatorMovingState.";

        assertEquals(expectedMessage, e.getMessage());
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
