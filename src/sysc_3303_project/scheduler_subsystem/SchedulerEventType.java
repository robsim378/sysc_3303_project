/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.scheduler_subsystem;

/**
 * @author Andrei Popescu
 * The SchedulerEventType enumeration represents the different types of events that
 * a Scheduler is expected to handle.
 */
public enum SchedulerEventType {
	ELEVATOR_DOORS_CLOSED, //floor number (int) payload
	ELEVATOR_DOORS_OPENED, //floor number (int) payload
	ELEVATOR_STOPPED, //floor number (int) payload
	ELEVATOR_APPROACHING_FLOOR, //floor number (int) payload
	FLOOR_BUTTON_PRESSED, //RequestData payload
	ELEVATOR_BUTTON_PRESSED, //floor number (int) payload
	ELEVATOR_BLOCKED, //elevator ID (int) payload
	ELEVATOR_PING
}
