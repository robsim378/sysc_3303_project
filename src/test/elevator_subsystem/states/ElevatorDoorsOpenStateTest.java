/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package test.elevator_subsystem.states;

import org.junit.jupiter.api.Test;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosingState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpenState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorDoorsOpenStateTest {
	
	/**
	 * Tests reaction when the valid event "CloseDoors" is triggered
	 */
    @Test
    public void testCloseDoors() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);
        
        ElevatorState newState = testState.closeDoors();
        
        assertTrue(newState instanceof ElevatorDoorsClosingState);

    }
    
	/**
	 * Tests reaction when the invalid event "openDoors" is triggered
	 */
    @Test
    public void testOpenDoors() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoors);

        String expectedMessage = "openDoors() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "setDirection" is triggered
	 */
    @Test
    public void testSetDirection() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

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
    public void testStopAtNextFloor() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::stopAtNextFloor);

        String expectedMessage = "stopAtNextFloor() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "continueMoving" is triggered
	 */
    @Test
    public void testContinueMoving() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::continueMoving);

        String expectedMessage = "continueMoving() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "openDoorsTimer" is triggered
	 */
    @Test
    public void testOpenDoorsTimer() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoorsTimer);

        String expectedMessage = "openDoorsTimer() must be called from the ElevatorDoorsOpeningState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "travelThroughFloorsTimer" is triggered
	 */
    @Test
    public void testTravelThroughFloorTimer() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::travelThroughFloorsTimer);

        String expectedMessage = "travelThroughFloorsTimer() must be called from the ElevatorMovingState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "closeDoorsTimer" is triggered
	 */
    @Test
    public void testCloseDoorsTimer() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoorsTimer);

        String expectedMessage = "closeDoorsTimer() must be called from the ElevatorDoorsClosingState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
    
}
