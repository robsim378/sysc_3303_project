/**
 * 
 */
package sysc_3303_project;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Andrei Popescu
 *
 */
public class Scheduler implements Runnable {
	
	private Queue<RequestData> incomingRequests;
	private Queue<RequestData> receivedResponses;
	
	public Scheduler() {
		incomingRequests = new LinkedList<>();
		receivedResponses = new LinkedList<>();
	}
	
	/**
	 * Checks whether there are pending requests to elevators for this Scheduler.
	 * @return true if there is at least one pending request, false otherwise
	 */
	public boolean hasRequests() {
		return !incomingRequests.isEmpty();
	}
	
	/**
	 * Adds a request to this Scheduler, that should be sent to the elevators to be served.
	 * @param request the RequestData to send to the elevators.
	 */
	public synchronized void addRequest(RequestData request) {
		incomingRequests.add(request);
		System.out.println("Scheduler received request from floor: " + request.toString());
		notifyAll();
	}
	
	/**
	 * Gets data for a request that needs to be served. This method waits for
	 * a request to be created before returning.
	 * @return RequestData corresponding to a request to serve
	 */
	public synchronized RequestData getRequest() {
		while (!hasRequests()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
		return incomingRequests.remove();
	}
	
	/**
	 * Checks whether the Scheduler has responses from elevators that need to be sent to a Floor.
	 * @return true if there is at least one pending response, false otherwise
	 */
	public boolean hasResponses() {
		return !receivedResponses.isEmpty();
	}
	
	/**
	 * Adds a response to this Scheduler that should be passed back to a Floor.
	 * @param response the RequestData to pass to the floor.
	 */
	public synchronized void addResponse(RequestData response) {
		receivedResponses.add(response);
		System.out.println("Scheduler received response from elevator: " + response.toString());

		notifyAll();
	}
	
	/**
	 * Gets data for a response corresponding to a served request. This method waits for
	 * a request to be served, and a response created, before returning.
	 * @return RequestData corresponding to a request to serve
	 */
	public synchronized RequestData getResponse() {
		while (!hasResponses()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
		return receivedResponses.remove();
	}

	@Override
	public void run() {
		// For this iteration, this thread does nothing - the Scheduler acts as a monitor, running on the main thread.
		
	}
}
