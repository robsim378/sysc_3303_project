package sysc_3303_project.common;

import java.io.Serializable;

public class Event<T> implements Serializable {
	
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
}
