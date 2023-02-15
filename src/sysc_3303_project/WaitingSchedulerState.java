/**
 * 
 */
package sysc_3303_project;

/**
 * @author apope
 *
 */
public class WaitingSchedulerState extends BaseSchedulerState {

	public WaitingSchedulerState(Scheduler scheduler) {
		super(scheduler);
	}

	@Override
	public void handleFloorButtonPressed(RequestData requestData) {
		super.handleFloorButtonPressed(requestData);
		scheduler.setState(new DoorsClosingSchedulerState(scheduler));
	}

	@Override
	public void handleElevatorApproachingFloor(Elevator elevator, int floorNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleElevatorButtonPressed(Elevator elevator, int buttonNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDoorsClosed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDoorsOpened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub

	}

}
