/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package sysc_3303_project.ElevatorSubsystem;

/**
 * Event types for the Elevator.
 *
 * @author apope
 */
public enum ElevatorEventType {
	CLOSE_DOORS, //no payload
	CLOSE_DOORS_TIMER, //no payload
	OPEN_DOORS, //no payload
	OPEN_DOORS_TIMER, //no payload
	START_MOVING_IN_DIRECTION, //Direction payload
	STOP_AT_NEXT_FLOOR, //no payload
	CONTINUE_MOVING, //no payload
	MOVING_TIMER //no payload

}
