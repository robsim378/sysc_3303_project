/**
 * 
 */
package sysc_3303_project.ui_subsystem;

import java.io.Serializable;

/**
 * @author apope
 *
 */
public class ElevatorLampStatus implements Serializable {
	
	private final int floorNumber;
	private final boolean status;
	
	public ElevatorLampStatus(int floorNumber, boolean status) {
		this.floorNumber = floorNumber;
		this.status = status;
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}
	
	public boolean getStatus() {
		return status;
	}
}
