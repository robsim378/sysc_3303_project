package sysc_3303_project.ElevatorSubsystem;

import sysc_3303_project.DelayTimerThread;
import sysc_3303_project.Elevator;
import sysc_3303_project.Event;
import sysc_3303_project.SchedulerEventType;

public class ElevatorDoorsOpeningState extends ElevatorState{

    public ElevatorDoorsOpeningState(Elevator context) {
        super(context);
    }

    @Override
    public void doEntry() {
        new Thread(new DelayTimerThread<>(2000,
                new Event<>(ElevatorEventType.OPEN_DOORS_TIMER,context,null),context.getEventBuffer())).start();
    }
    @Override
    public ElevatorState openDoorsTimer() {
        context.getSchedulerBuffer().addEvent(new Event<>(SchedulerEventType.ELEVATOR_DOORS_OPENED,context,null));
        return new ElevatorDoorsOpenState(context);
    }
}
