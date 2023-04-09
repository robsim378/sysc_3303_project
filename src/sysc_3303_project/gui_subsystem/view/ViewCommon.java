/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package sysc_3303_project.gui_subsystem.view;

import java.awt.Color;

/**
 * Common information for view data
 * @author Liam, Ian
 *
 */
public class ViewCommon {

	/**
	 * Static colouring for lamps being on or off
	 */
	public static final Color ON = new Color(0, 255, 0); 
	public static final Color OFF = Color.darkGray; 
	
    /**
     * Static method for transforming an elevator ID into a user friendly format
     * @param elevatorID	int, Elevator ID being stringified
     * @return				String, stringified elevator ID
     */
	public static String elevatorIDToString(int elevatorID) {
    	return "" + (elevatorID + 1);
    }

	/**
	 * Static method for transforming a floor ID into a user friendly format
	 * Does not handle any floor that is below 0
	 * @param floorID		int, Floor ID being stringified
	 * @return				String, stringified floor ID
	 */
	public static String floorIDToString(int floorID) {
		return (floorID==0? "B" : ""+floorID);
	}

}
