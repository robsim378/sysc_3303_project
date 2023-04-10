/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package test.gui_subsystem.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.gui_subsystem.GuiContext;
import sysc_3303_project.gui_subsystem.transfer_data.FloorLampStatus;
import sysc_3303_project.gui_subsystem.view.SystemFrame;
import sysc_3303_project.gui_subsystem.view.ViewCommon;

/**
 * Testing for the system frame test to ensure functionality
 * @author User
 *
 */
public class SystemFrameTest {
	
	/**
	 * Tests the ability to generate panels
	 */
	@Test
	public void testGenerateElevatorPanels() {
		SystemFrame sysFrame = new SystemFrame(new GuiContext(null));
		
		sysFrame.generateElevatorPanels();
		
		for(int i = 0; i < ResourceManager.get().getInt("count.elevators"); i++) {
			assertEquals(i, sysFrame.getElevatorPanels().get(i).getID());
		}
	}

	/**
	 * Tests the ability to generate floors
	 */
	@Test
	public void testGenerateFloorPanels() {
		SystemFrame sysFrame = new SystemFrame(new GuiContext(null));
		
		sysFrame.generateFloorPanels();
		
		for(int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
			assertEquals(i, sysFrame.getFloorPanels().get(i).getID());
		}
	}
	
	/**
	 * Tests the ability to update floor panels properly
	 */
	@Test
	public void testupdateFloorPanel() {
		GuiContext context = new GuiContext(null);
		context.handleFloorLampStatusChange(0, new FloorLampStatus(Direction.UP, false));
		SystemFrame sysFrame = new SystemFrame(context);
		sysFrame.buildSystemFrame();
		sysFrame.updateFloorPanel(0);
		assertEquals(ViewCommon.OFF, sysFrame.getFloorPanels().get(0).getFloorButtonUp().getBackground());
		context.handleFloorLampStatusChange(0, new FloorLampStatus(Direction.UP, true));
		assertEquals(ViewCommon.ON, sysFrame.getFloorPanels().get(0).getFloorButtonUp().getBackground());
	}

	/**
	 * Tests the ability to update directional lamps properly
	 */
	@Test
	public void testUpdateDirectionalLamps() {
		GuiContext context = new GuiContext(null);
		context.handleDirectionalLampStatusChange(0, Direction.UP);
		
		SystemFrame sysFrame = new SystemFrame(context);
		sysFrame.buildSystemFrame();
		sysFrame.updateElevatorDirectionalLamps(0);
		assertEquals(ViewCommon.ON, sysFrame.getFloorPanels().get(0).getDirectionalLampsUp().get(0).getBackground());
		context.handleDirectionalLampStatusChange(0, null);
		sysFrame.updateElevatorDirectionalLamps(0);
		assertEquals(ViewCommon.OFF, sysFrame.getFloorPanels().get(0).getDirectionalLampsUp().get(0).getBackground());
	}

	/**
	 * Tests the ability to update elevator panels properly
	 */
	@Test
	public void testUpdateElevatorPanel() {
		GuiContext context = new GuiContext(null);
		context.handleElevatorAtFloor(0, 1);
		
		SystemFrame sysFrame = new SystemFrame(context);
		sysFrame.buildSystemFrame();
		sysFrame.updateElevatorPanel(0);
		assertEquals(ResourceManager.get().get("elevatorframe.floor") + "1", sysFrame.getElevatorPanels().get(0).getFloorText());
		context.handleElevatorAtFloor(0, 2);
		sysFrame.updateElevatorPanel(0);
		assertEquals(ResourceManager.get().get("elevatorframe.floor") + "2", sysFrame.getElevatorPanels().get(0).getFloorText());
	}

}
