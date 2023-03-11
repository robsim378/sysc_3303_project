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
    
}
