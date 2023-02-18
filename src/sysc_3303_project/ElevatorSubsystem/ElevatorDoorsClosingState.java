package sysc_3303_project.ElevatorSubsystem;

import sysc_3303_project.DelayTimerThread;
import sysc_3303_project.Elevator;
import sysc_3303_project.Event;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

public class ElevatorDoorsClosingState extends ElevatorState{
    public ElevatorDoorsClosingState(Elevator context) {
        super(context);
    }

    @Override
    public void doEntry() {
        new Thread(new DelayTimerThread<>(2000,
                new Event<>(ElevatorEventType.CLOSE_DOORS_TIMER,context,null),context.getEventBuffer())).start();
    }

    @Override
    public ElevatorState closeDoorsTimer() {
        context.getSchedulerBuffer().addEvent(new Event<>(SchedulerEventType.ELEVATOR_DOORS_CLOSED,context,null));
        return new ElevatorDoorsClosedState(context);
    }
}
