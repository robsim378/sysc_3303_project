package sysc_3303_project.common.events;

import java.io.Serializable;

import sysc_3303_project.common.configuration.Subsystem;

public class Event<T extends Enum<?>> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4717946647791314554L;
	
	private Subsystem destinationSubsystem;
	private int destinationID;
	private Subsystem sourceSubsystem;
	private int sourceID;
	private T eventType;
	private Serializable payload;
	
	public Event(Subsystem dest, int destID, Subsystem src, int srcID, T type, Serializable payload) {
		this.destinationSubsystem = dest;
		this.destinationID = destID;
		this.sourceSubsystem = src;
		this.sourceID = srcID;
		this.eventType = type;
		this.payload = payload;
	}
	
	public Subsystem getDestinationSubsystem() {
		return destinationSubsystem;
	}
	
	public Subsystem getSourceSubsystem() {
		return sourceSubsystem;
	}
	
	public int getDestinationID() {
		return destinationID;
	}
	
	public int getSourceID() {
		return sourceID;
	}
	
	public T getEventType() {
		return eventType;
	}
	
	public Serializable getPayload() {
		return payload;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{Destination: [");
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
}
