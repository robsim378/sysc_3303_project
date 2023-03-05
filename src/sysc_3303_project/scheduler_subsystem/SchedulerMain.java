/**
 * 
 */
package sysc_3303_project.scheduler_subsystem;

import java.util.LinkedList;
import java.util.List;

import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.messaging.UDPMessagerIncoming;
import sysc_3303_project.messaging.UDPMessagerOutgoing;

/**
 * @author Andrei Popescu
 * Main class for the Scheduler subsystem.
 */
public class SchedulerMain {

	/**
	 * Runs the Scheduler subsystem program.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		EventBuffer<?> outgoingBuffer = new EventBuffer<>();
		EventBuffer<SchedulerEventType> schedulerBuffer = new EventBuffer<>();
		Scheduler scheduler = new Scheduler(schedulerBuffer, outgoingBuffer);
		UDPMessagerOutgoing out = new UDPMessagerOutgoing(outgoingBuffer);
		UDPMessagerIncoming<SchedulerEventType> in = new UDPMessagerIncoming<SchedulerEventType>(new LinkedList<>() {{add(schedulerBuffer);}});
		
		//create threads
		Thread schedulerThread = new Thread(scheduler);
		Thread outUdpThread = new Thread(out);
		Thread inUdpThread = new Thread(in);
		outUdpThread.start();
		inUdpThread.start();
		schedulerThread.start();
	}

}