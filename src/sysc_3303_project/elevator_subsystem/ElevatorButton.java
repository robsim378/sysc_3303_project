/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.elevator_subsystem;

/**
 * Elevator button.
 *
 * @author Ian Holmes
 */
public class ElevatorButton {

    private final int floorNumber;
    private boolean isPressed;

    public ElevatorButton(int floorNumber) {
        this.floorNumber = floorNumber;
        isPressed = false;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void pushButton() {
        isPressed = true;
    }

    public void resetButton() {
        isPressed = false;
    }
}
