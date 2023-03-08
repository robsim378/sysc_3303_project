package sysc_3303_project.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.Subsystem;
import sysc_3303_project.common.SystemProperties;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;

public class UDPMessagerOutgoing implements Runnable{

	private EventBuffer<?> eventBuffer;
	
	public UDPMessagerOutgoing(EventBuffer<?> eventBuffer) {
		this.eventBuffer = eventBuffer;
	}

	@Override
	public void run() {
		
		while(true) {
			Event<?> e = eventBuffer.getEvent();
			
			System.out.println(e.toString());
			
			int port = getPort(e.getDestinationSubsystem());
			
			byte[] msg = generateByteArray(e);
			
			DatagramSocket sendSocket = generateDatagramSocket();
			
			DatagramPacket sendPacket = generateDatagramPacket(msg, port);
			
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			
			sendSocket.close();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	private int getPort(Subsystem destinationSubsystem) {
		switch(destinationSubsystem) {
		case ELEVATOR:
			return SystemProperties.ELEVATOR_PORT;
		case FLOOR:
			return SystemProperties.FLOOR_PORT;
		case SCHEDULER:
			return SystemProperties.SCHEDULER_PORT;
		}
		return -1;
	}

	private DatagramSocket generateDatagramSocket() {
		try {
			return new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return null;
	}

	private byte[] generateByteArray(Event<?> e) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		byte[] sendBytes = null;
		try {
			out = new ObjectOutputStream(bos);   
			out.writeObject(e);
			out.flush();
			sendBytes = bos.toByteArray();
		  			  
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		
		return sendBytes;
	}

	public DatagramPacket generateDatagramPacket(byte[] msg, int port) {
		try {
			return new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	
	/**
	 * Test method to ensure transport is proper
	 * @param args
	 */
    public static void main(String[] args) {
		EventBuffer<ElevatorEventType> buffer = new EventBuffer<ElevatorEventType>();
		
		buffer.addEvent(new Event<ElevatorEventType>(Subsystem.ELEVATOR, 0, Subsystem.FLOOR, 0, ElevatorEventType.CLOSE_DOORS, Direction.UP));
		
		ArrayList<EventBuffer<ElevatorEventType>> list = new ArrayList<EventBuffer<ElevatorEventType>>();
		list.add(buffer);
		Thread input = new Thread(new UDPMessagerIncoming<ElevatorEventType>(list, SystemProperties.ELEVATOR_PORT));

		Thread output = new Thread(new UDPMessagerOutgoing(buffer));
		input.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output.start();
		
	}
	
}
