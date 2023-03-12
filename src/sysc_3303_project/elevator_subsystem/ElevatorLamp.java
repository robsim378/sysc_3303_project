/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.elevator_subsystem;

/**
 * Elevator button lamp.
 *
 * @author Ian Holmes
 */
public class ElevatorLamp {

    private final int floorNumber;
    private boolean isOn;

    /**
     * Contstuctor for the elevator lamp.
     *
     * @param floorNumber int, the floor number
     */
    public ElevatorLamp(int floorNumber) {
        this.floorNumber = floorNumber;
        isOn = false;
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
     * Turn on the lamp.
     */
    public void turnOn() {
        isOn = true;
    }

    /**
     * Turn off the lamp.
     */
    public void turnOff() {
        isOn = false;
    }

    /**
     * Convert the lamp to a string.
     *
     * @return String, the string
     */
    public String toString() {
        if (isOn) {
            return "ON";
        }
        return "OFF";
    }
}
