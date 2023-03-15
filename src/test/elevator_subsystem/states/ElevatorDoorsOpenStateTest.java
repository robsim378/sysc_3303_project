/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package test.elevator_subsystem.states;

import org.junit.jupiter.api.Test;

import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosingState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpenState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorDoorsOpenStateTest extends ElevatorStateTest{

    /**
     * Tests reaction when the valid event "closeDoors" is triggered
     */
	@Override
    @Test
    public void testCloseDoorsEvent() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);
        
        ElevatorState newState = testState.closeDoors();
        
        assertTrue(newState instanceof ElevatorDoorsClosingState);
    }

    /**
     * Tests reaction when the valid event "candlePassengersUnloaded" is triggered
     */
    @Test
    public void testHandlePassengersUnloaded() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        ElevatorState newState = testState.handlePassengersUnloaded();

        assertNull(newState);
    }

	@Override
	protected ElevatorState getState(Elevator context) {
		return new ElevatorDoorsOpenState(context);
	}

}
