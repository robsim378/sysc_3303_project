/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */
package sysc_3303_project.gui_subsystem.view;

/**
 * Interface for a view
 * @author Andrei, Liam, Ian
 *
 */
public interface GuiView {
	/**
	 * Updates a floor panel with the given ID
	 * @param floorID	int, floor to update
	 */
    public void updateFloorPanel(int floorID);
    
    /**
	 * Updates an elevator panel with the given ID
     * @param elevatorID	int, elevator to update
     */
    public void updateElevatorPanel(int elevatorID);
    
    /**
     * Updates all elevator directional lamps on floors and the elevator
     * @param elevatorID		int, Elevator to update on floors
     */
    public void updateElevatorDirectionalLamps(int elevatorID);
}
