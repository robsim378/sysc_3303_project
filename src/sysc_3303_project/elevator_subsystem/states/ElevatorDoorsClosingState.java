/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.common.events.DelayTimerThread;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.configuration.Subsystem;

import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * State for the elevator doors closing.
 *
 * @author Ian Holmes
 */
public class ElevatorDoorsClosingState extends ElevatorState {

    /**
     * Constructor for the elevator state.
     *
     * @param context Elevator, the elevator
     */
    public ElevatorDoorsClosingState(Elevator context) {
        super(context);
    }

    /**
     * Do an action on entry to the state. Start a timer to represent the elevator doors closing.
     */
    @Override
    public void doEntry() {
        new Thread(new DelayTimerThread<>(1000,
                new Event<>(Subsystem.ELEVATOR,
                        context.getElevatorID(),
                        Subsystem.ELEVATOR,
                        context.getElevatorID(),
                        ElevatorEventType.CLOSE_DOORS_TIMER,
                        null),
                context.getInputBuffer())).start();
    }

    /**
     * Send a message to the Scheduler that the elevator doors have closed.
     *
     * @return ElevatorDoorsClosedState, the next state
     */
    @Override
    public ElevatorState closeDoorsTimer() {
        context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.SCHEDULER,
                0,
                Subsystem.ELEVATOR,
                context.getElevatorID(),
                SchedulerEventType.ELEVATOR_DOORS_CLOSED,
                context.getFloor()));
        return new ElevatorDoorsClosedState(context);
    }
}
