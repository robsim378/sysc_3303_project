package sysc_3303_project.ui_subsystem.view;

import javax.swing.*;
import javax.swing.border.Border;

import sysc_3303_project.ui_subsystem.GuiModel;

import java.awt.*;

public class ElevatorPanel extends JPanel {

    private int elevatorID;
    private JLabel[] lamps;
    private JLabel position;
    private JPanel directionUpIcon;
    private JPanel directionDownIcon;

    public ElevatorPanel(int elevatorID) {
        this.setMinimumSize(new Dimension(200, 200));
        this.setLayout(new GridLayout(3,1));
        this.setBackground(Color.CYAN);
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackLine);

        this.elevatorID = elevatorID;
        this.add(new JLabel("Elevator " + elevatorID));

        this.position = new JLabel("Floor: 0");

        JPanel directionIcons = new JPanel();
        directionIcons.setLayout(new GridLayout());

        directionUpIcon = new JPanel();
        JLabel upLabel = new JLabel("⬆️");
        upLabel.setFont(new Font("Serif", Font.PLAIN, 70));
        directionUpIcon.setBackground(Color.darkGray);
        directionUpIcon.setBorder(blackLine);
        directionUpIcon.add(upLabel);


        directionDownIcon = new JPanel();
        JLabel downLabel = new JLabel("⬇️");
        downLabel.setFont(new Font("Serif", Font.PLAIN, 70));
        directionDownIcon.setBackground(Color.darkGray);
        directionDownIcon.setBorder(blackLine);
        directionDownIcon.add(downLabel);
        directionIcons.add(directionUpIcon);
        directionIcons.add(directionDownIcon);

        this.add(position);
        this.add(directionIcons);
    }

    public void updatePanel(GuiModel model) {

        position.setText("Floor: " + model.getElevatorPosition(elevatorID));
        this.updateUI();
    }
}
