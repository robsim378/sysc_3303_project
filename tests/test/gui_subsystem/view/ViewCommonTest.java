/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package test.gui_subsystem.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import sysc_3303_project.gui_subsystem.view.ViewCommon;

/**
 * Tests the common information for view data
 * @author Liam
 *
 */
public class ViewCommonTest {
	
	/**
	 * Tests the floor ID to string conversion
	 */
	@Test
	public void testFloorIDToString() {
		//Test a non-basement floor
		assertEquals("B", ViewCommon.floorIDToString(0));
		
		//Test a non-basement floor
		assertEquals("1", ViewCommon.floorIDToString(1));
	}
	
	/**
	 * Tests the elevator ID to string conversion
	 */
	@Test
	public void testElevatorIDToString() {
		//Test a elevator
		assertEquals("1", ViewCommon.elevatorIDToString(0));
		
		//Test a elevator
		assertEquals("2", ViewCommon.elevatorIDToString(1));

	}
}
