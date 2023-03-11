/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package test.messaging;

import java.util.List;

import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerIncoming;
import sysc_3303_project.common.messaging.UDPMessagerOutgoing;

/**
 * Requires two computers, one to run the client and one for the server
 * @author Liam
 *
 */
public class SimpleUDPMessagerClientTest  implements Runnable{
	
	private EventBuffer<testEnum> input;
	private EventBuffer<testEnum> output;
	
	/**
	 * Constructor with two event buffers
	 * @param input		EventBuffer, input events
	 * @param output	EventBuffer, output events
	 */
	public SimpleUDPMessagerClientTest (EventBuffer<testEnum> input, EventBuffer<testEnum> output){
		this.input = input;
		this.output = output;
	}
	
	@Override
	public void run() {
		
		for(int i = 0; i < 10; i++) {
			Event<testEnum> e = new Event<testEnum>(Subsystem.ELEVATOR, 0, Subsystem.FLOOR, 0, testEnum.VALUE1, null);
			
			System.out.println(e.toString());

			output.addEvent(e);
			
			Event<testEnum> e2 = input.getEvent();
			
			System.out.println(e2.toString());
		}
	}
	
	
	/**
	 * Main method to build the nessesary requirements. Run server first.
	 * @param args		N/A
	 */
	public static void main (String args[]) {
		EventBuffer<testEnum> sToC = new EventBuffer<testEnum>();

		EventBuffer<testEnum> cToS = new EventBuffer<testEnum>();
		
		SimpleUDPMessagerClientTest client = new SimpleUDPMessagerClientTest(sToC, cToS);
		
		UDPMessagerIncoming<testEnum> incoming = new UDPMessagerIncoming<testEnum>(List.of(sToC), Subsystem.FLOOR);
		UDPMessagerOutgoing outgoing = new UDPMessagerOutgoing(cToS);
		
		Thread clientThread = new Thread(client);
		Thread inThread = new Thread(incoming);
		Thread outThread = new Thread(outgoing);
		
		inThread.start();
		outThread.start();
		clientThread.start();
	}
	
	
	/**
	 * Basic enum to use for testing
	 * @author Liam
	 *
	 */
	public enum testEnum{
		VALUE1,
		VALUE2
	}

}
