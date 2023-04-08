/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package sysc_3303_project.gui_subsystem.view;

import javax.swing.*;
import javax.swing.border.Border;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.gui_subsystem.model.SystemModel;

import java.awt.*;
/**
 * Represents an elevator panel to display
 * @author Liam and Ian
 */
public class ElevatorPanel extends JPanel {

    private JLabel floorLabel;

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Static data for these panel
     */
    private static final int LAMP_COLUMNS = 5;
    private static final int ARROW_SIZE = 20;

    /**
     * ID of the elevator
     */
    private int elevatorID;

    /**
     * Lamps array for this elevator
     */
    private JPanel[] lamps;

    /**
     * Position label of the elevator
     */
    private JLabel position;

    /**
     * Directional arrows for the elevator's movement
     */
    private JPanel directionUpIcon;
    private JPanel directionDownIcon;

    /**
     * Label for motor information. Not sure what its purpose is
     */
    private JLabel motorLabel;

    /**
     * Label for faults occuring in the system
     */
    private JLabel faultsLabel;

    /**
     * Label for the status of the door
     */
    private JLabel doorStatus;

    /**
     * Constructor for the elevator
     * @param elevatorID	int, elevator ID
     */
    public ElevatorPanel(int elevatorID) {
        // Setting ID
        this.elevatorID = elevatorID;

        // Setting panel informations
        this.setMinimumSize(new Dimension(200, 200));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.CYAN);
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackLine);


        // Setting the title of the panel
        this.add(new JLabel("Elevator " + ViewCommon.elevatorIDToString(elevatorID)), BorderLayout.NORTH);


        // The main section of the panel
        JPanel information = new JPanel();
        information.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx=0;
        c.gridy=0;

        // Displaying the position of the elevator in the panel
        this.position = new JLabel("Floor: 0");
        information.add(position, c);

        // Displaying the directional lamps of the elevator
        JPanel directionsSection = new JPanel();
        directionsSection.setLayout(new BorderLayout());

        JPanel directionIcons = new JPanel();
        directionIcons.setLayout(new GridLayout(1,2));

        JLabel directionalLampsLabel = new JLabel("Directional Lamps:");
        directionsSection.add(directionalLampsLabel, BorderLayout.NORTH);

        directionUpIcon = new JPanel();
        JLabel upLabel = new JLabel("⬆️");
        upLabel.setFont(new Font("Serif", Font.PLAIN, ARROW_SIZE));
        directionUpIcon.setBackground(Color.darkGray);
        directionUpIcon.setBorder(blackLine);
        directionUpIcon.add(upLabel);

        directionDownIcon = new JPanel();
        JLabel downLabel = new JLabel("⬇️");
        downLabel.setFont(new Font("Serif", Font.PLAIN, ARROW_SIZE));
        directionDownIcon.setBackground(Color.darkGray);
        directionDownIcon.setBorder(blackLine);
        directionDownIcon.add(downLabel);

        directionIcons.add(directionUpIcon);
        directionIcons.add(directionDownIcon);
        directionsSection.add(directionIcons, BorderLayout.CENTER);

        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx=0;
        c.gridy=1;

        information.add(directionsSection, c);

        // Displaying the motor information?
        motorLabel = new JLabel("Motor: ");
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx=0;
        c.gridy=2;
        information.add(motorLabel, c);

        // Displaying the faults information
        faultsLabel = new JLabel("Faults: ");
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx=0;
        c.gridy=3;
        information.add(faultsLabel, c);

        // Displaying the door information
        doorStatus = new JLabel("Door Status: ");
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx=0;
        c.gridy=4;
        information.add(doorStatus, c);


        // Displaying the Lamps information on los buttons
        JPanel lampsSection = new JPanel();
        lampsSection.setLayout(new BorderLayout());
        lampsSection.add(new JLabel("LOS POLLOS HERMANOS"), BorderLayout.NORTH);

        JPanel lampsSubsection = new JPanel();

        int floorCount = ResourceManager.get().getInt("count.floors");
        lampsSubsection.setLayout(new GridLayout(floorCount/LAMP_COLUMNS + 1, LAMP_COLUMNS));

        lamps = new JPanel[floorCount];
        for(int i = 0; i < floorCount; i++) {
            JLabel lampLabel = new JLabel(ViewCommon.floorIDToString(i));
            JPanel lampPanel = new JPanel();
            lampPanel.setBackground(ViewCommon.OFF);
            lampPanel.add(lampLabel);
            lamps[i] = lampPanel;
            lampsSubsection.add(lampPanel);
        }
        lampsSection.add(lampsSubsection, BorderLayout.CENTER);

        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx=0;
        c.gridy=5;
        information.add(lampsSection, c);

        this.add(information, BorderLayout.CENTER);
    }

    /**
     * Updates the entire panel
     * @param model		GuiModel, reference to the backing model
     */
    public void updatePanel(SystemModel model) {

        // Update door status tag
        doorStatus.setText("Door Status: " + model.getElevatorDoorStatus(elevatorID).toString());

        // Update faults tag
        boolean isShutdown = model.isElevatorShutdown(elevatorID);
        boolean doorsFault = model.hasElevatorDoorsFault(elevatorID);
        String faultsLabelString = "";
        if(isShutdown) {
            faultsLabelString = "DISABLED";
        } else if(doorsFault) {
            faultsLabelString = "DOOR BLOCKED";
        }
        faultsLabel.setText("Elevator Fault Status: " + faultsLabelString);

        // Update motor status tag
        motorLabel.setText("Motor: " + model.getElevatorDirection(elevatorID));

        // Update directional lamp tag
        directionUpIcon.setBackground(model.getElevatorDirection(elevatorID) == Direction.UP ? ViewCommon.ON : ViewCommon.OFF);
        directionDownIcon.setBackground(model.getElevatorDirection(elevatorID) == Direction.DOWN ? ViewCommon.ON : ViewCommon.OFF);

        // Update the current floor tag
        position.setText("Floor: " + ViewCommon.floorIDToString(model.getElevatorPosition(elevatorID)));

        // Update all floor destination button lamps
        boolean[] lampStatus = model.getElevatorButtonLamps(elevatorID);
        for(int i = 0; i < lamps.length; i++) {
            lamps[i].setBackground(lampStatus[i] ? ViewCommon.ON : ViewCommon.OFF);
        }

        // Refresh the panel on the UI end
        this.updateUI();
    }
    public JLabel getFloorLabel() {

        return floorLabel;
    }
}

