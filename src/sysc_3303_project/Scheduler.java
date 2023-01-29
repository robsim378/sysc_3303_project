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
public class Scheduler {
	
	private Queue<RequestData> incomingRequests;
	
	public Scheduler() {
		incomingRequests = new LinkedList<>();
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
}
