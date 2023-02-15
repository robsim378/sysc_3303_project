package sysc_3303_project;

public class DoorsClosingSchedulerState extends BaseSchedulerState {

	public DoorsClosingSchedulerState(Scheduler scheduler) {
		super(scheduler);
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
		scheduler.setState(new MovingSchedulerState(scheduler));
	}

	@Override
	public void handleDoorsOpened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		scheduler.getElevator().closeDoors();
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
