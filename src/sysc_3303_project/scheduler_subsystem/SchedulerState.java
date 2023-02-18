/**
 * 
 */
package sysc_3303_project.scheduler_subsystem;

import sysc_3303_project.RequestData;
import sysc_3303_project.State;
import sysc_3303_project.ElevatorSubsystem.Elevator;

/**
 * @author apope
 *
 */
public abstract class SchedulerState implements State {
	
	protected Scheduler context;
	
	public SchedulerState(Scheduler context) {
		this.context = context;
	}
	
	public SchedulerState handleElevatorDoorsClosed(Elevator e) {
		throw new IllegalStateException();
	}
	public SchedulerState handleElevatorDoorsOpened(Elevator e) {
		throw new IllegalStateException();
	}
	public SchedulerState handleElevatorStopped(Elevator e, int floor) {
		throw new IllegalStateException();
	}
	public SchedulerState handleElevatorApproachingFloor(Elevator e, int floor) {
		throw new IllegalStateException();
	}
	public SchedulerState handleFloorButtonPressed(RequestData request) {
		throw new IllegalStateException();
	}
	@Override
	public void doEntry() {}
	@Override
	public void doExit() {}

}
