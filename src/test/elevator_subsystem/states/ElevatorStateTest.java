package test.elevator_subsystem.states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosedState;
import sysc_3303_project.elevator_subsystem.states.ElevatorMovingState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;

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


}
