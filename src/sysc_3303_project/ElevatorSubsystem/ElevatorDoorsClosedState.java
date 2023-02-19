/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.ElevatorSubsystem;

import sysc_3303_project.Direction;

/**
 * The state in which the elevator is idle with its doors closed.
 *
 * @author Robert Simionescu & Ian Holmes
 */
public class ElevatorDoorsClosedState extends ElevatorState {

    /**
     * Constructor for the state.
     *
     * @param context, the elevator
     */
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
     *
     * @return ElevatorMovingState, the next state.
     */
    @Override
    public ElevatorState setDirection(Direction direction) {
        context.setDirection(direction);
        return new ElevatorMovingState(context);
    }

}
