/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.common.Direction;

import sysc_3303_project.common.state.State;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;

import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.gui_subsystem.transfer_data.ElevatorLampStatus;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * State class for the elevator state machine.
 *
 * @author Robert Simionescu & Ian Holmes
 */
public abstract class ElevatorState implements State {

    /**
     * All methods for all states are declared here and throw exceptions unless they are called from a valid state in
     * which they are overridden.
     */

    protected Elevator context;

    public ElevatorState(Elevator context) {
        this.context = context;
    }

    public ElevatorState closeDoors() {
        throw new IllegalStateException("closeDoors() must be called from the ElevatorDoorsOpenState.");
    }

    public ElevatorState openDoors() {
        throw new IllegalStateException("openDoors() must be called from the ElevatorDoorsClosedState.");
    }

    public ElevatorState setDirection(Direction direction) {
        throw new IllegalStateException("setDirection() must be called from the ElevatorDoorsClosedState.");
    }

    public ElevatorState stopAtNextFloor() {
        throw new IllegalStateException("stopAtNextFloor() must be called from the ElevatorApproachingFloorsState.");
    }

    public ElevatorState continueMoving() {
        throw new IllegalStateException("continueMoving() must be called from the ElevatorApproachingFloorsState.");
    }

    public ElevatorState openDoorsTimer() {
        throw new IllegalStateException("openDoorsTimer() must be called from the ElevatorDoorsOpeningState.");
    }

    public ElevatorState closeDoorsTimer() {
        throw new IllegalStateException("closeDoorsTimer() must be called from the ElevatorDoorsClosingState.");
    }

    public ElevatorState travelThroughFloorsTimer() {
        throw new IllegalStateException("travelThroughFloorsTimer() must be called from the ElevatorMovingState.");
    }

    public ElevatorState handlePassengersUnloaded() {
        throw new IllegalStateException("handlePassengersUnloaded must be called from the ElevatorDoorsOpenState.");
    }
    
    public ElevatorState handleSetIdle() {
    	context.setDirection(null);
		return null;
    }

    /**
     * When an elevator button is pressed during any state, send an event to the Scheduler.
     *
     * @param destination int, the destination
     * @return ElevatorState, next state
     */
    public ElevatorState handleElevatorButtonPressed(int destination) {
    	context.getButtonLamps()[destination].turnOn();
        context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.GUI, 0,
                Subsystem.ELEVATOR, context.getElevatorID(),
                GuiEventType.ELEVATOR_LAMP_STATUS_CHANGE, new ElevatorLampStatus(destination, true)));
        context.getOutputBuffer().addEvent(
                new Event<Enum<?>>(
                        Subsystem.SCHEDULER,
                        0,
                        Subsystem.ELEVATOR,
                        context.getElevatorID(),
                        SchedulerEventType.ELEVATOR_BUTTON_PRESSED,
                        destination));
        context.getButtons()[context.getFloor()].pushButton();
        return null;
    }

    /**
     * Handle a block doors event from the floor.
     *
     * @return null
     */
    public ElevatorState handleDoorsBlocked() {
        context.incrementBlockedDoorsCounter();
        return null;
    }

    /**
     * Default for detected doors blocked handler.
     *
     * @return null
     */
    public ElevatorState handleDoorsBlockedDetected() {
        return null;
    }

    public void doEntry() {}

    public void doExit() {}
}
