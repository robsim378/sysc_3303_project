/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.common.events;

import java.io.Serializable;

import sysc_3303_project.common.configuration.Subsystem;

/**
 * Event object for transmitting data. Simply a getter/setter object
 * @author Andrei
 *
 * @param <T> type of enum to pass
 */
public class Event<T extends Enum<?>> implements Serializable {
	
	private static final long serialVersionUID = 4717946647791314554L;
	
	private static int counter = 1;
	
	private final int ID; 
	private Subsystem destinationSubsystem;
	private int destinationID;
	private Subsystem sourceSubsystem;
	private int sourceID;
	private T eventType;
	private Serializable payload;
	
	/**
	 * Constructor
	 * @param dest		Subsystem, location to send to
	 * @param destID	int, ID of subsystem recipient
	 * @param src		Subsystem, system sender
	 * @param srcID		int, ID of sender
	 * @param type		<T>, event type
	 * @param payload	Serializable, a serialisable payload
	 */
	public Event(Subsystem dest, int destID, Subsystem src, int srcID, T type, Serializable payload) {
		this.destinationSubsystem = dest;
		this.destinationID = destID;
		this.sourceSubsystem = src;
		this.sourceID = srcID;
		this.eventType = type;
		this.payload = payload;
		this.ID = getNewID(src);
	}
	
	/**
	 * Getter for destionation subsystem
	 * @return Subsystem, destination subsystem
	 */
	public Subsystem getDestinationSubsystem() {
		return destinationSubsystem;
	}
	
	/**
	 * Getter for source subsystem
	 * @return Subsystem, source subsystem
	 */
	public Subsystem getSourceSubsystem() {
		return sourceSubsystem;
	}
	
	/**
	 * Getter for destination ID
	 * @return int, destination ID
	 */
	public int getDestinationID() {
		return destinationID;
	}
	
	/**
	 * Getter for source ID
	 * @return int, source ID
	 */
	public int getSourceID() {
		return sourceID;
	}
	
	/**
	 * Getter for event type
	 * @return <T>, event type
	 */
	public T getEventType() {
		return eventType;
	}
	
	/**
	 * Getter for ID
	 * @return int, ID of the event
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Getter for payload
	 * @return Serializable, payload
	 */
	public Serializable getPayload() {
		return payload;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{ID: ");
		builder.append(ID);
		builder.append(", Destination: [");
		builder.append(destinationSubsystem.name());
		builder.append(",");
		builder.append(destinationID);
		builder.append("], Source: [");
		builder.append(sourceSubsystem.name());
		builder.append(",");
		builder.append(sourceID);
		builder.append("], EventType: ");
		builder.append(eventType.name());
		builder.append(", payload: ");
		builder.append(payload==null? "null": payload.toString());
		builder.append("}");
		return builder.toString();
	}
	
	/**
	 * Determines an ID for a new subsystem.
	 * Must be synchronized as the counter is a critical section which should not
	 *   be accessed by multiple threads simultaneously
	 * @param src		Subsystem, the sender of the event. Helps determine ID
	 * @return			int, ID of the new event;
	 */
	private static synchronized int getNewID(Subsystem src) {
		Subsystem[] values = Subsystem.values();
		int toReturn=-1;
		for(int i = 0; i < values.length; i++) {
			if(src.equals(values[i])) {
				toReturn = counter*3 + i;
			}
		}
		counter++;
		
		return toReturn;
	}
}
