package sysc_3303_project.ui_subsystem;

import javax.swing.*;
import java.awt.*;

public class ElevatorPanel extends JPanel {

    private int elevatorID;
    private JLabel[] lamps;
    private JLabel direction;
    private JLabel position;

    public ElevatorPanel(int elevatorID) {
        this.setMinimumSize(new Dimension(200, 200));
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(100, 200, 50));
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
