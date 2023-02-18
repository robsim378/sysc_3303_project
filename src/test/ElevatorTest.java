
package test;

import org.junit.Test;

import sysc_3303_project.Direction;
import sysc_3303_project.ElevatorSubsystem.Elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.*;

/**
 *	@author Khalid Merai, Andrei Popescu
 *	This class includes tests for the Elevator class.
 * 	Due to the visibility restrictions on Elevator's methods/fields,
 * 	reflection is used to access them.
 */
public class ElevatorTest {
	
	/**
	 * Tests that the moveElevator method properly updates the elevator floor.
	 */
    @Test
    public void testMoveElevator() {
        Elevator elevator = new Elevator(null, 1);
        Method moveElevator;
		try {
			moveElevator = Elevator.class.getDeclaredMethod("moveElevator", int.class);
	        moveElevator.setAccessible(true);
	        moveElevator.invoke(elevator, 5);
	        Field elevatorFloor = Elevator.class.getDeclaredField("elevatorFloor");
	        elevatorFloor.setAccessible(true);
	        assertEquals(5, elevatorFloor.get(elevator));
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			fail("Exception using reflection");
		}
    }
    
    /**
     * Tests that the elevator ID is correctly set on object creation.
     */
    @Test
    public void testElevatorID() {
        Elevator elevator = new Elevator(null, 1);
		try {
	        Field elevatorID = Elevator.class.getDeclaredField("elevatorID");
	        elevatorID.setAccessible(true);
	        assertEquals(1, elevatorID.get(elevator));
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			fail("Exception using reflection");
		}
    }
    
    /**
     * Tests that the elevator direction is correctly set and retrieved.
     */
    @Test
    public void testDirection() {
        Elevator elevator = new Elevator(null, 1);
		try {
	        Field elevatorDirection = Elevator.class.getDeclaredField("direction");
	        elevatorDirection.setAccessible(true);
	        elevatorDirection.set(elevator, Direction.UP);
	        assertEquals(Direction.UP, elevatorDirection.get(elevator));
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			fail("Exception using reflection");
		}
    }
}

 
