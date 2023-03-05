package sysc_3303_project.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;

public class UDPMessagerIncoming<T extends Enum<?>> implements Runnable{
	
	private List<EventBuffer<T>> eventBuffers;
	
	private int listeningPort;
	
	DatagramSocket sendRecieveSocket = null;
	
	public UDPMessagerIncoming(List<EventBuffer<T>> eventBuffers, int listeningPort) {
		this.eventBuffers = eventBuffers;
		this.listeningPort = listeningPort;
		try {
			sendRecieveSocket = new DatagramSocket(this.listeningPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		while(true) {
			byte[] msg = new byte[500];
			DatagramPacket packet = new DatagramPacket(msg, 500);
			
			try {
				sendRecieveSocket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
			ObjectInput in = null;
			Event<T> e = null;
			
			try {
				in = new ObjectInputStream(bis);
				e = (Event<T>) in.readObject(); 
			} catch (Exception e1) {
				// TODO Auto-generated catch block
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
			System.out.println(e.toString());
			eventBuffers.get(e.getDestinationID()).addEvent(e);
		}
	}

}
