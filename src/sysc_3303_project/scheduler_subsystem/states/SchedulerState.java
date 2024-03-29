/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.scheduler_subsystem.states;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.state.State;
import sysc_3303_project.scheduler_subsystem.ElevatorTracker;
import sysc_3303_project.scheduler_subsystem.Scheduler;

/**
 * @author Andrei Popescu
 * Base class for states from the Scheduler's state machine.
 * Provides entry and exit actions, as well as default handlers for
 * all requisite events (which throw exceptions) to be overridden.
 */
public abstract class SchedulerState implements State {
	
	protected Scheduler context;
	protected ElevatorTracker contextTracker;
	
	/**
	 * Creates a new SchedulerState.
	 * @param context the Scheduler this state is tied to
	 */
	public SchedulerState(Scheduler context) {
		this.context = context;
		this.contextTracker = context.getTracker();
	}
	
	/**
	 * Handles an elevator's doors closing completely.
	 * @param elevatorID the ID of the elevator triggering the event
	 * @param floorNumber the number of the floor that the elevator is at
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorDoorsClosed(int elevatorId, int floorNumber) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handles an elevator's doors opening completely.
	 * @param elevatorID the ID of the elevator triggering the event
	 * @param floorNumber the number of the floor that the elevator is at
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorDoorsOpened(int elevatorId, int floorNumber) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handles an elevator stopping at a floor.
	 * @param elevatorID the ID of the elevator triggering the event
	 * @param floorNumber the number of the floor that the elevator stopped at
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorStopped(int elevatorId, int floorNumber) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handles an elevator approaching a floor.
	 * @param elevatorID the ID of the elevator triggering the event
	 * @param floorNumber the number of the floor that the elevator is approaching
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorApproachingFloor(int elevatorId, int floorNumber) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handles a floor button being pressed.
	 * @param floorId the number of the floor where the button was pressed
	 * @param direction the direction (up/down) of the pressed directional button
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleFloorButtonPressed(int floorNumber, Direction direction) {
		throw new IllegalStateException();
	}
	
	@Override
	public void doEntry() {}
	@Override
	public void doExit() {}
	
	/**
	 * 
	 * @param elevatorId the ID of the elevator where the button was pressed
	 * @param floorNumber the floor number of the pressed button
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorButtonPressed(int elevatorId, int floorNumber) {
		throw new IllegalStateException();
	}
	
	/**
	 * Handle an elevator getting stuck.
	 * @param elevatorId the ID of the stuck elevator
	 * @return the next SchedulerState the state machine should proceed to
	 */
	public SchedulerState handleElevatorBlocked(int elevatorId) {
		throw new IllegalStateException();
	}
	
	public SchedulerState handleElevatorPing(int elevatorId) {
		context.getFaultDetector().clearTimers(elevatorId);
		context.getFaultDetector().addTimer(elevatorId, (int) (1000*1.5));
		return null;
	}

}
