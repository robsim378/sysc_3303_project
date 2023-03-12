/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.elevator_subsystem;

/**
 * Elevator Door class.
 *
 * @author Ian Holmes
 */
public class Door {

    private boolean isOpen;

    public Door() {
        isOpen = false;
    }

    public void setOpen() {
        isOpen = true;
    }

    public void setClosed() {
        isOpen = false;
    }

    public boolean getIsOpen() {
        return isOpen;
    }
}
