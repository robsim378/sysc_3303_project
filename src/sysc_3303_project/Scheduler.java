/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package sysc_3303_project;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 	@author Andrei Popescu
 *	The Scheduler class is responsible for passing requests from the Floor to the Elevator,
 *	and passing the responses from the Elevator back to the Floor. It acts as a monitor.
 */
public class Scheduler implements Runnable, SchedulerState {
	
	private Queue<RequestData> requestQueue; 
	private List<RequestData> pendingRequests;
	private List<RequestData> inProgressRequests;
	private RequestData activeRequest;
	private BaseSchedulerState state;
	private Elevator elevator;
	private int targetFloor;
	
	/**
	 * Creates a Scheduler with no requests or responses.
	 */
	public Scheduler() {
		activeRequest = null;
		pendingRequests = new LinkedList<>();
		inProgressRequests = new LinkedList<>();
		targetFloor = 1;
	}
	
	public Elevator getElevator() {
		return elevator;
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
	}
	
	public synchronized void setActiveRequest() {
		if (!requestQueue.isEmpty()) {
			setActiveRequest(requestQueue.peek());
		}
	}
	
	public synchronized void addPendingRequest(RequestData request) {
		pendingRequests.add(request);
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
		if (activeRequest == request) {
			targetFloor = request.getDestinationFloor();
		}
	}
	
	public synchronized void completeRequest(RequestData request) {
		pendingRequests.remove(request);
		inProgressRequests.remove(request);
		requestQueue.remove(request);
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
	
	public void setState(BaseSchedulerState newState) {
		state.onExit();
		state = newState;
		newState.onEnter();
		newState.doAction();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleFloorButtonPressed(RequestData requestData) {
		state.handleFloorButtonPressed(requestData);
	}

	@Override
	public void handleElevatorApproachingFloor(Elevator elevator, int floorNumber) {
		state.handleElevatorApproachingFloor(elevator, floorNumber);
	}

	@Override
	public void handleElevatorButtonPressed(Elevator elevator, int buttonNumber) {
		state.handleElevatorButtonPressed(elevator, buttonNumber);
	}

	@Override
	public void handleDoorsClosed() {
		state.handleDoorsClosed();
	}

	@Override
	public void handleDoorsOpened() {
		state.handleDoorsOpened();
	}

	@Override
	public void onEnter() {

	}

	@Override
	public void onExit() {
		
	}

	@Override
	public void doAction() {
		
	}
}
