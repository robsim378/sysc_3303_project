/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.elevator_subsystem.states;

import logging.Logger;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.ui_subsystem.DoorStatus;
import sysc_3303_project.ui_subsystem.GuiEventType;

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
    public ElevatorDoorsOpenState(Elevator context) {
        super(context);
    }
    
    @Override
    public void doEntry() {
    	context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.GUI, 0,
                Subsystem.ELEVATOR, context.getElevatorID(),
                GuiEventType.ELEVATOR_DOOR_STATUS_CHANGE, DoorStatus.DOORS_OPEN));
    }

    /**
     * Close the elevator doors and move to the ElevatorDoorsClosed state.
     *
     * @return ElevatorDoorsClosingState, the next state
     */
    @Override
    public ElevatorState closeDoors() {
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
