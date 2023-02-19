/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.State;
import sysc_3303_project.elevator_subsystem.Elevator;

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

    public void doEntry() {}

    public void doExit() {}
}
