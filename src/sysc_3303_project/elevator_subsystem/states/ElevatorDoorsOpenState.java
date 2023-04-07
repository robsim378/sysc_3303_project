/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.elevator_subsystem.states;

import logging.Logger;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.DelayTimerThread;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.gui_subsystem.transfer_data.DoorStatus;

/**
 * The state in which the elevator is idle with its doors open.
 * @author Robert Simionescu & Ian Holmes.
 */
public class ElevatorDoorsOpenState extends ElevatorState {

    /**
     * Constructor for the elevator state.
     *
     * @param context Elevator, the elevator
     */
	
	private boolean isLoading = false;
	private boolean closeDoorsRequested = false;
	
    public ElevatorDoorsOpenState(Elevator context) {
        super(context);
    }
    
    @Override
    public void doEntry() {
    	isLoading = true;
    	context.getOutputBuffer().addEvent(new Event<>(
            Subsystem.GUI, 0,
            Subsystem.ELEVATOR, context.getElevatorID(),
            GuiEventType.ELEVATOR_DOOR_STATUS_CHANGE, DoorStatus.DOORS_OPEN));
    	new Thread(new DelayTimerThread<>(ResourceManager.get().getInt("timing.load"),
            new Event<>(
                    Subsystem.ELEVATOR,
                    context.getElevatorID(),
                    Subsystem.ELEVATOR,
                    context.getElevatorID(),
                    ElevatorEventType.CLOSE_DOORS_TIMER,
                    null),
            context.getInputBuffer())).start();
    }
    
    @Override
    public ElevatorState closeDoorsTimer() {
    	Logger.getLogger().logNotification(this.getClass().getSimpleName(), "No longer loading/unloading");
        isLoading = false;
        return closeDoorsRequested ? closeDoors() : null;
    }

    /**
     * Close the elevator doors and move to the ElevatorDoorsClosed state.
     *
     * @return ElevatorDoorsClosingState, the next state
     */
    @Override
    public ElevatorState closeDoors() {
    	if (isLoading) {
    		closeDoorsRequested = true;
    		return null;
    	}
        Logger.getLogger().logDebug(this.getClass().getSimpleName(), "closeDoors() called");
        return new ElevatorDoorsClosingState(context);
    }

    /**
     * handle the unloading of passengers. ******PLACEHOLDER
     *
     * @return null
     */
    @Override
    public ElevatorState handlePassengersUnloaded() {
        return null;
    }
}
