/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package sysc_3303_project.scheduler_subsystem;

import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.scheduler_subsystem.states.SchedulerState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerWaitingState;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import logging.Logger;

/**
 * 	@author Andrei Popescu
 *	The Scheduler class is responsible for passing requests from the Floor to the Elevator,
 *	and passing the responses from the Elevator back to the Floor. It acts as a monitor.
 */
public class Scheduler implements Runnable {
	
	private Queue<RequestData> requestQueue; //represents the ordered queue of all requests, determines the next active request
	private List<RequestData> pendingRequests; //requests where the passenger needs to be picked up
	private List<RequestData> inProgressRequests; //requests where the passenger needs to be brought to a destination
	private RequestData activeRequest; //request currently being handled
	private int targetFloor;
	private final EventBuffer<SchedulerEventType> eventBuffer;
	private final EventBuffer<ElevatorEventType> elevatorBuffer;
	private final EventBuffer<FloorEventType> floorBuffer;
	private SchedulerState state;
	
	/**
	 * Creates a new Scheduler with no requests, which interacts with an Elevator and a FloorSystem via buffers.
	 * @param elevatorBuffer event buffer to send events to an Elevator
	 * @param floorBuffer event buffer to send events to a Floor
	 */
	public Scheduler(EventBuffer<ElevatorEventType> elevatorBuffer, EventBuffer<FloorEventType> floorBuffer) {
		requestQueue = new LinkedList<>();
		pendingRequests = new LinkedList<>();
		inProgressRequests = new LinkedList<>();
		eventBuffer = new EventBuffer<>();
		this.elevatorBuffer = elevatorBuffer;
		this.floorBuffer = floorBuffer;
		state = new SchedulerWaitingState(this);
		activeRequest = null;
		targetFloor = -1;
	}
	
	/**
	 * Gets this Scheduler's event buffer.
	 * @return this Scheduler's EventBuffer
	 */
	public EventBuffer<SchedulerEventType> getEventBuffer() {
		return eventBuffer;
	}
	
	/**
	 * Gets the event buffer of the Elevator associated with this Scheduler.
	 * @return the Elevator's EventBuffer
	 */
	public EventBuffer<ElevatorEventType> getElevatorBuffer() {
		return elevatorBuffer;
	}
	
	/**
	 * Gets the floor where this Scheduler is currently trying to
	 * route the Elevator.
	 * @return the target floor's number
	 */
	public int getTargetFloor() {
		return targetFloor;
	}
	
	/**
	 * Sets an existing request as active, indicating that it should be served as soon as possible.
	 * @param request the RequestData for the request to activate
	 */
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
	
	/**
	 * Sets a new request as active, indicating that it should be served as soon as possible.
	 * Chooses requests in FIFO order, i.e. oldest first.
	 */
	public synchronized void setActiveRequest() {
		if (!requestQueue.isEmpty()) {
			setActiveRequest(requestQueue.peek());
		} else {
			activeRequest = null;
			Logger.getLogger().logNotification(this.getClass().getName(), "No active request");
		}
	}
	
	/**
	 * Adds a pending request to be served by this Scheduler.
	 * @param request the RequestData for the request to add
	 */
	public synchronized void addPendingRequest(RequestData request) {
		pendingRequests.add(request);
		requestQueue.add(request);
		Logger.getLogger().logNotification(this.getClass().getName(), "Added new pending request: " + request.toString());
		if (activeRequest == null) {
			setActiveRequest();
		}
	}
	
	/**
	 * Marks a pending request as "in progress", indicating that the passengers have been
	 * picked up.
	 * @param request the RequestData for the request to mark as in progress
	 */
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
	
	/**
	 * Completes an in-progress request, indicating that passengers have been delivered to the
	 * destination and there is nothing more to do for the request.
	 * @param request the RequestData for the request to complete
	 */
	public synchronized void completeRequest(RequestData request) {
		pendingRequests.remove(request);
		inProgressRequests.remove(request);
		requestQueue.remove(request);
		Logger.getLogger().logNotification(this.getClass().getName(), "Passengers brought to destination for request: " + request.toString());
		if (activeRequest == request) {
			setActiveRequest();
		}
	}
	
	/**
	 * Gets all pending requests managed by this Scheduler.
	 * @return the List of RequestData for pending requests
	 */
	public synchronized List<RequestData> getPendingRequests() {
		return pendingRequests;
	}
	
	/**
	 * Gets all in-progress requests managed by this Scheduler.
	 * @return the List of RequestData for in-progress requests
	 */
	public synchronized List<RequestData> getInProgressRequests() {
		return inProgressRequests;
	}
	
	/**
	 * Checks whether this Scheduler is currently serving any requests
	 * (pending or in-progress).
	 * @return true if there is at least one in-progress or pending request,
	 * false otherwise.
	 */
	public synchronized boolean hasRequests() {
		return !requestQueue.isEmpty();
	}
	
	/**
	 * Parses and events from this Scheduler's event buffer in an infinite loop.
	 */
	public void eventLoop() {
		while (true) {

			Event<SchedulerEventType> evt = eventBuffer.getEvent();
			SchedulerState newState = null;


    		Logger.getLogger().logNotification(this.getClass().getName(), "Event: " + evt.getEventType() + ", State: " + state.getClass().getName());    		

			
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
			
			if (newState != null) {
				state.doExit();
				state = newState;
				state.doEntry();
			}
		}
	}

	@Override
	public void run() {
		Logger.getLogger().logNotification(this.getClass().getName(), "Scheduler thread running");
		try {
			eventLoop();
		} catch (IllegalStateException e) {
			Logger.getLogger().logError(this.getClass().getName(), e.toString());
		}
		
	}
}
