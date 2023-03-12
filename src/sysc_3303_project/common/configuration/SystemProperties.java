/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.common.configuration;

/**
 * Old system to be used for managing system information. Depracated
 * @author Liam, Andrei
 *
 */
@Deprecated
public class SystemProperties {
	
	public static final int ELEVATOR_PORT = 6000;
	
	public static final int FLOOR_PORT = 7000;
	
	public static final int SCHEDULER_PORT = 8000;
	
	public static final int MAX_FLOOR_NUMBER = 10; //expect 10 floors, numbered 0-9

	public static final int MAX_ELEVATOR_NUMBER = 2; //expect 2 elevators, numbered 0-1

}
