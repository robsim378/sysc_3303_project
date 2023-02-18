/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package sysc_3303_project;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import logging.Logger;
import scheduler_subsystem.SchedulerState;
import scheduler_subsystem.WaitingSchedulerState;

/**
 * 	@author Andrei Popescu
 *	The Scheduler class is responsible for passing requests from the Floor to the Elevator,
 *	and passing the responses from the Elevator back to the Floor. It acts as a monitor.
 */
public class Scheduler implements Runnable {
	
	private Queue<RequestData> requestQueue; 
	private List<RequestData> pendingRequests;
	private List<RequestData> inProgressRequests;
	private RequestData activeRequest;
	private int targetFloor;
	private final EventBuffer<SchedulerEventType> eventBuffer;
	private final EventBuffer<ElevatorEventType> elevatorBuffer;
	private final EventBuffer<FloorEventType> floorBuffer;
	private SchedulerState state;
	
	/**
	 * Creates a Scheduler with no requests or responses.
	 */
	public Scheduler(EventBuffer<ElevatorEventType> elevatorBuffer, EventBuffer<FloorEventType> floorBuffer) {
		requestQueue = new LinkedList<>();
		pendingRequests = new LinkedList<>();
		inProgressRequests = new LinkedList<>();
		eventBuffer = new EventBuffer<>();
		this.elevatorBuffer = elevatorBuffer;
		this.floorBuffer = floorBuffer;
		state = new WaitingSchedulerState(this);
		activeRequest = null;
		targetFloor = -1;
	}
	
	public EventBuffer<SchedulerEventType> getEventBuffer() {
		return eventBuffer;
	}
	
	public EventBuffer<ElevatorEventType> getElevatorBuffer() {
		return elevatorBuffer;
	}
	
	public int getTargetFloor() {
		return targetFloor;
	}
	
	private synchronized void setActiveRequest(RequestData request) {
		activeRequest = request;
		if (inProgressRequests.contains(request)) {
			targetFloor = request.getDestinationFloor();
		} else {
			targetFloor = request.getCurrentFloor();
		}
		Logger.getLogger().logNotification(this.getClass().getName(), "Active request is now: " + request.toString());
		Logger.getLogger().logNotification(this.getClass().getName(), "Target floor is now " + targetFloor);
	}
	
	public synchronized void setActiveRequest() {
		if (!requestQueue.isEmpty()) {
			setActiveRequest(requestQueue.peek());
		} else {
			activeRequest = null;
			Logger.getLogger().logNotification(this.getClass().getName(), "No active request");
		}
	}
	
	public synchronized void addPendingRequest(RequestData request) {
		pendingRequests.add(request);
		requestQueue.add(request);
		Logger.getLogger().logNotification(this.getClass().getName(), "Added new pending request: " + request.toString());
		if (activeRequest == null) {
			setActiveRequest();
		}
	}
	
	public synchronized void markRequestInProgress(RequestData request) {
		if (!pendingRequests.contains(request)) {
			return;
		}
		pendingRequests.remove(request);
		inProgressRequests.add(request);
		Logger.getLogger().logNotification(this.getClass().getName(), "Picked up passenger for request: " + request.toString());
		if (activeRequest == request) {
			targetFloor = request.getDestinationFloor();
			Logger.getLogger().logNotification(this.getClass().getName(), "Target floor is now " + targetFloor);
		}
	}
	
	public synchronized void completeRequest(RequestData request) {
		pendingRequests.remove(request);
		inProgressRequests.remove(request);
		requestQueue.remove(request);
		Logger.getLogger().logNotification(this.getClass().getName(), "Passengers brought to destination for request: " + request.toString());
		if (activeRequest == request) {
			setActiveRequest();
		}
	}
	
	public synchronized List<RequestData> getPendingRequests() {
		return pendingRequests;
	}
	
	public synchronized List<RequestData> getInProgressRequests() {
		return inProgressRequests;
	}
	
	public synchronized boolean hasRequests() {
		return !requestQueue.isEmpty();
	}
	
	public void eventLoop() {
		while (true) {
			Logger.getLogger().logNotification(this.getClass().getName(), "Waiting for new event...");
			Event<SchedulerEventType> evt = eventBuffer.getEvent();
			SchedulerState newState = null;
			
			switch(evt.getEventType()) {
				case ELEVATOR_DOORS_CLOSED:
					newState = state.handleElevatorDoorsClosed((Elevator) evt.getSender());
					break;
				case ELEVATOR_DOORS_OPENED:
					newState = state.handleElevatorDoorsOpened((Elevator) evt.getSender());
					break;
				case ELEVATOR_APPROACHING_FLOOR:
					newState = state.handleElevatorApproachingFloor((Elevator) evt.getSender(), (int) evt.getPayload());
					break;
				case ELEVATOR_STOPPED:
					newState = state.handleElevatorStopped((Elevator) evt.getSender(), (int) evt.getPayload());
					break;
				case FLOOR_BUTTON_PRESSED:
					newState = state.handleFloorButtonPressed((RequestData) evt.getPayload());
					break;
			}
			
			if (state != null) {
				state.doExit();
				state = newState;
				state.doEntry();
			}
		}
	}

	@Override
	public void run() {
		// For this iteration, this thread does nothing - the Scheduler acts as a monitor, running on the main thread.
		Logger.getLogger().logNotification(this.getClass().getName(), "Scheduler thread running");
		try {
			eventLoop();
		} catch (IllegalStateException e) {
			Logger.getLogger().logError(this.getClass().getName(), e.toString());
		}
		
	}
}
