package test.gui_subsystem;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.gui_subsystem.model.FloorInformation;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.gui_subsystem.model.SystemModel;
import sysc_3303_project.gui_subsystem.transfer_data.FloorLampStatus;
import sysc_3303_project.gui_subsystem.GuiContext;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.gui_subsystem.model.ElevatorInformation;
import sysc_3303_project.gui_subsystem.view.GuiView;

public class GuiContextTest {

    private GuiContext guiContext;
    private SystemModel model;
    private EventBuffer<GuiEventType> inputBuffer;
    private List<GuiView> views;

    @Before
    public void setUp() {
        // Set up input buffer and views
        inputBuffer = new EventBuffer<>();
        GuiView view = (new GuiView() {
            @Override
            public void updateFloorPanel(int floorID) {

            }

            @Override
            public void updateElevatorPanel(int elevatorID) {

            }

            @Override
            public void updateElevatorDirectionalLamps(int elevatorID) {

            }
        });

        // Create a new GuiContext with input buffer
        guiContext = new GuiContext(inputBuffer);
        guiContext.addView(view);

        // Create a new system model with a single floor and elevator
        FloorInformation[][] floors = {
                {new FloorInformation(1), new FloorInformation(2)},
                {new FloorInformation(3), new FloorInformation(4)}
        };
        ElevatorInformation elevatorInfo = new ElevatorInformation();

        model = guiContext.getModel();

    }
    @Test
    public void testHandleFloorLampStatusChange() {
        // Set the status of the up button on the floor to ON
        FloorLampStatus lampStatus = new FloorLampStatus(Direction.UP, true);
        guiContext.handleFloorLampStatusChange(0, lampStatus);

        // Ensure that the model has been updated correctly
        assertEquals(lampStatus.getStatus(), model.getFloors()[0].getUpButton());

        // Set the status of the down button on the floor to OFF
        lampStatus = new FloorLampStatus(Direction.DOWN, false);
        guiContext.handleFloorLampStatusChange(0, lampStatus);

        // Ensure that the model has been updated correctly
        assertEquals(lampStatus.getStatus(), model.getFloors()[0].getDownButton());

        // Set the status of the up button on the floor to OFF
        lampStatus = new FloorLampStatus(Direction.UP, false);
        guiContext.handleFloorLampStatusChange(0, lampStatus);

        // Ensure that the model has been updated correctly
        assertEquals(lampStatus.getStatus(), model.getFloors()[0].getUpButton());
        // Set the status of the down button on the floor to ON
        lampStatus = new FloorLampStatus(Direction.DOWN, true);
        guiContext.handleFloorLampStatusChange(0, lampStatus);

        // Ensure that the model has been updated correctly
        assertEquals(lampStatus.getStatus(), model.getFloors()[0].getDownButton());
    }

    @Test
    public void testHandleElevatorAt() {
        // Set the position of the elevator to floor 0
        guiContext.handleElevatorAtFloor(0, 0);

        // Ensure that the model has been updated correctly
        assertEquals(0, model.getElevators()[0].getPosition());

        // Move the elevator to floor 1
        guiContext.handleElevatorAtFloor(0, 1);

        // Ensure that the model has been updated correctly
        assertEquals(1, model.getElevators()[0].getPosition());

        // Move the elevator to floor 2
        guiContext.handleElevatorAtFloor(0, 2);

        // Ensure that the model has been updated correctly
        assertEquals(2, model.getElevators()[0].getPosition());

    }
    @Test
    public void testHandleMultipleElevatorsMoving() {
        // Create a system model with two elevators
        FloorInformation[][] floors = {
                {new FloorInformation(1), new FloorInformation(2)},
                {new FloorInformation(3), new FloorInformation(4)}
        };
        ElevatorInformation[] elevators = {new ElevatorInformation(), new ElevatorInformation()};

        // Set the position of the first elevator to floor 0
        guiContext.handleElevatorAtFloor(0, 0);

        // Set the position of the second elevator to floor 2
        guiContext.handleElevatorAtFloor(1, 2);

        // Ensure that the model has been updated correctly
        assertEquals(0, model.getElevators()[0].getPosition());
        assertEquals(2, model.getElevators()[1].getPosition());

        // Move both elevators to floor 1 simultaneously
        guiContext.handleElevatorAtFloor(0, 1);
        guiContext.handleElevatorAtFloor(1, 1);

        // Ensure that the model has been updated correctly
        assertEquals(1, model.getElevators()[0].getPosition());
        assertEquals(1, model.getElevators()[1].getPosition());

        // Move both elevators to floor 2 simultaneously
        guiContext.handleElevatorAtFloor(0, 2);
        guiContext.handleElevatorAtFloor(1, 2);

        // Ensure that the model has been updated correctly
        assertEquals(2, model.getElevators()[0].getPosition());
        assertEquals(2, model.getElevators()[1].getPosition());
    }
}
