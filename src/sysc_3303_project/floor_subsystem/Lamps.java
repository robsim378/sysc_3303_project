/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

import sysc_3303_project.common.Direction;


package sysc_3303_project.floor_subsystem;

static final boolean ON = true;
static final boolean OFF = false;

/**
 * @author Robert Simionescu
 * Represents all the lamps on the current floor and their current status.
 */
public class Lamps {
    /**
     * Lamp indicating that an elevator going up has arrived
     */
    private boolean upLamp = OFF;
    /**
     * Lamp indicating that an elevator going down has arrived
     */
    private boolean downLamp = OFF;

    /**
     * Lights the directional lamp for the given direction
     * @param direction     Direction, the directional lamp to light.
     */
    public lightLamp(Direction direction) {
        if (direction == UP) {
            upLamp = ON;
        }
        else {
            downLamp = ON;
        }
    }

    /**
     * Turns off all lamps.
     */
    public clearLamps() {
        upLamp = OFF;
        downLamp = OFF;
    }
}