/**
 * 
 */
package sysc_3303_project.ui_subsystem;

/**
 * @author apope
 *
 */
public class ElevatorLampStatus {
	
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
