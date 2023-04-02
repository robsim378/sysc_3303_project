package sysc_3303_project.ui_subsystem.view;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.ui_subsystem.FloorLampStatus;
import sysc_3303_project.ui_subsystem.GuiModel;

import javax.swing.*;

import logging.Logger;

import java.awt.*;
import java.util.ArrayList;

public class SystemFrame extends JFrame implements GuiView {
	
	public static final Color ON = new Color(0, 255, 0); 
	public static final Color OFF = Color.darkGray; 


    private final GuiModel model;
    private ArrayList<ElevatorPanel> elevatorPanels;
    private ArrayList<FloorPanel> floorPanels;

    public SystemFrame(GuiModel model) {
    	this.model = model;
    	model.addView(this);
        elevatorPanels = new ArrayList<>();
        floorPanels = new ArrayList<>();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setMinimumSize(new Dimension(900, 800));
        this.setLayout(new GridLayout());
        
        generateElevatorPanels();
        generateFloorPanels();

        
        // Floors side
        
        JPanel floorsSection = new JPanel();
        floorsSection.setLayout(new BorderLayout());
        floorsSection.add(new JLabel("Floors"), BorderLayout.NORTH);
        
        JPanel floorsPanel = new JPanel();
        for (int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
            floorsPanel.add(floorPanels.get(i));
            updateFloorPanel(i);
        }
        floorsPanel.setBackground(new Color(100, 100, 20));
        floorsPanel.setLayout(new GridLayout(22,1));
        
        JScrollPane scrollPaneFloors = new JScrollPane(floorsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        floorsSection.add(scrollPaneFloors, BorderLayout.CENTER);
        
        this.add(floorsSection);

        
        
        // Elevators side
        JPanel elevatorsSection = new JPanel();
        elevatorsSection.setLayout(new BorderLayout());
        elevatorsSection.add(new JLabel("Elevators"), BorderLayout.NORTH);
        
        JPanel elevatorsPanel = new JPanel();
        elevatorsPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        for (int i = 0; i < ResourceManager.get().getInt("count.elevators"); i++) {
            c.fill=GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.gridx=i%2;
            c.gridy=i/2;
            elevatorsPanel.add(elevatorPanels.get(i), c);
            updateElevatorPanel(i);
        }
        
        JScrollPane scrollPaneElevators = new JScrollPane(elevatorsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        elevatorsSection.add(scrollPaneElevators, BorderLayout.CENTER);
        
        this.add(elevatorsSection);

        setVisible(true);
    }

    private void generateElevatorPanels() {
        for (int i = 0; i < ResourceManager.get().getInt("count.elevators"); i++) {
            elevatorPanels.add(new ElevatorPanel(i));
        }
    }

    private void generateFloorPanels() {
        for (int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
            floorPanels.add(new FloorPanel(i));
        }
    }
    public void updateFloorPanel(int floorID) {
        floorPanels.get(floorID).updatePanel(model);
    }
    
    public void updateElevatorDirectionalLamps(int elevatorID) {
    	for(FloorPanel p : floorPanels) {
    		p.updateDirectionalLamp(elevatorID, model);
    	}
    }

    public void updateElevatorPanel(int elevatorID) {
        elevatorPanels.get(elevatorID).updatePanel(model);
    }


    public static void main(String[] args) {
    	System.out.println("Doing shit");

    	GuiModel model = new GuiModel(null, null);
        new SystemFrame(model);
    	System.out.println("Doing shit");

        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("Doing shit");

        model.handleFloorLampStatusChange(1, new FloorLampStatus(Direction.UP, true));

        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("Doing shit");

        model.handleDirectionalLampStatusChange(1, Direction.UP);

    }
}
