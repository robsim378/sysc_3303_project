/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package sysc_3303_project.gui_subsystem.view;
import javax.swing.*;
import javax.swing.border.Border;

import logging.Logger;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.gui_subsystem.model.SystemModel;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents an floor panel to display
 * @author Liam and Ian
 */
public class FloorPanel extends JPanel {
	
	/**
	 * Reference to the resource manager for building stuff
	 */
	private static final ResourceManager rm = ResourceManager.get();

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Static data for these panel
	 */
	private static final int DIRECTION_FONT_SIZE = 20;
	
	/**
	 * ID of the floor
	 */
    private final int floorID;

    /**
     * Floor button lamps
     */
    private JPanel floorButtonUp;
    private JPanel floorButtonDown;

    /**
     * Directional lamps for all elevators on this floor
     */
    private ArrayList<JPanel> directionalLampsUp;
    private ArrayList<JPanel> directionalLampsDown;

    /**
     * Constructor for the floor panel. Initializes data
     * @param floorID	int, ID of the floor and its current floor
     */
    public FloorPanel(int floorID) {
    	// Initializing basic panel data
        this.floorID = floorID;
        this.directionalLampsUp = new ArrayList<JPanel>();
        this.directionalLampsDown = new ArrayList<JPanel>();
        
        // Setting basic panel architecture
        this.setMinimumSize(new Dimension(200, 200));
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(255, 112, 112));
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackLine);
        
        // Set the label for the floor
        this.add(new JLabel(rm.get("floorframe.name") + ViewCommon.floorIDToString(floorID)), BorderLayout.NORTH);

        // All information related to adding the directional button lamps to the left side
        JPanel floorButtonsSection = new JPanel();
        floorButtonsSection.setLayout(new BorderLayout());
        floorButtonsSection.add(new JLabel(rm.get("floorframe.buttons"), SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel floorButtons = new JPanel();
        floorButtons.setLayout(new GridLayout(2, 1));

        floorButtonUp = new JPanel();
        floorButtonUp.add(new JLabel(rm.get("floorframe.up")));
        floorButtonUp.setBackground(ViewCommon.OFF);
        floorButtonUp.setBorder(blackLine);

        floorButtonDown = new JPanel();
        floorButtonDown.add(new JLabel(rm.get("floorframe.down")));
        floorButtonDown.setBackground(ViewCommon.OFF);
        floorButtonDown.setBorder(blackLine);

        floorButtons.add(floorButtonUp);
        floorButtons.add(floorButtonDown);

        floorButtonsSection.add(floorButtons, BorderLayout.CENTER);

        this.add(floorButtonsSection, BorderLayout.WEST);

        // All information related to adding teh directional buttons on the elevators for this floor
        //   to the right side
        JPanel directionalLampsSection = new JPanel();
        directionalLampsSection.setLayout(new BorderLayout());
        directionalLampsSection.add(new JLabel(rm.get("floorframe.directional"), SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel directionalLampsSubsection = new JPanel();
        directionalLampsSubsection.setLayout(new GridLayout());
        
        int elevatorSlots = ResourceManager.get().getInt("count.elevators");
        
        for(int i = 0; i < elevatorSlots; i++) {
        	JPanel subPanel = new JPanel();
        	subPanel.setLayout(new GridLayout(3, 1));
        	
        	subPanel.add(new JLabel(rm.get("floorframe.elevatortitle") + ViewCommon.elevatorIDToString(i)));
        	
            JPanel directionUpIcon = new JPanel();
            JLabel upLabel = new JLabel("⬆️");
            upLabel.setFont(new Font("Serif", Font.PLAIN, DIRECTION_FONT_SIZE));
            directionUpIcon.setBackground(ViewCommon.OFF);
            directionUpIcon.setBorder(blackLine);
            directionUpIcon.add(upLabel);


            JPanel directionDownIcon = new JPanel();
            JLabel downLabel = new JLabel("⬇️");
            downLabel.setFont(new Font("Serif", Font.PLAIN, DIRECTION_FONT_SIZE));
            directionDownIcon.setBackground(ViewCommon.OFF);
            directionDownIcon.setBorder(blackLine);
            directionDownIcon.add(downLabel);
            
            subPanel.add(directionUpIcon);
            directionalLampsUp.add(directionUpIcon);
            subPanel.add(directionDownIcon);
            directionalLampsDown.add(directionDownIcon);
            directionalLampsSubsection.add(subPanel);
        }
        directionalLampsSection.add(directionalLampsSubsection, BorderLayout.CENTER);
        this.add(directionalLampsSection);
    }

    /**
     * Updates the floor button status of the floor
     * @param model		GuiModel, the model to reference
     */
    public void updatePanel(SystemModel model) {
    	Logger.getLogger().logNotification(this.getClass().getSimpleName(), "Updating floor panel #" + floorID);
    	
    	floorButtonUp.setBackground(Boolean.TRUE.equals(model.getFloorUpLamp(floorID)) ? ViewCommon.ON : ViewCommon.OFF);
    	floorButtonDown.setBackground(Boolean.TRUE.equals(model.getFloorDownLamp(floorID)) ? ViewCommon.ON : ViewCommon.OFF);
       	this.updateUI();
    }
    
    /**
     * Updates the directional lamps of the floor. Separate to reduce execution time
     * @param elevator	int, elevator to refresh directional lamps of
     * @param model		GuiModel, backing model to use
     */
    public void updateDirectionalLamp(int elevator, SystemModel model) {
    	Logger.getLogger().logNotification(this.getClass().getSimpleName(), "Updating directional lamps on floor #" + floorID);
		directionalLampsUp.get(elevator).setBackground(model.getElevatorDirection(elevator) == Direction.UP ? ViewCommon.ON : ViewCommon.OFF);
		directionalLampsDown.get(elevator).setBackground(model.getElevatorDirection(elevator) == Direction.DOWN ? ViewCommon.ON : ViewCommon.OFF);
       	this.updateUI();
    }
}
