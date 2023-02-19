/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.common.Event;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * State for the elevator approaching a floor.
 *
 * @author Ian Holmes
 */
public class ElevatorApproachingFloorsState extends ElevatorState {

    /**
     * Constructor for the elevator state.
     *
     * @param context Elevator, the elevator
     */
    public ElevatorApproachingFloorsState(Elevator context) {
        super(context);
    }

    /**
     * Stop the elevator at the next floor and transition to the ElevatorDoorsClosed state.
     *
     * @return ElevatorDoorsClosedState, the next state.
     */
    @Override
    public ElevatorState stopAtNextFloor() {
        context.moveElevator();
        context.getSchedulerBuffer().addEvent(new Event<>(SchedulerEventType.ELEVATOR_STOPPED,context, context.getFloor()));
        return new ElevatorDoorsClosedState(context);
    }

    /**
     * Do not stop the elevator at the next floor and stay in the ElevatorMoving state.
     *
     * @return ElevatorMovingState, the next state
     */
    @Override
    public ElevatorState continueMoving() {
        context.moveElevator();
        return new ElevatorMovingState(super.context);
    }
}
