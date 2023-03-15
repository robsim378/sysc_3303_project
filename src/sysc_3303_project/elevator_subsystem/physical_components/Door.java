/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.elevator_subsystem.physical_components;

/**
 * Elevator Door class.
 *
 * @author Ian Holmes
 */
public class Door {

    private boolean isOpen;

    /**
     * Constructor for Door.
     */
    public Door() {
        isOpen = false;
    }

    /**
     * Open the door.
     */
    public void setOpen() {
        isOpen = true;
    }

    /**
     * Close the door.
     */
    public void setClosed() {
        isOpen = false;
    }

    /**
     * Get if door is open.
     *
     * @return boolean, true if door is open
     */
    public boolean getIsOpen() {
        return isOpen;
    }
}
