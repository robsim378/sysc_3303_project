/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.scheduler_subsystem.states;

import sysc_3303_project.common.RequestData;
import sysc_3303_project.common.State;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.scheduler_subsystem.Scheduler;

/**
 * @author Andrei Popescu
 * Base class for states from the Scheduler's state machine.
 * Provides entry and exit actions, as well as default handlers for
 * all requisite events (which throw exceptions) to be overridden.
 */
public abstract class SchedulerState implements State {
	
	protected Scheduler context;
	
	/**
	 * Creates a new SchedulerState
	 * @param context the Scheduler this state is tied to
	 */
	public SchedulerState(Scheduler context) {
		this.context = context;
	}
	
	/**
	 * Handles an elevator's doors closing completely.
	 * @param e the Elevator that closed its doors
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorDoorsClosed(Elevator e) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handles an elevator's doors opening completely.
	 * @param e the Elevator that opened its doors
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorDoorsOpened(Elevator e) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handles an elevator stopping at a floor.
	 * @param e the Elevator that stopped
	 * @param floor the floor that the Elevator stopped at
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorStopped(Elevator e, int floor) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handles an elevator approaching a floor.
	 * @param e the Elevator that approached a floor
	 * @param floor the floor that the Elevator is approaching
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorApproachingFloor(Elevator e, int floor) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handles a floor button being pressed.
	 * @param request the new RequestData to add to the Scheduler
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleFloorButtonPressed(RequestData request) {
		throw new IllegalStateException();
	}
	
	@Override
	public void doEntry() {}
	@Override
	public void doExit() {}

}
