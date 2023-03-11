package sysc_3303_project.common.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import logging.Logger;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.configuration.SystemProperties;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;

public class UDPMessagerOutgoing extends UDPMessager implements Runnable {

	private EventBuffer<?> eventBuffer;
	
	/**
	 * Constructor for class
	 * @param eventBuffer		EventBuffer<?>, buffer to pull data from
	 */
	public UDPMessagerOutgoing(EventBuffer<?> eventBuffer) {
		this.eventBuffer = eventBuffer;
	}

	/**
	 * Loops between sending out any outwards facing data requests
	 */
	@Override
	public void run() {
		
		// Infinite looping
		while(true) {
			
			//Get event
			Event<?> ev = eventBuffer.getEvent();
			
			Logger.getLogger().logNotification(this.getClass().toString(), ev.toString());
			
			// Build data to send
			int port = getPort(ev.getDestinationSubsystem());
			
			InetAddress addr = getHost(ev.getDestinationSubsystem());
			
			byte[] msg = generateByteArray(ev);
			
			DatagramSocket sendSocket = generateDatagramSocket();
			
			DatagramPacket sendPacket = generateDatagramPacket(msg, addr, port);
			
			// Send data
			try {
				sendSocket.send(sendPacket);
				Logger.getLogger().logNotification(this.getClass().toString(), "Sent datagram packet to " + ev.getDestinationSubsystem().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger().logError(this.getClass().toString(), e.getMessage());
			}			
			
			sendSocket.close();
		}
	}
	
	/**
	 * Generates a datagram socket to use for sending data
	 * @return		DatagramSocket, socket to use
	 */
	private DatagramSocket generateDatagramSocket() {
		try {
			return new DatagramSocket();
		} catch (SocketException e) {
			Logger.getLogger().logError(this.getClass().toString(), e.getMessage());
		}

		return null;
	}

	/**
	 * Generates a byte array from the event parameters
	 * @param ev		Event, event to byte-ify
	 * @return		Byte[], serlialized version of the event
	 */
	private byte[] generateByteArray(Event<?> ev) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		byte[] sendBytes = null;
		try {
			out = new ObjectOutputStream(bos);   
			out.writeObject(ev);
			out.flush();
			sendBytes = bos.toByteArray();
		  			  
		} catch (IOException e) {
			Logger.getLogger().logError(this.getClass().toString(), e.getMessage());
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				Logger.getLogger().logError(this.getClass().toString(), e.getMessage());
			}
		}
		
		return sendBytes;
	}

	/**
	 * Generates the datagramPacket from the nessesary parameters
	 * @param msg		byte[], message to send
	 * @param addr		InetAddress, host destination
	 * @param port		int, port to send to
	 * @return			DatagramPacket, packet to send
	 */
	public DatagramPacket generateDatagramPacket(byte[] msg, InetAddress addr, int port) {
		return new DatagramPacket(msg, msg.length, addr, port);
	}
	
	/**
	 * Turns a byte array into a visually satisfying string array.
	 * @param byteArray		byte[], the byte array to convert
	 * @param arrayLength	int, the length of the contents within the byte array
	 * @return				String, byte array converted to a string representation
	 */
	@SuppressWarnings("unused")
	private String stringifyByteArray(byte[] byteArray, int arrayLength) {
		StringBuffer byteBuffer = new StringBuffer();
		
		byteBuffer.append("[");
		for(int i = 0; i < arrayLength; i++) {
			byteBuffer.append(byteArray[i]);
			if(i != (arrayLength-1)) {
				byteBuffer.append(",");
			}
		}
		byteBuffer.append("]");
		return byteBuffer.toString();
	}	
}
