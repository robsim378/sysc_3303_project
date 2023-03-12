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

    public Motor() {
        isRunning = false;
    }

    public void turnOn() {
        isRunning = true;
    }

    public void turnOff() {
        isRunning = false;
    }
}
