/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */
package sysc_3303_project.gui_subsystem.view;

import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.gui_subsystem.GuiContext;
import sysc_3303_project.gui_subsystem.model.SystemModel;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * The frame view to display information with
 * @author Liam and Ian
 */
public class SystemFrame extends JFrame implements GuiView {
	
	/**
	 * Reference to the resource manager for building stuff
	 */
	private static final ResourceManager rm = ResourceManager.get();
	
	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Backing GUI model
	 */
    private final SystemModel model;
    
    /**
     * Lists of panels being displayed
     */
    private ArrayList<ElevatorPanel> elevatorPanels;
    private ArrayList<FloorPanel> floorPanels;

    /**
     * Cosntructor for the system frame. Takes a model
     * @param model		GuiModel, backing moel for views
     */
    public SystemFrame(GuiContext model) {
    	this.model = model.getModel();
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
        floorsSection.add(new JLabel(rm.get("systemframe.floors")), BorderLayout.NORTH);
        
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
        elevatorsSection.add(new JLabel(rm.get("systemframe.elevators")), BorderLayout.NORTH);
        
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

        // Make frame visible
        setVisible(true);
    }

    /**
     * Generates all panels for the elevators
     */
    private void generateElevatorPanels() {
        for (int i = 0; i < ResourceManager.get().getInt("count.elevators"); i++) {
            elevatorPanels.add(new ElevatorPanel(i));
        }
    }

    /**
     * Generates all floor panelsl for the floors
     */
    private void generateFloorPanels() {
        for (int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
            floorPanels.add(new FloorPanel(i));
        }
    }
    
    public void updateFloorPanel(int floorID) {
        floorPanels.get(floorID).updatePanel(model);
    }
    
    public void updateElevatorDirectionalLamps(int elevatorID) {
    	updateElevatorPanel(elevatorID);
    	for(FloorPanel p : floorPanels) {
    		p.updateDirectionalLamp(elevatorID, model);
    	}
    }

    public void updateElevatorPanel(int elevatorID) {
        elevatorPanels.get(elevatorID).updatePanel(model);
    }
    	
}
