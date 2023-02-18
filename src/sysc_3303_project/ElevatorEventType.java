/**
 * 
 */
package sysc_3303_project;

/**
 * @author apope
 *
 */
public enum ElevatorEventType {
	CLOSE_DOORS, //no payload
	OPEN_DOORS, //no payload
	START_MOVING_IN_DIRECTION, //Direction payload
	STOP_AT_NEXT_FLOOR, //no payload
	CONTINUE_MOVING //no payload
}
