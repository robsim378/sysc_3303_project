package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.common.Direction;
import sysc_3303_project.elevator_subsystem.Elevator;

/**
 * The state in which the elevator is idle with its doors closed.
 * @author Robert Simionescu & Ian Holmes
 */
public class ElevatorDoorsClosedState extends ElevatorState {

    public ElevatorDoorsClosedState(Elevator context) {
        super(context);
    }

    /**
     * Open the elevator doors and move to the ElevatorDoorsOpen state.
     * @return The next state.
     */
    @Override
    public ElevatorState openDoors() {
        return new ElevatorDoorsOpeningState(context);
    }

    /**
     * Set the direction of the elevator's movement and begin moving by going to the ElevatorMoving state.
     * @return The next state.
     */
    @Override
    public ElevatorState setDirection(Direction direction) {
        context.setDirection(direction);
        return new ElevatorMovingState(context);
    }

}