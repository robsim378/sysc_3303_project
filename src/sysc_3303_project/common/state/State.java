/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.common.state;

/**
 * @author Andrei Popescu
 *
 * Interface for all states
 */
public interface State {
	
	/**
	 * Entry procedure to do when entering the state
	 */
	public void doEntry();
	
	/**
	 * Exit procedure to do when exiting the state
	 */
	public void doExit();
}
