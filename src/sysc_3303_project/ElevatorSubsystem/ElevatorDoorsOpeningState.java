/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.ElevatorSubsystem;

import sysc_3303_project.DelayTimerThread;
import sysc_3303_project.Event;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * A state for the elevator Doors Opening.
 *
 * @author Ian Holmes
 */
public class ElevatorDoorsOpeningState extends ElevatorState {

    /**
     * Constructor for the state.
     *
     * @param context, the elevator
     */
    public ElevatorDoorsOpeningState(Elevator context) {
        super(context);
    }

    /**
     * Action to be performed on entry to the state. Start a timer to represent doors opening.
     */
    @Override
    public void doEntry() {
        new Thread(new DelayTimerThread<>(2000,
                new Event<>(ElevatorEventType.OPEN_DOORS_TIMER,context,null),context.getEventBuffer())).start();
    }

    /**
     * Open the elevator doors. Send Scheduler a message and go to next state.
     *
     * @return ElevatorDoorsClosedState, the next state
     */
    @Override
    public ElevatorState openDoorsTimer() {
        context.getSchedulerBuffer().addEvent(new Event<>(SchedulerEventType.ELEVATOR_DOORS_OPENED,context,null));
        return new ElevatorDoorsOpenState(context);
    }
}
