package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.common.Event;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

public class ElevatorApproachingFloorsState extends ElevatorState {
    public ElevatorApproachingFloorsState(Elevator context) {
        super(context);
    }

    /**
     * Stop the elevator at the next floor and transition to the ElevatorDoorsClosed state.
     * @return The next state.
     */
    @Override
    public ElevatorState stopAtNextFloor() {
        context.moveElevator();
        context.getSchedulerBuffer().addEvent(new Event<>(SchedulerEventType.ELEVATOR_STOPPED,context, context.getFloor()));
        return new ElevatorDoorsClosedState(context);
    }

    /**
     * Do not stop the elevator at the next floor and stay in the ElevatorMoving state.
     * @return The next state.
     */
    @Override
    public ElevatorState continueMoving() {
        context.moveElevator();
        return new ElevatorMovingState(super.context);
    }
}
