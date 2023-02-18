package sysc_3303_project.ElevatorSubsystem;

import sysc_3303_project.Direction;
import sysc_3303_project.State;

/**
 * State class for the elevator state machine.
 * @author Robert Simionescu & Ian Holmes.
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

    /**
     * NOTES FOR IAN
     * -Currently, all states return a new instance of the next state when they finish a method. This doesn't feel right,
     * but I'm not sure how else to do it.
     * -I added a few getters/setters in Elevator and made all the state methods take an Elevator as a parameter. This
     * should work, but I feel like there's a more elegant way of doing this.
     *      -I also made moveElevator public, which feels wrong but I couldn't think of a better way. Maybe move it to
     *      ElevatorMovingState and add a getter/setter for floor
     */

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
        throw new IllegalStateException("stopAtNextFloor() must be called from the ElevatorMovingState.");
    }

    public ElevatorState continueMoving() {
        throw new IllegalStateException("continueMoving() must be called from the ElevatorApproachingState.");
    }

    public ElevatorState openDoorsTimer() {
        throw new IllegalStateException("openDoorsTimer() must be called from the _________________.");
    }

    public ElevatorState closeDoorsTimer() {
        throw new IllegalStateException("closeDoorsTimer() must be called from the _________________.");
    }

    public ElevatorState travelThroughFloorsTimer() {
        throw new IllegalStateException("travelThroughFloorsTimer() must be called from the _________________.");
    }

    public void doEntry() {}

    public void doExit() {}
}
