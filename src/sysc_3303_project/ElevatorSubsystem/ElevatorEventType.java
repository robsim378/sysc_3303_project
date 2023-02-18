/**
 * 
 */
package sysc_3303_project.ElevatorSubsystem;

/**
 * @author apope
 *
 */
public enum ElevatorEventType {
	CLOSE_DOORS, //no payload
	CLOSE_DOORS_TIMER,
	OPEN_DOORS, //no payload
	OPEN_DOORS_TIMER,
	START_MOVING_IN_DIRECTION, //Direction payload
	STOP_AT_NEXT_FLOOR, //no payload
	CONTINUE_MOVING, //no payload
	MOVING_TIMER

}
