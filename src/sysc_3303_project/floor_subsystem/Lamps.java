/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.floor_subsystem;

import sysc_3303_project.common.Direction;

/**
 * @author Robert Simionescu
 * Represents all the directional lamps on the current floor and their current status.
 */
public class Lamps {
	
	private static final boolean ON = true;
	private static final boolean OFF = false;

    /**
     * Lamp indicating that an elevator going up has arrived
     */
    private boolean upDirectionalLamp = OFF;
    /**
     * Lamp indicating that an elevator going down has arrived
     */
    private boolean downDirectionalLamp = OFF;

    /**
     * Lamp indicating that an elevator going up has arrived
     */
    private boolean upButtonLamp = OFF;
    /**
     * Lamp indicating that an elevator going down has arrived
     */
    private boolean downButtonLamp = OFF;

    /**
     * Gets the status of the up directional lamp.
     * @return  Boolean, true if the up lamp is on, false if it is off.
     */
    public boolean getUpDirectionalLampStatus() {
        return upDirectionalLamp;
    }

    /**
     * Gets the status of the down directional lamp.
     * @return  Boolean, true if the down lamp is on, false if it is off.
     */
    public boolean getDownDirectionalLampStatus() {
        return downDirectionalLamp;
    }

    /**
     * Gets the status of the up button lamp.
     * @return  Boolean, true if the up lamp is on, false if it is off.
     */
    public boolean getUpButtonLampStatus() {
        return upDirectionalLamp;
    }

    /**
     * Gets the status of the down button lamp.
     * @return  Boolean, true if the down lamp is on, false if it is off.
     */
    public boolean getDownButtonLampStatus() {
        return downDirectionalLamp;
    }

    /**
     * Lights the directional lamp for the given direction
     * @param direction     Direction, the directional lamp to light.
     */
    public void lightDirectionalLamp(Direction direction) {
        if (direction == Direction.UP) {
            upDirectionalLamp = ON;
        }
        else {
            downDirectionalLamp = ON;
        }
    }

    /**
     * Lights the button lamp for the given direction
     * @param direction     Direction, the button lamp to light.
     */
    public void lightButtonLamp(Direction direction) {
        if (direction == Direction.UP) {
            upButtonLamp = ON;
        }
        else {
            downButtonLamp = ON;
        }
    }

    /**
     * Turns off all directional lamps.
     * @param direction     Direction, the directional lamp to turn off.
     */
    public void clearDirectionalLamps(Direction direction) {
        if (direction == Direction.UP) {
            upDirectionalLamp = OFF;
        }
        else {
            downDirectionalLamp = OFF;
        }    }

    /**
     * Turns off all button lamps.
     * @param direction     Direction, the button lamp to turn off.
     */
    public void clearButtonLamps(Direction direction) {
        if (direction == Direction.UP) {
            upButtonLamp = OFF;
        }
        else {
            downButtonLamp = OFF;
        }    }
}