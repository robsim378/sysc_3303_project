package sysc_3303_project.ui_subsystem.view;
import javax.swing.*;
import javax.swing.border.Border;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.ui_subsystem.GuiModel;

import java.awt.*;
import java.util.ArrayList;

public class FloorPanel extends JPanel {
	
	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	private static final int DIRECTION_FONT_SIZE = 20;
	

    private final int floorID;

    private JPanel floorButtonUp;
    private JPanel floorButtonDown;

    private ArrayList<JPanel> directionalLampsUp;
    private ArrayList<JPanel> directionalLampsDown;

    public FloorPanel(int floorID) {
        this.floorID = floorID;
        this.directionalLampsUp = new ArrayList<JPanel>();
        this.directionalLampsDown = new ArrayList<JPanel>();
        this.setMinimumSize(new Dimension(200, 200));
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(255, 112, 112));
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackLine);
        

        this.add(new JLabel("Floor " + (this.floorID==0? "B" : this.floorID)), BorderLayout.NORTH);

        // All information related to adding the directional button lamps to the left side
        JPanel floorButtonsSection = new JPanel();
        floorButtonsSection.setLayout(new BorderLayout());
        floorButtonsSection.add(new JLabel("Floor Buttons", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel floorButtons = new JPanel();
        floorButtons.setLayout(new GridLayout(2, 1));

        floorButtonUp = new JPanel();
        floorButtonUp.add(new JLabel("UP"));
        floorButtonUp.setBackground(SystemFrame.OFF);
        floorButtonUp.setBorder(blackLine);


        floorButtonDown = new JPanel();
        floorButtonDown.add(new JLabel("DOWN"));
        floorButtonDown.setBackground(SystemFrame.OFF);
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
        	
        	subPanel.add(new JLabel("Elevator#" + (i+1)));
        	
            JPanel directionUpIcon = new JPanel();
            JLabel upLabel = new JLabel("⬆️");
            upLabel.setFont(new Font("Serif", Font.PLAIN, DIRECTION_FONT_SIZE));
            directionUpIcon.setBackground(SystemFrame.OFF);
            directionUpIcon.setBorder(blackLine);
            directionUpIcon.add(upLabel);


            JPanel directionDownIcon = new JPanel();
            JLabel downLabel = new JLabel("⬇️");
            downLabel.setFont(new Font("Serif", Font.PLAIN, DIRECTION_FONT_SIZE));
            directionDownIcon.setBackground(SystemFrame.OFF);
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
    	
    	floorButtonUp.setBackground(Boolean.TRUE.equals(model.getFloorUpLamp(floorID)) ? SystemFrame.ON : SystemFrame.OFF);

    	// Floor button down
    	floorButtonDown.setBackground(Boolean.TRUE.equals(model.getFloorDownLamp(floorID)) ? SystemFrame.ON : SystemFrame.OFF);

       	this.updateUI();
    }
    
    public void updateDirectionalLamp(int elevator, GuiModel model) {
		directionalLampsUp.get(elevator).setBackground(model.getElevatorDirection(elevator) == Direction.UP ? SystemFrame.ON : SystemFrame.OFF);
		directionalLampsDown.get(elevator).setBackground(model.getElevatorDirection(elevator) == Direction.DOWN ? SystemFrame.ON : SystemFrame.OFF);
    }
}
