/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package test.gui_subsystem.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.gui_subsystem.GuiContext;
import sysc_3303_project.gui_subsystem.view.ElevatorPanel;

/**
 * Testing this
 * @author Ian
 */
public class ElevatorPanelTest {

	/**
	 * Tests the ability to update elevator panels properly
	 */
	@Test
	public void testUpdateElevatorPanel() {
		GuiContext context = new GuiContext(null);
		context.handleElevatorAtFloor(0, 1);
		
		ElevatorPanel test = new ElevatorPanel(0);
		test.updatePanel(context.getModel());

		assertEquals(ResourceManager.get().get("elevatorframe.floor") + "1", test.getFloorText());
		context.handleElevatorAtFloor(0, 2);

		test.updatePanel(context.getModel());
		assertEquals(ResourceManager.get().get("elevatorframe.floor") + "2", test.getFloorText());
	}

}
