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

import logging.Logger;

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
	 * Static data
	 */
	private static final int WIDTH = 900;
	private static final int HEIGHT = 800;
	
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
    	
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "System Frame Created");
    	
    	this.model = model.getModel();
    	model.addView(this);
        elevatorPanels = new ArrayList<>();
        floorPanels = new ArrayList<>();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setMinimumSize(new Dimension(900, 800));
        this.setLayout(new GridLayout());
        this.setTitle(rm.get("systemframe.title"));
    }
    
    public void buildSystemFrame() {
    	
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "Building System Frame");

        generateElevatorPanels();
        generateFloorPanels();

        // Floors side
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "Generating floors display");

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
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "Generating elevators display");

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
    }

    /**
     * Generates all panels for the elevators
     */
    public void generateElevatorPanels() {
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "Building Elevator panels");

        for (int i = 0; i < ResourceManager.get().getInt("count.elevators"); i++) {
            elevatorPanels.add(new ElevatorPanel(i));
        }
    }

    /**
     * Generates all floor panels for the floors
     */
    public void generateFloorPanels() {
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "Building Floor panels");

        for (int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
            floorPanels.add(new FloorPanel(i));
        }
    }
    
    /**
     * ------------------ Getters -------------------
     */
    
    /**
     * Getter
     * @return	ArrayList<>, elevator panels
     */
    public ArrayList<ElevatorPanel> getElevatorPanels(){
    	return elevatorPanels;
    }

    /**
     * Getter
     * @return	ArrayList<>, floor panels
     */
    public ArrayList<FloorPanel> getFloorPanels(){
    	return floorPanels;
    }

    /**
     * ------------------ Inherited ---------------------
     */
    public void updateFloorPanel(int floorID) {
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "Updating Floor panels");

        floorPanels.get(floorID).updatePanel(model);
    }
    
    public void updateElevatorDirectionalLamps(int elevatorID) {
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "Updating Directional Lamps");

    	updateElevatorPanel(elevatorID);
    	for(FloorPanel p : floorPanels) {
    		p.updateDirectionalLamp(elevatorID, model);
    	}
    }

    public void updateElevatorPanel(int elevatorID) {
    	Logger.getLogger().logDebug(SystemFrame.class.getSimpleName(), "Updating Elevator panels");

        elevatorPanels.get(elevatorID).updatePanel(model);
    }
    	
}
