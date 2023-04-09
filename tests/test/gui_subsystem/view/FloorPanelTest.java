/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package test.gui_subsystem.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.gui_subsystem.GuiContext;
import sysc_3303_project.gui_subsystem.transfer_data.FloorLampStatus;
import sysc_3303_project.gui_subsystem.view.FloorPanel;
import sysc_3303_project.gui_subsystem.view.ViewCommon;

/**
 * Testing this
 * @author Ian
 */
public class FloorPanelTest {
	/**
	 * Tests the ability to update floor panels properly
	 */
	@Test
	public void testupdateFloorPanel() {
		GuiContext context = new GuiContext(null);
		context.handleFloorLampStatusChange(0, new FloorLampStatus(Direction.UP, false));

		FloorPanel test = new FloorPanel(0);
		
		test.updatePanel(context.getModel());
		
		assertEquals(ViewCommon.OFF, test.getFloorButtonUp().getBackground());
		context.handleFloorLampStatusChange(0, new FloorLampStatus(Direction.UP, true));
		test.updatePanel(context.getModel());
		assertEquals(ViewCommon.ON, test.getFloorButtonUp().getBackground());
	}

	/**
	 * Tests the ability to update directional lamps properly
	 */
	@Test
	public void testUpdateDirectionalLamps() {		
	
		
		GuiContext context = new GuiContext(null);
		context.handleDirectionalLampStatusChange(0, Direction.UP);

		FloorPanel test = new FloorPanel(0);
		
		test.updateDirectionalLamp(0, context.getModel());
		
		assertEquals(ViewCommon.ON, test.getDirectionalLampsUp().get(0).getBackground());
		context.handleDirectionalLampStatusChange(0, null);
		test.updateDirectionalLamp(0, context.getModel());
		assertEquals(ViewCommon.OFF, test.getDirectionalLampsUp().get(0).getBackground());

	}

}
