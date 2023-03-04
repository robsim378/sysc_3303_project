package sysc_3303_project.messaging;

import java.util.List;

import sysc_3303_project.common.EventBuffer;

public class UDPMessagerIncoming<T> implements Runnable{
	
	private List<EventBuffer<T>> eventBuffers;
	
	public UDPMessagerIncoming(List<EventBuffer<T>> eventBuffers) {
		this.eventBuffers = eventBuffers;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
