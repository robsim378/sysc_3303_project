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

    public ElevatorLamp(int floorNumber) {
        this.floorNumber = floorNumber;
        isOn = false;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void turnOn() {
        isOn = true;
    }

    public void turnOff() {
        isOn = false;
    }

    public String toString() {
        if (isOn) {
            return "ON";
        }
        return "OFF";
    }
}
