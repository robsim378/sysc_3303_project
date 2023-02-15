/**
 * 
 */
package sysc_3303_project;

/**
 * @author apope
 *
 */
public class DoorsOpeningSchedulerState extends BaseSchedulerState {

	public DoorsOpeningSchedulerState(Scheduler scheduler) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDoorsOpened() {
		if (scheduler.hasRequests()) {
			scheduler.setState(new DoorsClosingSchedulerState(scheduler));
		} else {
			scheduler.setState(new WaitingSchedulerState(scheduler));
		}
	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		scheduler.getElevator().openDoors();
	}

	@Override
	public void onExit() {
		
	}

	@Override
	public void doAction() {
		int currentFloor = scheduler.getElevator().getCurrentFloor();
		for (RequestData request : scheduler.getPendingRequests()) {
			if (request.getCurrentFloor() == currentFloor) {
				scheduler.markRequestInProgress(request);
			}
		}
		for (RequestData request : scheduler.getInProgressRequests()) {
			if (request.getDestinationFloor() == currentFloor) {
				scheduler.completeRequest(request);
			}
		}
	}

}
