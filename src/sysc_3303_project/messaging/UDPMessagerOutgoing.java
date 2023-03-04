package sysc_3303_project.messaging;

import sysc_3303_project.common.EventBuffer;

public class UDPMessagerOutgoing implements Runnable{

	private EventBuffer<?> eventBuffer;
	
	public UDPMessagerOutgoing(EventBuffer<?> eventBuffer) {
		this.eventBuffer = eventBuffer;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
