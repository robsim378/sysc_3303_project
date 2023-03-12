/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.elevator_subsystem;

/**
 * Elevator motor.
 *
 * @author Ian Holmes
 */
public class Motor {

    private boolean isRunning;

    /**
     * Constructor for the elevator motor.
     */
    public Motor() {
        isRunning = false;
    }

    /**
     * Turn on the motor.
     */
    public void turnOn() {
        isRunning = true;
    }

    /**
     * Turn on the motor.
     */
    public void turnOff() {
        isRunning = false;
    }
}
