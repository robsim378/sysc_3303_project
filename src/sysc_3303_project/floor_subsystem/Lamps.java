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
    private boolean upLamp = OFF;
    /**
     * Lamp indicating that an elevator going down has arrived
     */
    private boolean downLamp = OFF;

    /**
     * Gets the status of the up lamp.
     * @return  Boolean, true if the up lamp is on, false if it is off.
     */
    public boolean getUpLampStatus() {
        return upLamp;
    }

    /**
     * Gets the status of the down lamp.
     * @return  Boolean, true if the down lamp is on, false if it is off.
     */
    public boolean getDownLampStatus() {
        return downLamp;
    }

    /**
     * Lights the directional lamp for the given direction
     * @param direction     Direction, the directional lamp to light.
     * @return 
     */
    public void lightLamp(Direction direction) {
        if (direction == Direction.UP) {
            upLamp = ON;
        }
        else {
            downLamp = ON;
        }
    }

    /**
     * Turns off all lamps.
     */
    public void clearLamps() {
        upLamp = OFF;
        downLamp = OFF;
    }
}