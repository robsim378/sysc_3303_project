/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.elevator_subsystem.states;

import sysc_3303_project.common.Direction;

import sysc_3303_project.common.events.DelayTimerThread;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.configuration.Subsystem;

import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * The state in which the elevator is moving in a direction.
 * @author Robert Simionescu & Ian Holmes.
 */
public class ElevatorMovingState extends ElevatorState {

    /**
     * Constructor for the elevator state.
     *
     * @param context Elevator, the elevator
     */
    public ElevatorMovingState(Elevator context) {
        super(context);
    }

    /**
     * Do an action on entry to the state. Start a timer to represent the elevator moving.
     */
    @Override
    public void doEntry() {
        new Thread(new DelayTimerThread<>(1000,
                new Event<>(
                        Subsystem.ELEVATOR,
                        context.getElevatorID(),
                        Subsystem.ELEVATOR,
                        context.getElevatorID(),
                        ElevatorEventType.MOVING_TIMER,
                        null),
                context.getInputBuffer())).start();

    }

    /**
     * Send a message to the Scheduler that the elevator is approaching a floor.
     *
     * @return ElevatorApproachingFloorsState, the next state
     */
    @Override
    public ElevatorState travelThroughFloorsTimer() {
        context.getOutputBuffer().addEvent(
                new Event<>(
                        Subsystem.SCHEDULER,
                        0,
                        Subsystem.ELEVATOR,
                        context.getElevatorID(),
                        SchedulerEventType.ELEVATOR_APPROACHING_FLOOR,
                        context.getFloor() + (context.getDirection() == Direction.UP? 1 : -1)));

        return new ElevatorApproachingFloorsState(context);
    }
}
