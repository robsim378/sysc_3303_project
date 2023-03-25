package sysc_3303_project.elevator_subsystem;

import logging.Logger;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;

import java.util.Timer;
import java.util.TimerTask;

public class DoorFaultDetector {

    private final Elevator elevator;
    private final Timer doorFaultTimer;

    public DoorFaultDetector(Elevator elevator) {
        this.elevator = elevator;
        doorFaultTimer = new Timer();
    }
    
    public void startDoorsTimer(int delay) {
        Logger.getLogger().logDebug(this.getClass().getSimpleName(), "Started timer for " + delay + "ms to monitor elevator " + elevator.getElevatorID());
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

    public void resetDoorFaultTimer() {
        doorFaultTimer.cancel();
    }
}
