/**
 * 
 */
package sysc_3303_project.scheduler_subsystem;

/**
 * @author apope
 *
 */
public enum SchedulerEventType {
	ELEVATOR_DOORS_CLOSED, //no payload
	ELEVATOR_DOORS_OPENED, //no payload
	ELEVATOR_STOPPED, //floor number (int) payload
	ELEVATOR_APPROACHING_FLOOR, //floor number (int) payload
	FLOOR_BUTTON_PRESSED //RequestData payload
}
