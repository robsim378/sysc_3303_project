package sysc_3303_project.ElevatorSubsystem;

import sysc_3303_project.*;

/**
 * The state in which the elevator is moving in a direction.
 * @author Robert Simionescu & Ian Holmes.
 */
public class ElevatorMovingState extends ElevatorState {

    private boolean moving;
    private Direction direction;

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
                new Event<>(SchedulerEventType.ELEVATOR_APPROACHING_FLOOR,context,context.getFloor()));

        return new ElevatorApproachingFloorsState(context);
    }
}
