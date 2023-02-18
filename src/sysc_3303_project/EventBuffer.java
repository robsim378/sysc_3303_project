/**
 * 
 */
package sysc_3303_project;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author apope
 *
 */
public class EventBuffer<T> {
	
	private Queue<Event<T>> buffer;
	
	public EventBuffer() {
		buffer = new LinkedList<>();
	}
	
	public synchronized void addEvent(Event<T> event) {
		buffer.add(event);
		notifyAll();
	}
	
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
