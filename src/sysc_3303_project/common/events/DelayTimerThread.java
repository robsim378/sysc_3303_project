/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.common.events;

/**
 * 
 * @author Liam Gaudet
 *
 * @param Event type, determines which thread can receive
 * 
 * A thread process that administers a delayed event trigger for various purposes. For
 *   example, delaying the time before an elevator request is made or delaying the time between floors
 */
public class DelayTimerThread<T extends Enum<?>> implements Runnable {
	
	private int timeDelay;
	private Event<T> event;
	private EventBuffer<T> eventBuffer;
	
	/**
	 * Constructor
	 * @param timeDelay		int, delay in milliseconds to wait
	 * @param event			Event, event to send
	 * @param buffer		EventBuffer, the buffer to send the event to
	 */
	public DelayTimerThread(int timeDelay, Event<T> event, EventBuffer<T> buffer) {
		this.timeDelay = timeDelay;
		this.event = event;
		this.eventBuffer = buffer;
	}
	
	/**
	 * Delays for a certain time period and after triggering sends an event to the specified event buffer
	 */
	@Override
	public void run() {
		
		// Wait for a specified amount of time before triggering the event
		try {
			Thread.sleep(timeDelay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Trigger the event and then finish
		eventBuffer.addEvent(event);
	}

}
