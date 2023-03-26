/**
 * SYSC3303 Project
 * Group 1
 * @version 4.0
 */

package sysc_3303_project.elevator_subsystem;

import logging.Logger;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Detector for faults with Elevator doors opening or closing.
 *
 * @author Ian Holmes
 */
public class DoorFaultDetector {

    private final Elevator elevator;
    private Timer doorFaultTimer;

    /**
     * Constructor for the fault detector.
     *
     * @param elevator Elevator, the elevator
     */
    public DoorFaultDetector(Elevator elevator) {
        this.elevator = elevator;
        doorFaultTimer = new Timer();
    }

    /**
     * Start the timer to detect a door fault.
     *
     * @param delay
     */
    public void startDoorsTimer(int delay) {
        Logger.getLogger().logDebug(this.getClass().getSimpleName(), "Started timer for " + delay + "ms to monitor elevator " + elevator.getElevatorID());
        doorFaultTimer = new Timer();
        doorFaultTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                elevator.getInputBuffer().addEvent(new Event<>(
                        Subsystem.ELEVATOR, elevator.getElevatorID(),
                        Subsystem.ELEVATOR, elevator.getElevatorID(),
                        ElevatorEventType.BLOCKED_DOORS_DETECTED, null
                ));
            }
        }, (long) (delay*1.5)); //replace 1000 with elevator doors closing/opening timer value once we get that into the resource manager
    }

    /**
     * Cancel the timer after the doors successfully open or close.
     */
    public void resetDoorFaultTimer() {
        doorFaultTimer.cancel();
    }
}
