/**
 * 
 */
package sysc_3303_project;

/**
 * @author apope
 *
 */
public abstract class BaseSchedulerState implements SchedulerState {
	
	protected Scheduler scheduler;
	
	public BaseSchedulerState(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	@Override
	public void handleFloorButtonPressed(RequestData requestData) {
		scheduler.addPendingRequest(requestData);
	}
}
