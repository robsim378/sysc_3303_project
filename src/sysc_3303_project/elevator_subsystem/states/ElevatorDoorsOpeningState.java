/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.common.DelayTimerThread;
import sysc_3303_project.common.Event;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

public class ElevatorDoorsOpeningState extends ElevatorState{

    /**
     * Constructor for the elevator state.
     *
     * @param context Elevator, the elevator
     */
    public ElevatorDoorsOpeningState(Elevator context) {
        super(context);
    }

    /**
     * Do an action on entry to the state. Start a timer to represent the elevator doors opening.
     */
    @Override
    public void doEntry() {
        new Thread(new DelayTimerThread<>(2000,
                new Event<>(ElevatorEventType.OPEN_DOORS_TIMER,context,null),context.getEventBuffer())).start();
    }

    /**
     * Send a message to the Scheduler that the elevator doors have opened.
     *
     * @return ElevatorDoorsOpenState, the next state
     */
    @Override
    public ElevatorState openDoorsTimer() {
        context.getSchedulerBuffer().addEvent(new Event<>(SchedulerEventType.ELEVATOR_DOORS_OPENED,context,null));
        return new ElevatorDoorsOpenState(context);
    }
}
