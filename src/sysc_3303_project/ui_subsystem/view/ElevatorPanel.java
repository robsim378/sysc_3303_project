package sysc_3303_project.ui_subsystem.view;

import javax.swing.*;
import javax.swing.border.Border;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.ui_subsystem.GuiModel;

import java.awt.*;

public class ElevatorPanel extends JPanel {
	
	private static final int LAMP_COLUMNS = 5;
	private static final int ARROW_SIZE = 20;

    private int elevatorID;
    private JPanel[] lamps;
    private JLabel position;
    private JPanel directionUpIcon;
    private JPanel directionDownIcon;
    private JLabel motorLabel;
    private JLabel faultsLabel;
    private JLabel doorStatus;

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
        this.add(new JLabel("Elevator " + (elevatorID+1)), BorderLayout.NORTH);
        
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
        	JLabel lampLabel = new JLabel((i==0? "B" : ""+i));
        	JPanel lampPanel = new JPanel();
        	lampPanel.setBackground(SystemFrame.OFF);
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

    public void updatePanel(GuiModel model) {
    	
    	doorStatus.setText("Door Status: " + model.getElevatorDoorStatus(elevatorID).toString());

    	boolean isShutdown = model.isElevatorShutdown(elevatorID);
    	boolean doorsFault = model.hasElevatorDoorsFault(elevatorID);
    	String faultsLabelString = "";
    	if(isShutdown) {
    		faultsLabelString = "ELEVATOR DISABLED";
    	} else if(doorsFault) {
    		faultsLabelString = "ELEVATOR DOOR BLOCKED";
    	}
    	faultsLabel.setText("Door Status: " + faultsLabelString);
    	
    	motorLabel.setText("Motor: " + model.getElevatorDirection(elevatorID));

    	directionUpIcon.setBackground(model.getElevatorDirection(elevatorID) == Direction.UP ? SystemFrame.ON : SystemFrame.OFF);
    	directionDownIcon.setBackground(model.getElevatorDirection(elevatorID) == Direction.DOWN ? SystemFrame.ON : SystemFrame.OFF);

    	
        position.setText("Floor: " + model.getElevatorPosition(elevatorID));
        boolean[] lampStatus = model.getElevatorButtonLamps(elevatorID);
        for(int i = 0; i < lamps.length; i++) {
        	lamps[i].setBackground(lampStatus[i] ? SystemFrame.ON : SystemFrame.OFF);
        }
        this.updateUI();
    }
}
