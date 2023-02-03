/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package sysc_3303_project;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 	@author Liam Gaudet
 *	Represents a request from a Floor to be serviced by an Elevator.
 */
public class RequestData implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private final LocalTime requestTime;
	
	private final int currentFloor;
	
	private final Direction direction;
	
	private final int destinationFloor;
	
	/**
	 * Creates a new RequestData corresponding to a line of text.
	 * Example format:
	 * 00:00:00.001 1 UP 2
	 * @param line the text to create a RequestData from
	 * @return the RequestData corresponding to the string
	 */
	public static RequestData of(String line) {
		String[] requestParameters = line.split(" ");
		
		LocalTime time = LocalTime.parse(requestParameters[0], DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
		int currentFloor = Integer.parseInt(requestParameters[1]);
		Direction direction = Direction.valueOf(requestParameters[2]);
		int destinationFloor = Integer.parseInt(requestParameters[3]);
		
		return new RequestData(time, currentFloor, direction, destinationFloor);
	}
	
	/**
	 * Creates a new RequestData from the given parameters.
	 * @param requestTime the time at which the request was created
	 * @param currentFloor the number of the floor from which the request was sent
	 * @param direction the direction (UP/DOWN) associated with the request
	 * @param destinationFloor the number of the floor to go to
	 */
	public RequestData(LocalTime requestTime, int currentFloor, Direction direction, int destinationFloor) {
		this.requestTime=requestTime;
		this.currentFloor=currentFloor;
		this.direction=direction;
		this.destinationFloor=destinationFloor;
	}
	
	/**
	 * Returns the time at which this request was sent.
	 * @return the time at which this request was sent
	 */
	public LocalTime getRequestTime() {
		return requestTime;
	}
	
	/**
	 * Returns the number of the floor from which this request was sent.
	 * @return the number of the floor from which this request was sent
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	/**
	 * Returns the direction (UP/DOWN) associated with this request.
	 * @return the direction associated with this request
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Returns the number of the destination floor of this request.
	 * @return the number of the destination floor of this request
	 */
	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	@Override
	public String toString() {
		return requestTime.toString() + " " + currentFloor + " " + direction.toString() + " " + destinationFloor;
	}
}
