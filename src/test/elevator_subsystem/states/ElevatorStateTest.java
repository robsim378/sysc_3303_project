package test.elevator_subsystem.states;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.elevator_subsystem.states.*;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class ElevatorStateTest {
    ElevatorState testState;

    /**
     * Tests reaction when the event "openDoors" is triggered
     */
    @Test
    public void testOpenDoorsEvent() {
        ElevatorState testState = new ElevatorMovingState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoors);

        String expectedMessage = "openDoors() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "setDirection" is triggered
     */
    @Test
    public void testSetDirectionEvent() {
        ElevatorState testState = new ElevatorMovingState(null);

        Exception e = assertThrows(IllegalStateException.class, () -> {
            testState.setDirection(null);
        });

        String expectedMessage = "setDirection() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "closeDoors" is triggered
     */
    @Test
    public void testCloseDoorsEvent() {
        testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoors);

        String expectedMessage = "closeDoors() must be called from the ElevatorDoorsOpenState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "stopAtNextFloor" is triggered
     */
    @Test
    public void testStopAtNextFloorEvent() {
        testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::stopAtNextFloor);

        String expectedMessage = "stopAtNextFloor() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "continueMoving" is triggered
     */
    @Test
    public void testContinueMovingEvent() {
        testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::continueMoving);

        String expectedMessage = "continueMoving() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "openDoorsTimer" is triggered
     */
    @Test
    public void testOpenDoorsTimer() {
        testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoorsTimer);

        String expectedMessage = "openDoorsTimer() must be called from the ElevatorDoorsOpeningState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "openDoorsTimer" is triggered
     */
    @Test
    public void testCloseDoorsTimer() {
        testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoorsTimer);

        String expectedMessage = "closeDoorsTimer() must be called from the ElevatorDoorsClosingState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "travelThroughFloorsTimer" is triggered
     */
    @Test
    public void testTravelThroughFloorsTimerEvent() {
        testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::travelThroughFloorsTimer);

        String expectedMessage = "travelThroughFloorsTimer() must be called from the ElevatorMovingState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "handlePassengersUnloaded" is triggered
     */
    @Test
    public void testHandlePassengersUnloaded() {
        testState = new ElevatorDoorsOpenState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::handlePassengersUnloaded);

        String expectedMessage = "handlePassengersUnloaded must be called from the ElevatorDoorsOpenState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the event "handleElevatorButtonPressed" is triggered
     */
    @Test
    public void testHandleElevatorButtonPressed() {
        EventBuffer<Enum<?>> schedulerBuffer = new EventBuffer<>();
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();

        Elevator testContext = new Elevator(schedulerBuffer, contextBuffer, 0);

        ElevatorState testState = new ElevatorMovingState(testContext); // could be any state

        ElevatorState newState = testState.handleElevatorButtonPressed(12);

        Event<Enum<?>> testEvent = testContext.getOutputBuffer().getEvent();

        assertEquals(SchedulerEventType.ELEVATOR_BUTTON_PRESSED, testEvent.getEventType());
        assertEquals(12, testEvent.getPayload());
        assertNull(newState);
    }
}
