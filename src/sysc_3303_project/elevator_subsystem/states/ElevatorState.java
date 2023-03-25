/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.elevator_subsystem.states;

import logging.Logger;
import sysc_3303_project.common.Direction;

import sysc_3303_project.common.state.State;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;

import sysc_3303_project.elevator_subsystem.Elevator;
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

    /**
     * When an elevator button is pressed during any state, send an event to the Scheduler.
     *
     * @param destination int, the destination
     * @return ElevatorState, next state
     */
    public ElevatorState handleElevatorButtonPressed(int destination) {
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

    public ElevatorState handleDoorsBlocked(boolean openClose) {
        Logger.getLogger().logError(context.getClass().getSimpleName(), "Elevator " + context.getElevatorID() + " doors are blocked!!!");

        if (openClose) {
            this.openDoors();
        } else {
            this.closeDoors();
        }
        return null;
    }

    public void doEntry() {}

    public void doExit() {}
}
