package sysc_3303_project;

/**
 * State class for the elevator state machine.
 * @author Robert Simionescu & Ian Holmes.
 */
public abstract class ElevatorState {
    /**
     * All methods for all states are declared here and throw exceptions unless they are called from a valid state in
     * which they are overridden.
     */

    /**
     * NOTES FOR IAN
     * -Currently, all states return a new instance of the next state when they finish a method. This doesn't feel right,
     * but I'm not sure how else to do it.
     * -I added a few getters/setters in Elevator and made all the state methods take an Elevator as a parameter. This
     * should work, but I feel like there's a more elegant way of doing this.
     *      -I also made moveElevator public, which feels wrong but I couldn't think of a better way. Maybe move it to
     *      ElevatorMovingState and add a getter/setter for floor
     */

    public ElevatorState closeDoors(Elevator elevator) {
        throw new IllegalStateException("closeDoors() must be called from the ElevatorDoorsOpenState.");
    }

    public ElevatorState openDoors(Elevator elevator) {
        throw new IllegalStateException("openDoors() must be called from the ElevatorDoorsClosedState.");
    }

    public ElevatorState setDirection(Elevator elevator, Direction direction) {
        throw new IllegalStateException("setDirection() must be called from the ElevatorDoorsClosedState.");
    }

    public ElevatorState stopAtNextFloor(Elevator elevator) {
        throw new IllegalStateException("stopAtNextFloor() must be called from the ElevatorMovingState.");
    }

    public ElevatorState doNotStopAtNextFloor(Elevator elevator) {
        throw new IllegalStateException("doNotStopAtNextFloor() must be called from the ElevatorMovingState.");
    }


}
