package sysc_3303_project.ui_subsystem;

import sysc_3303_project.common.configuration.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SystemFrame extends JFrame implements GuiView {

    private GuiModel model;
    private ArrayList<ElevatorPanel> elevatorPanels;
    private ArrayList<FloorPanel> floorPanels;

    public SystemFrame() {
        this.setMinimumSize(new Dimension(500, 500));
        this.setLayout(new GridLayout());
        GridBagConstraints c = new GridBagConstraints();
        //model = new GuiModel();
        elevatorPanels = new ArrayList<>();
        floorPanels = new ArrayList<>();
        generateElevatorPanels();
        generateFloorPanels();

        JPanel floorsPanel = new JPanel();
        floorsPanel.add(new JLabel("Floors"));
        for (int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
            floorsPanel.add(floorPanels.get(i));
        }
        floorsPanel.setBackground(new Color(100, 100, 20));
        floorsPanel.setLayout(new GridLayout());
        this.add(floorsPanel);

        JPanel elevatorsPanel = new JPanel();
        elevatorsPanel.add(new JLabel("Elevators"));
        for (int i = 0; i < ResourceManager.get().getInt("count.elevators"); i++) {
            elevatorsPanel.add(elevatorPanels.get(i));
        }
        elevatorsPanel.setBackground(new Color(200, 50, 200));
        elevatorsPanel.setLayout(new GridLayout());
        this.add(elevatorsPanel);



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
        new SystemFrame();
    }
}
