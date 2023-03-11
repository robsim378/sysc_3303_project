package sysc_3303_project.common.messaging;

import java.net.InetAddress;

import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.common.configuration.Subsystem;

public abstract class UDPMessager {
	
	ResourceManager resourceManager = ResourceManager.getResourceManager();
	
	public int getPort(Subsystem destination) {
		try {
			return resourceManager.getInt(destination.toString() + ".port");
		} catch (Exception e) {
			return -1;
		}
	}
	
	public InetAddress getHost(Subsystem destination) {
		try {
			return InetAddress.getByName(resourceManager.get(destination.toString() + ".hostname"));
		} catch (Exception e) {
			return null;
		}
	}

}
