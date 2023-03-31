package sysc_3303_project.ui_subsystem.view;

import javax.swing.*;
import javax.swing.border.Border;

import sysc_3303_project.ui_subsystem.GuiModel;

import java.awt.*;

public class ElevatorPanel extends JPanel {

    private int elevatorID;
    private JLabel[] lamps;
    private JLabel direction;
    private JLabel position;

    public ElevatorPanel(int elevatorID) {
        this.setMinimumSize(new Dimension(200, 200));
        this.setLayout(new GridLayout());
        this.setBackground(new Color(100, 200, 50));
        Border blackline = BorderFactory.createLineBorder(Color.black);
        setBorder(blackline);

        
        this.direction = new JLabel("Direction: ");
        this.position = new JLabel("Floor: 0");
        
        this.add(position);
        this.add(direction);
    }

    public void updatePanel(GuiModel model) {

        position.setText("Floor: " + model.getElevatorPosition(elevatorID));
        direction.setText("Direction: " + model.getElevatorDirection(elevatorID));
        this.updateUI();
    }
}
