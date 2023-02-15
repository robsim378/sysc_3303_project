package sysc_3303_project;

/**
 * The state in which the elevator is idle with its doors closed.
 * @author Robert Simionescu & Ian Holmes
 */
public class ElevatorDoorsClosedState extends ElevatorState {

    /**
     * Open the elevator doors and move to the ElevatorDoorsOpen state.
     * @param elevator Elevator, the context for this method.
     * @return The next state.
     */
    @Override
    public ElevatorState openDoors(Elevator elevator) {
        System.out.println("Elevator " + elevator.getElevatorID() + " opening doors.");
        // Delay a bit here
        System.out.println("Elevator " + elevator.getElevatorID() + " doors open.");
        return new ElevatorDoorsOpenState();
    }

    /**
     * Set the direction of the elevator's movement and begin moving by going to the ElevatorMoving state.
     * @param elevator Elevator, the context for this method.
     * @return The next state.
     */
    @Override
    public ElevatorState setDirection(Elevator elevator, Direction direction) {
        System.out.println("Elevator " + elevator.getElevatorID() + " moving " + direction);
        elevator.setDirection(direction);
        return new ElevatorMovingState();
    }

}
