package sysc_3303_project;

/**
 * The state in which the elevator is moving in a direction.
 * @author Robert Simionescu & Ian Holmes.
 */
public class ElevatorMovingState extends ElevatorState {

    private boolean moving;
    private Direction direction;

    /**
     * Stop the elevator at the next floor and transition to the ElevatorDoorsClosed state.
     * @param elevator Elevator, the context for this method.
     * @return The next state.
     */
    @Override
    public ElevatorState stopAtNextFloor(Elevator elevator) {
        elevator.moveElevator();
        System.out.println("Elevator " + elevator.getElevatorID() + " arrived at floor " + elevator.getFloor() + ".");
        return new ElevatorDoorsClosedState();
    }

    /**
     * Do not stop the elevator at the next floor and stay in the ElevatorMoving state.
     * @param elevator Elevator, the context for this method.
     * @return The next state.
     */
    @Override
    public ElevatorState doNotStopAtNextFloor(Elevator elevator) {
        elevator.moveElevator();
        return new ElevatorMovingState();
    }
}
