package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.*;
import sysc_3303_project.common.DelayTimerThread;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.Event;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * The state in which the elevator is moving in a direction.
 * @author Robert Simionescu & Ian Holmes.
 */
public class ElevatorMovingState extends ElevatorState {

    public ElevatorMovingState(Elevator context) {
        super(context);
    }

    @Override
    public void doEntry() {
        new Thread(new DelayTimerThread<>(2000,
                new Event<>(ElevatorEventType.MOVING_TIMER,context,null),context.getEventBuffer())).start();
    }

    @Override
    public ElevatorState travelThroughFloorsTimer() {
        context.getSchedulerBuffer().addEvent(
                new Event<>(SchedulerEventType.ELEVATOR_APPROACHING_FLOOR,context,context.getFloor() + (context.getDirection() == Direction.UP? 1 : -1)));

        return new ElevatorApproachingFloorsState(context);
    }
}
