/**
 * 
 */
package scheduler_subsystem;

import sysc_3303_project.Elevator;
import sysc_3303_project.RequestData;
import sysc_3303_project.Scheduler;
import sysc_3303_project.State;

/**
 * @author apope
 *
 */
public abstract class SchedulerState implements State {
	
	protected Scheduler context;
	
	public SchedulerState(Scheduler context) {
		this.context = context;
	}
	
	public abstract SchedulerState handleElevatorDoorsClosed(Elevator e);
	public abstract SchedulerState handleElevatorDoorsOpened(Elevator e);
	public abstract SchedulerState handleElevatorStopped(Elevator e, int floor);
	public abstract SchedulerState handleElevatorApproachingFloor(Elevator e, int floor);
	public abstract SchedulerState handleFloorButtonPressed(RequestData request);
	@Override
	public void doEntry() {}
	@Override
	public void doExit() {}

}
