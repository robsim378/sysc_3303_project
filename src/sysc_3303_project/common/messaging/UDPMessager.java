/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.common.messaging;

import java.net.InetAddress;

import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.common.configuration.Subsystem;

/**
 * Abstract class containing common methods for the messaging
 * @author Liam
 *
 */
public abstract class UDPMessager {
	
	ResourceManager resourceManager = ResourceManager.getResourceManager();
	
	/**
	 * Determines port from destination
	 * @param destination	Subsystem, system to send to
	 * @return				int, port to send to
	 */
	public int getPort(Subsystem destination) {
		try {
			return resourceManager.getInt(destination.toString() + ".port");
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * Determines host from destination
	 * @param destination	Subsystem, system to send to
	 * @return				InetAddress, host to send to
	 */
	public InetAddress getHost(Subsystem destination) {
		try {
			return InetAddress.getByName(resourceManager.get(destination.toString() + ".hostname"));
		} catch (Exception e) {
			return null;
		}
	}

}
