package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.elevator_subsystem.Elevator;

/**
 * The state in which the elevator is idle with its doors open.
 * @author Robert Simionescu & Ian Holmes.
 */
public class ElevatorDoorsOpenState extends ElevatorState {

    public ElevatorDoorsOpenState(Elevator context) {
        super(context);
    }

    /**
     * Close the elevator doors and move to the ElevatorDoorsClosed state.
     * @return The next state.
     */
    @Override
    public ElevatorState closeDoors() {
        return new ElevatorDoorsClosingState(context);
    }
}
