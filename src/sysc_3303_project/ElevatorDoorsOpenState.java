package sysc_3303_project;

/**
 * The state in which the elevator is idle with its doors open.
 * @author Robert Simionescu & Ian Holmes.
 */
public class ElevatorDoorsOpenState extends ElevatorState {

    /**
     * Close the elevator doors and move to the ElevatorDoorsClosed state.
     * @param elevator Elevator, the context for this method.
     * @return The next state.
     */
    @Override
    public ElevatorState closeDoors(Elevator elevator) {
        System.out.println("Elevator " + elevator.getElevatorID() + " opening doors.");
        // Delay a bit here
        System.out.println("Elevator " + elevator.getElevatorID() + " doors open.");

        return new ElevatorDoorsClosedState();
    }
}
