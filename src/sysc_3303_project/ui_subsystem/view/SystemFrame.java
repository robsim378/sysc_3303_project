package sysc_3303_project.ui_subsystem.view;

import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.ui_subsystem.GuiModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SystemFrame extends JFrame implements GuiView {

    private final GuiModel model;
    private ArrayList<ElevatorPanel> elevatorPanels;
    private ArrayList<FloorPanel> floorPanels;

    public SystemFrame(GuiModel model) {
    	this.model = model;
        elevatorPanels = new ArrayList<>();
        floorPanels = new ArrayList<>();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setMinimumSize(new Dimension(900, 800));
        this.setLayout(new GridLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        generateElevatorPanels();
        generateFloorPanels();

        
        // Floors side
        
        JPanel floorsSection = new JPanel();
        floorsSection.setLayout(new BorderLayout());
        floorsSection.add(new JLabel("Floors"), BorderLayout.NORTH);
        
        JPanel floorsPanel = new JPanel();
        for (int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
            floorsPanel.add(floorPanels.get(i));
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
        for (int i = 0; i < ResourceManager.get().getInt("count.elevators"); i++) {
            elevatorsPanel.add(elevatorPanels.get(i));
        }
        elevatorsPanel.setBackground(new Color(200, 50, 200));
        elevatorsPanel.setLayout(new GridLayout(2,2));
        
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

    public void updateElevatorPanel(int elevatorID) {
        elevatorPanels.get(elevatorID).updatePanel(model);
    }


    public static void main(String[] args) {
    	GuiModel model = new GuiModel(null, null);
        SystemFrame f = new SystemFrame(model);
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        model.
        
    }
}
