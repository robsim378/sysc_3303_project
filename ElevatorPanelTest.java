package test.gui_subsystem;

import org.junit.jupiter.api.Test;
import sysc_3303_project.gui_subsystem.model.SystemModel;
import sysc_3303_project.gui_subsystem.view.ElevatorPanel;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorPanelTest {

    @Test
    void updatePanel() {
        // Create a valid SystemModel instance
        SystemModel model = new SystemModel();
        model.setCurrentFloor(5);
        model.setDoorStatus(true);

        // Create a new ElevatorPanel instance
        ElevatorPanel panel = new ElevatorPanel(1);

        // Call the updatePanel() method with the valid SystemModel instance
        panel.updatePanel(model);

        // Check that the panel's floor label has been updated correctly
        assertEquals("5", panel.getFloorLabel().getText(), "Panel floor label should be updated to '5'");
    }

}
