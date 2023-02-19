/**
 * 
 */
package sysc_3303_project.common;


/**
 * @author Andrei Popescu
 *
 * An event class that holds the nessesary information to send events to
 *   other threads
 */
public class Event<T> {
	
	private T eventType;
	private Object senderObject;
	private Object payloadObject;
	
	/**
	 * Creates an event with a given event type, source, and payload
	 * @param type		T, enum for the type of request
	 * @param sender	Object, the sender
	 * @param payload	Object, payload to send
	 */
	public Event(T type, Object sender, Object payload) {
		this.eventType = type;
		this.senderObject = sender;
		this.payloadObject = payload;
	}
	
	/**
	 * Getter for the event type
	 * @return	T, enum for the request type
	 */
	public T getEventType() {
		return eventType;
	}
	
	/**
	 * Getter for the sender
	 * @return	Object, the thing that sends the event
	 */
	public Object getSender() {
		return senderObject;
	}
	
	/**
	 * Getter for the payload
	 * @return	Object, a payload object
	 */
	public Object getPayload() {
		return payloadObject;
	}
}
