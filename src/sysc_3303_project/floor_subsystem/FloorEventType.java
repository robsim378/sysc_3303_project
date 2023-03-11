/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.floor_subsystem;

/**
 * @author Andrei Popescu, Liam Gaudet
 * 
 * Potential events for the floor system
 *
 */
public enum FloorEventType {
	BUTTON_PRESSED, // Payload: RequestData. This is only sent from the input file.
	PASSENGERS_LOADED, // direction payload for lamp
}
