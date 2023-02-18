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
		Logger.getLogger().logNotification(this.getClass().getName(), "Timer started: " + timeDelay + " milliseconds to event buffer " + eventBuffer.toString());
		try {
			Thread.sleep(timeDelay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Logger.getLogger().logNotification(this.getClass().getName(), "Timer completed: " + timeDelay + " milliseconds to event buffer " + eventBuffer.toString());
		// Trigger the event and then finish
		eventBuffer.addEvent(event);
		
	}

}
