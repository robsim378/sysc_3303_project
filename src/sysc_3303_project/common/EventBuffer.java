/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package sysc_3303_project.common;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Andrei Popescu
 *
 * An event buffer to hold all of the requests to a thread
 */
public class EventBuffer<T> {
	
	private Queue<Event<T>> buffer;
	
	/**
	 * Constructor, generates a linked list to use
	 */
	public EventBuffer() {
		buffer = new LinkedList<>();
	}
	
	/**
	 * Adds an event to the linked list of available requests
	 * @param event		Event, event to add
	 */
	public synchronized void addEvent(Event<T> event) {
		buffer.add(event);
		notifyAll();
	}
	
	/**
	 * Takes an event from the list of the linked list
	 * @return		Event, the event taken
	 */
	public synchronized Event<T> getEvent() {
		while (buffer.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
		return buffer.remove();
	}
}
