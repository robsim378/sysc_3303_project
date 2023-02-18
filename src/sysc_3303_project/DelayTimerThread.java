package sysc_3303_project;

import logging.Logger;

public class DelayTimerThread<T> implements Runnable {
	
	private int timeDelay;
	private Event<T> event;
	private EventBuffer<T> eventBuffer;
	
	public DelayTimerThread(int timeDelay, Event<T> event, EventBuffer<T> buffer) {
		this.timeDelay = timeDelay;
		this.event = event;
		this.eventBuffer = buffer;
	}
	
	
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
