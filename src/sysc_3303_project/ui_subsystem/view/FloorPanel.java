package sysc_3303_project.ui_subsystem.view;
import javax.swing.*;

import sysc_3303_project.ui_subsystem.GuiModel;

import java.awt.*;
import java.util.ArrayList;

public class FloorPanel extends JPanel {
	
	private final int floorID;
	
	private JPanel floorButtonUp;
	private JPanel floorButtonDown;
	
	private ArrayList<JPanel> directionalLampsUp;
	private ArrayList<JPanel> directionalLampsDown;

    public FloorPanel(int floorID) {
    	this.floorID = floorID;
        this.setMinimumSize(new Dimension(200, 200));
        this.setLayout(new BorderLayout());
        
        this.add(new JLabel("Floor " + this.floorID), BorderLayout.NORTH);
        
        // All information related to adding the directional button lamps to the left side
        JPanel floorButtonsSection = new JPanel();
        floorButtonsSection.setLayout(new BorderLayout());
        floorButtonsSection.add(new JLabel("Floor Buttons"));
        
        JPanel floorButtons = new JPanel();
        floorButtons.setLayout(new GridLayout());
        
        floorButtonUp = new JPanel();
        floorButtonUp.add(new JLabel("UP"));

        floorButtonDown = new JPanel();
        floorButtonDown.add(new JLabel("DOWN"));
        floorButtons.add(floorButtonUp);
        floorButtons.add(floorButtonDown);
        
        floorButtonsSection.add(floorButtons, BorderLayout.CENTER);
        
        this.add(floorButtonsSection, BorderLayout.WEST);
        
        // All information related to adding teh directional buttons on the elevators for this floor
        //   to the right side
        
        JPanel directionalLampsSection = new JPanel();
        directionalLampsSection.setLayout(new BorderLayout());
        directionalLampsSection.add(new JLabel("Directional Buttons"), BorderLayout.NORTH);

        
        
        
        
        
        

        
    }

    public void updatePanel(GuiModel model) {
        this.updateUI();
    }
}
