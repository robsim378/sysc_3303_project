/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */
package sysc_3303_project.gui_subsystem;

/**
 * @author Andrei
 *
 */
public enum GuiEventType {
	FLOOR_LAMP_STATUS_CHANGE, //FloorLampStatus payload
	DIRECTIONAL_LAMP_STATUS_CHANGE, //direction payload
	ELEVATOR_AT_FLOOR, //int payload (floor #)
	ELEVATOR_DOORS_FAULT, //boolean payload (true = blocked)
	ELEVATOR_SHUTDOWN_FAULT, //int payload (elevator ID)
	ELEVATOR_LAMP_STATUS_CHANGE, //ElevatorLampStatus payload
	ELEVATOR_DOOR_STATUS_CHANGE //DoorStatus enum payload
}
