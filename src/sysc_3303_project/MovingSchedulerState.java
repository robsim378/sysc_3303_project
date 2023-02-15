/**
 * 
 */
package sysc_3303_project;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author apope
 *
 */
public class MovingSchedulerState extends BaseSchedulerState {

	public MovingSchedulerState(Scheduler scheduler) {
		super(scheduler);
	}

	@Override
	public void handleElevatorApproachingFloor(Elevator elevator, int floorNumber) {
		boolean stopping = false;
		for (RequestData requestData : scheduler.getInProgressRequests()) {
			if (requestData.getDestinationFloor() == floorNumber) {
				stopping = true;
				break;
			}
		}
		for (RequestData requestData : scheduler.getPendingRequests()) {
			if (requestData.getCurrentFloor() == floorNumber) {
				stopping = true;
				break;
			}
		}
		if (stopping) {
			scheduler.getElevator().stopAtNextFloor();
			scheduler.setState(new DoorsOpeningSchedulerState(scheduler));
		} else {
			scheduler.getElevator().doNotStopAtNextFloor(); //acknowledgement, does nothing
		}
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
		Elevator elevator = scheduler.getElevator();
		if (elevator.getCurrentFloor() < scheduler.getTargetFloor()) {
			elevator.setDirection(Direction.UP);
		} else {
			elevator.setDirection(Direction.DOWN);
		}
		elevator.move();
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
