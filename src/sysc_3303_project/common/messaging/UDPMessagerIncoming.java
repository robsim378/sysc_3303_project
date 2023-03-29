/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.common.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;

/**
 * Handles incoming UDP connections
 * @author Liam
 *
 * @param <T> type of input to process
 */
public class UDPMessagerIncoming<T extends Enum<?>> extends UDPMessager implements Runnable{
	
	// Event buffers to forward messages to
	private List<EventBuffer<T>> eventBuffers;
	
	// A set of all completed events
	private Set<Integer> completedEvents;
		
	// The send/receiving socket to use
	DatagramSocket sendRecieveSocket = null;
	
	/**
	 * Constructor. Uses a list of event buffers and identifies its subsystem
	 * @param eventBuffers
	 * @param sys
	 */
	public UDPMessagerIncoming(List<EventBuffer<T>> eventBuffers, Subsystem sys) {
		completedEvents = new HashSet<Integer>();
		this.eventBuffers = eventBuffers;
		try {
			sendRecieveSocket = new DatagramSocket(getPort(sys));
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		while(true) {
			byte[] msg = new byte[1000];
			DatagramPacket packet = new DatagramPacket(msg, 1000);
			
			// Receive the data
			try {
				sendRecieveSocket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Parse the packet data into an event object
			Event<T> e = parseEvent(packet);
			
			// Respond to sender with an acknowledgment of the request
			respond(e, packet);

			// Process the event if it has not been processed yet
			if(completedEvents.contains(e.getID())) {
				continue;
			} else {
				processEvent(e);
			}
		}
	}
	
	/**
	 * Processes an event by forwarding it to the receiving entities
	 * @param e		Event<T>, event to send
	 */
	private void processEvent(Event<T> e) {
		completedEvents.add(e.getID());
		int destination = e.getDestinationID();
		if(destination == -1) {
			for(EventBuffer<T> buff : eventBuffers){
				buff.addEvent(e);
			}
		} else if (destination >=0 && destination < eventBuffers.size()) {
			eventBuffers.get(e.getDestinationID()).addEvent(e);
		}
	}

	/**
	 * Generates a response to a request and sends it
	 * @param e			Event<T>, event that was send with the request
	 * @param packet	DatagramPacket, the packet containing response information
	 */
	private void respond(Event<T> e, DatagramPacket packet) {
		DatagramPacket response = new DatagramPacket(new byte[] {(byte) e.getID()}, 1, packet.getAddress(), packet.getPort());;
		try {
			sendRecieveSocket.send(response);
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}

	/**
	 * Parses an datagram packet into an Event object
	 * @param packet		DatagramPacket, packet to parse
	 * @return				Event<T>, event that was parsed
	 */
	public Event<T> parseEvent(DatagramPacket packet){
		ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
		ObjectInput in = null;
		Event<T> e = null;
		
		try {
			in = new ObjectInputStream(bis);
			e = (Event<T>) in.readObject(); 
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		
		return e;
	}

}
