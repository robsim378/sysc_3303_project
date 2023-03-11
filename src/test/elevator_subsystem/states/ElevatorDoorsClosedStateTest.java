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
public class ElevatorDoorsClosedStateTest extends ElevatorStateTest{


    /**
     * Tests reaction when the valid event "openDoors" is triggered
     */
    @Override
    @Test
    public void testOpenDoorsEvent() {
        ElevatorState testState = new ElevatorDoorsClosedState(null);

        ElevatorState newState = testState.openDoors();

        assertTrue(newState instanceof ElevatorDoorsOpeningState);
    }

    /**
     * Tests reaction when the valid event "setDirection" is triggered
     */
    @Override
    @Test
    public void testSetDirectionEvent() {
        Elevator testContext = new Elevator(null, null, 0);

        ElevatorState testState = new ElevatorDoorsClosedState(testContext);

        ElevatorState newState = testState.setDirection(Direction.UP);

        assertEquals(Direction.UP, testContext.getDirection());
        assertTrue(newState instanceof ElevatorMovingState);
    }
}