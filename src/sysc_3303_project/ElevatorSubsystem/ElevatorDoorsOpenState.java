/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.ElevatorSubsystem;

/**
 * The state in which the elevator is idle with its doors open.
 * @author Robert Simionescu & Ian Holmes.
 */
public class ElevatorDoorsOpenState extends ElevatorState {

    /**
     * Constructor for the state.
     *
     * @param context, the elevator
     */
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
