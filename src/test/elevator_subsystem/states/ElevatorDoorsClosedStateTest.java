/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package test.elevator_subsystem.states;

import org.junit.jupiter.api.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosedState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpeningState;
import sysc_3303_project.elevator_subsystem.states.ElevatorMovingState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorDoorsClosedStateTest {


    /**
     * Tests reaction when the valid event "openDoors" is triggered
     */
    @Test
    public void testOpenDoorsEvent() {
        ElevatorState testState = new ElevatorDoorsClosedState(null);

        ElevatorState newState = testState.openDoors();

        assertTrue(newState instanceof ElevatorDoorsOpeningState);
    }

    /**
     * Tests reaction when the valid event "setDirection" is triggered
     */
    @Test
    public void testSetDirectionEvent() {
        Elevator testContext = new Elevator(null, null, 0);

        ElevatorState testState = new ElevatorDoorsClosedState(testContext);

        ElevatorState newState = testState.setDirection(Direction.UP);

        assertEquals(Direction.UP, testContext.getDirection());
        assertTrue(newState instanceof ElevatorMovingState);
    }

    /**
     * Tests reaction when the invalid event "closeDoors" is triggered
     */
    @Test
    public void testCloseDoorsEvent() {
        ElevatorState testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoors);

        String expectedMessage = "closeDoors() must be called from the ElevatorDoorsOpenState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "stopAtNextFloor" is triggered
     */
    @Test
    public void testStopAtNextFloorEvent() {
        ElevatorState testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::stopAtNextFloor);

        String expectedMessage = "stopAtNextFloor() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "continueMoving" is triggered
     */
    @Test
    public void testContinueMovingEvent() {
        ElevatorState testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::continueMoving);

        String expectedMessage = "continueMoving() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "openDoorsTimer" is triggered
     */
    @Test
    public void testOpenDoorsTimer() {
        ElevatorState testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoorsTimer);

        String expectedMessage = "openDoorsTimer() must be called from the ElevatorDoorsOpeningState.";

        assertEquals(expectedMessage, e.getMessage());
    }

    /**
     * Tests reaction when the invalid event "openDoorsTimer" is triggered
     */
    @Test
    public void testCloseDoorsTimer() {
        ElevatorState testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoorsTimer);

        String expectedMessage = "closeDoorsTimer() must be called from the ElevatorDoorsClosingState.";

        assertEquals(expectedMessage, e.getMessage());
    }


    /**
     * Tests reaction when the invalid event "travelThroughFloorsTimer" is triggered
     */
    @Test
    public void testTravelThroughFloorsTimerEvent() {
        ElevatorState testState = new ElevatorDoorsClosedState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::travelThroughFloorsTimer);

        String expectedMessage = "travelThroughFloorsTimer() must be called from the ElevatorMovingState.";

        assertEquals(expectedMessage, e.getMessage());
    }
}