/**
 * 
 */
package sysc_3303_project;

import java.time.LocalTime;

/**
 * @author Owner
 *
 */
public class RequestData {
	
	private final LocalTime requestTime;
	
	private final int currentFloor;
	
	private final Direction direction;
	
	private final int destinationFloor;
	
	public RequestData(LocalTime requestTime, int currentFloor, Direction direction, int destinationFloor) {
		this.requestTime=requestTime;
		this.currentFloor=currentFloor;
		this.direction=direction;
		this.destinationFloor=destinationFloor;
	}
	
	
	public LocalTime getRequestTime() {
		return requestTime;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public int getDestinationFloor() {
		return destinationFloor;
	}
	

}
