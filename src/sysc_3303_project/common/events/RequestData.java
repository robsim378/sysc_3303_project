/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.common.events;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import sysc_3303_project.common.Direction;

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
	
	private final int error;
	
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
		
		int errorType;
		
		try {
			errorType = Integer.parseInt(requestParameters[4]);
		} catch (Exception e) {
			errorType = 0;
		}
		
		return new RequestData(time, currentFloor, direction, destinationFloor, errorType);
	}
	
	/**
	 * Creates a new RequestData from the given parameters.
	 * @param requestTime the time at which the request was created
	 * @param currentFloor the number of the floor from which the request was sent
	 * @param direction the direction (UP/DOWN) associated with the request
	 * @param destinationFloor the number of the floor to go to
	 */
	public RequestData(LocalTime requestTime, int currentFloor, Direction direction, int destinationFloor, int errorType) {
		this.requestTime=requestTime;
		this.currentFloor=currentFloor;
		this.direction=direction;
		this.destinationFloor=destinationFloor;
		this.error = errorType;
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
		return requestTime.toString() + " " + currentFloor + " " + direction.toString() + " " + destinationFloor + (hasError()? " " + error : "");
	}
	
	/**
	 * Returns if the request has an error associated with it
	 * @return		boolean, if the request has an error
	 */
	public boolean hasError() {
		return error != 0;
	}
	
	/**
	 * Returns the value of the error ID
	 * @return		int, error ID
	 */
	public int getError() {
		return error;
	}
	
	@Override
	public int hashCode() {
	    int hash = 7;
	    hash = 31 * hash + (int) error;
	    hash = 31 * hash + (requestTime == null ? 0 : requestTime.hashCode());
	    hash = 31 * hash + (int) currentFloor;
	    hash = 31 * hash + (direction == null ? 0 : direction.hashCode());
	    hash = 31 * hash + (int) destinationFloor;
	    return hash;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        RequestData data = (RequestData) o;
        return error == data.error
	    	&& currentFloor == data.currentFloor
	        && destinationFloor == data.destinationFloor
    		&& direction.equals(data.getDirection())
			&& requestTime.equals(data.getRequestTime());
    }
}
