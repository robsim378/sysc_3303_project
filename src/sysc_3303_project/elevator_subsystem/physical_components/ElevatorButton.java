/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.elevator_subsystem.physical_components;

/**
 * Elevator button.
 *
 * @author Ian Holmes
 */
public class ElevatorButton {

    private final int floorNumber;
    private boolean isPressed;

    /**
     * Constructor for button.
     *
     * @param floorNumber int, the floor number
     */
    public ElevatorButton(int floorNumber) {
        this.floorNumber = floorNumber;
        isPressed = false;
    }

    /**
     * Get the floor number.
     *
     * @return int, the floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Push the button.
     */
    public void pushButton() {
        isPressed = true;
    }

    /**
     * Reset the button.
     */
    public void resetButton() {
        isPressed = false;
    }

    /**
     * Get if the button is pressed.
     *
     * @return boolean, true if it is pressed
     */
    public boolean isPressed() {
        return isPressed;
    }
}
