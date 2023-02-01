/**
 * 
 */
package sysc_3303_project;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Owner
 *
 */
public class RequestData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final LocalTime requestTime;
	
	private final int currentFloor;
	
	private final Direction direction;
	
	private final int destinationFloor;
	
	public static RequestData of(String line) {
		String[] requestParameters = line.split(" ");
		
		LocalTime time = LocalTime.parse(requestParameters[0], DateTimeFormatter.ofPattern("HH:mm:ss:AAA"));
		int currentFloor = Integer.parseInt(requestParameters[1]);
		Direction direction = Direction.valueOf(requestParameters[2]);
		int destinationFloor = Integer.parseInt(requestParameters[3]);
		
		return new RequestData(time, currentFloor, direction, destinationFloor);
	}
	
	private RequestData(LocalTime requestTime, int currentFloor, Direction direction, int destinationFloor) {
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
	
	
	@Override
	public String toString() {
		return requestTime.toString() + " " + currentFloor + " " + direction.toString() + " " + destinationFloor;
	}
	
	

}
