package sysc_3303_project.messaging;

import sysc_3303_project.common.EventBuffer;

public class UDPMessagerOutgoing {

	private EventBuffer<?> eventBuffer;
	
	public UDPMessagerOutgoing(EventBuffer<?> eventBuffer) {
		this.eventBuffer = eventBuffer;
	}
}
