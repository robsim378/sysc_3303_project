/**
 * 
 */
package sysc_3303_project;


/**
 * @author apope
 *
 */
public class Event<T> {
	
	private T eventType;
	private Object senderObject;
	private Object payloadObject;
	
	public Event(T type, Object sender, Object payload) {
		this.eventType = type;
		this.senderObject = sender;
		this.payloadObject = payload;
	}
	
	public T getEventType() {
		return eventType;
	}
	
	public Object getSender() {
		return senderObject;
	}
	
	public Object getPayload() {
		return payloadObject;
	}
}
