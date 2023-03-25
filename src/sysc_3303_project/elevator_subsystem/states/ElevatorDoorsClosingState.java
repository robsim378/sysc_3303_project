/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.elevator_subsystem.states;

import java.util.Timer;
import java.util.TimerTask;

import logging.Logger;
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
    	context.getFaultDetector().startDoorsTimer(1000);
    	Timer timer = new Timer();
    	timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if (context.getBlockedDoorsCounter() > 0) {
		            context.decrementBlockedDoorsCounter();
		            return;
		        }
				context.getInputBuffer().addEvent(new Event<>(
	                  Subsystem.ELEVATOR,
	                  context.getElevatorID(),
	                  Subsystem.ELEVATOR,
	                  context.getElevatorID(),
	                  ElevatorEventType.CLOSE_DOORS_TIMER,
	                  null));
			}
		}, 1000);
        context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.SCHEDULER,
                0,
                Subsystem.ELEVATOR,
                context.getElevatorID(),
                SchedulerEventType.ELEVATOR_PING,
                null));
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
        context.getDoor().setClosed();
        context.getFaultDetector().resetDoorFaultTimer();
        return new ElevatorDoorsClosedState(context);
    }

    /**
     * Retry closing the doors when fault detected.
     *
     * @return null
     */
    @Override
    public ElevatorState handleDoorsBlockedDetected() {
        Logger.getLogger().logError(context.getClass().getSimpleName(),
                "Elevator " + context.getElevatorID() + " doors are blocked!!!");
        Logger.getLogger().logNotification(context.getClass().getSimpleName(),
                "Elevator " + context.getElevatorID() + " retrying close doors...");
        this.doEntry();
        return null;
    }

    @Override
    public ElevatorState closeDoors() {
        return null;
    }
}
