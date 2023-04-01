package sysc_3303_project.ui_subsystem.view;
import javax.swing.*;
import javax.swing.border.Border;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.ui_subsystem.GuiModel;

import java.awt.*;
import java.util.ArrayList;

public class FloorPanel extends JPanel {
	
	private static final int DIRECTION_FONT_SIZE = 20;
	
	private static final Color ON = new Color(0, 255, 0); 
	private static final Color OFF = Color.darkGray; 

    private final int floorID;

    private JPanel floorButtonUp;
    private JPanel floorButtonDown;

    private ArrayList<JPanel> directionalLampsUp;
    private ArrayList<JPanel> directionalLampsDown;

    public FloorPanel(int floorID) {
        this.floorID = floorID;
        this.directionalLampsUp = new ArrayList();
        this.directionalLampsDown = new ArrayList();
        this.setMinimumSize(new Dimension(200, 200));
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(255, 112, 112));
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackLine);
        

        this.add(new JLabel("Floor " + this.floorID), BorderLayout.NORTH);

        // All information related to adding the directional button lamps to the left side
        JPanel floorButtonsSection = new JPanel();
        floorButtonsSection.setLayout(new BorderLayout());
        floorButtonsSection.add(new JLabel("Floor Buttons", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel floorButtons = new JPanel();
        floorButtons.setLayout(new GridLayout(2, 1));

        floorButtonUp = new JPanel();
        floorButtonUp.add(new JLabel("UP"));
        floorButtonUp.setBackground(OFF);
        floorButtonUp.setBorder(blackLine);


        floorButtonDown = new JPanel();
        floorButtonDown.add(new JLabel("DOWN"));
        floorButtonDown.setBackground(OFF);
        floorButtonDown.setBorder(blackLine);

        
        floorButtons.add(floorButtonUp);
        floorButtons.add(floorButtonDown);

        floorButtonsSection.add(floorButtons, BorderLayout.CENTER);

        this.add(floorButtonsSection, BorderLayout.WEST);

        // All information related to adding teh directional buttons on the elevators for this floor
        //   to the right side

        JPanel directionalLampsSection = new JPanel();
        directionalLampsSection.setLayout(new BorderLayout());
        directionalLampsSection.add(new JLabel("Directional Lamps", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel directionalLampsSubsection = new JPanel();
        directionalLampsSubsection.setLayout(new GridLayout());
        
        int elevatorSlots = ResourceManager.get().getInt("count.elevators");
        
        for(int i = 0; i < elevatorSlots; i++) {
        	JPanel subPanel = new JPanel();
        	subPanel.setLayout(new GridLayout(3, 1));
        	
        	subPanel.add(new JLabel("Elevator#" + i));
        	
            JPanel directionUpIcon = new JPanel();
            JLabel upLabel = new JLabel("⬆️");
            upLabel.setFont(new Font("Serif", Font.PLAIN, DIRECTION_FONT_SIZE));
            directionUpIcon.setBackground(OFF);
            directionUpIcon.setBorder(blackLine);
            directionUpIcon.add(upLabel);


            JPanel directionDownIcon = new JPanel();
            JLabel downLabel = new JLabel("⬇️");
            downLabel.setFont(new Font("Serif", Font.PLAIN, DIRECTION_FONT_SIZE));
            directionDownIcon.setBackground(OFF);
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

    public void updatePanel(GuiModel model) {
    	System.out.println("Doing shit");

    	// Floor button up
    	
    	floorButtonUp.setBackground(Boolean.TRUE.equals(model.getFloorUpLamp(floorID)) ? ON : OFF);

    	// Floor button down
    	floorButtonDown.setBackground(Boolean.TRUE.equals(model.getFloorDownLamp(floorID)) ? ON : OFF);

    	
    	// Floor directional lamps
    	for(int i = 0; i < directionalLampsUp.size(); i++) {
    		directionalLampsUp.get(i).setBackground(model.getElevatorDirection(i) == Direction.UP ? ON : OFF);
    		directionalLampsDown.get(i).setBackground(model.getElevatorDirection(i) == Direction.DOWN ? ON : OFF);
    	}
    	System.out.println("Updated");
    	this.updateUI();
    }
    
    public void updateDirectionalLamp(int elevator, GuiModel model) {
		directionalLampsUp.get(elevator).setBackground(model.getElevatorDirection(elevator) == Direction.UP ? ON : OFF);
		directionalLampsDown.get(elevator).setBackground(model.getElevatorDirection(elevator) == Direction.DOWN ? ON : OFF);
    }
}
