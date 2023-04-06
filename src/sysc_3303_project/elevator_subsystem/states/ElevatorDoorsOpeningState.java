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
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.common.configuration.Subsystem;

import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.gui_subsystem.transfer_data.DoorStatus;
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
    	context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.GUI, 0,
                Subsystem.ELEVATOR, context.getElevatorID(),
                GuiEventType.ELEVATOR_DOOR_STATUS_CHANGE, DoorStatus.DOORS_OPENING));
        context.getFaultDetector().startDoorsTimer((int) (ResourceManager.get().getInt("timing.doors") * 1.5));
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
                        ElevatorEventType.OPEN_DOORS_TIMER,
                        null));
			}
		}, ResourceManager.get().getInt("timing.doors"));
        context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.SCHEDULER,
                0,
                Subsystem.ELEVATOR,
                context.getElevatorID(),
                SchedulerEventType.ELEVATOR_PING,
                null));
    }

    /**
     * Send a message to the Scheduler that the elevator doors have opened.
     *
     * @return ElevatorDoorsOpenState, the next state
     */
    @Override
    public ElevatorState openDoorsTimer() {
        context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.SCHEDULER,
                0,
                Subsystem.ELEVATOR,
                context.getElevatorID(),
                SchedulerEventType.ELEVATOR_DOORS_OPENED,
                context.getFloor()));
        context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.GUI, 0,
                Subsystem.ELEVATOR, context.getElevatorID(),
                GuiEventType.ELEVATOR_DOORS_FAULT, false)); //sometimes redundant but low cost
        context.getDoor().setOpen();
        context.getFaultDetector().resetDoorFaultTimer();
        return new ElevatorDoorsOpenState(context);
    }

    /**
     * Retry opening the doors when fault detected.
     *
     * @return null
     */
    @Override
    public ElevatorState handleDoorsBlockedDetected() {
        Logger.getLogger().logError(context.getClass().getSimpleName(),
                "Elevator " + context.getElevatorID() + " doors are blocked!!!");
        Logger.getLogger().logNotification(context.getClass().getSimpleName(),
                "Elevator " + context.getElevatorID() + " retrying open doors...");
        context.getOutputBuffer().addEvent(new Event<>(
                Subsystem.GUI, 0,
                Subsystem.ELEVATOR, context.getElevatorID(),
                GuiEventType.ELEVATOR_DOORS_FAULT, true));
        this.doEntry();
        return null;
    }

    @Override
    public ElevatorState openDoors() {
        return null;
    }
}
