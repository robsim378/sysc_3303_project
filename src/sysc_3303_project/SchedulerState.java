/**
 * 
 */
package sysc_3303_project;

/**
 * @author apope
 *
 */
public interface SchedulerState {
	public abstract void handleFloorButtonPressed(RequestData requestData);
	public abstract void handleElevatorApproachingFloor(Elevator elevator, int floorNumber);
	public abstract void handleElevatorButtonPressed(Elevator elevator, int buttonNumber);
	public abstract void handleDoorsClosed();
	public abstract void handleDoorsOpened();
	public abstract void onEnter();
	public abstract void onExit();
	public abstract void doAction();
}
