/**
 * 
 */
package sysc_3303_project.scheduler_subsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import logging.Logger;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;

/**
 * @author Andrei Popescu
 *
 */
public class ElevatorFaultDetector {
	
	private class ElevatorMoveTimer {
		public Timer timer;
		public int elevatorId;
		
		private ElevatorMoveTimer(Timer timer, int elevatorId) {
			this.timer = timer;
			this.elevatorId = elevatorId;
		}
	}
	
	private List<ElevatorMoveTimer> elevatorMoveTimers;
	private Scheduler scheduler;
	
	public ElevatorFaultDetector(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.elevatorMoveTimers = new LinkedList<>();
	}
	
	public void addTimer(int elevatorId, int delay) {
		Timer timer = new Timer();
		elevatorMoveTimers.add(new ElevatorMoveTimer(timer, elevatorId));
		Logger.getLogger().logDebug(this.getClass().getSimpleName(), "Started timer for " + delay + "ms to monitor elevator " + elevatorId);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				scheduler.getInputBuffer().addEvent(new Event<SchedulerEventType>(Subsystem.SCHEDULER, 0,
						Subsystem.SCHEDULER, 0,
						SchedulerEventType.ELEVATOR_BLOCKED, elevatorId));
			}
		}, (long) (delay*1.5)); //replace 1000 with elevator move timer value once we get that into the resource manager
	}
	
	public void clearTimers(int elevatorId) {
		Logger.getLogger().logDebug(this.getClass().getSimpleName(), "Elevator " + elevatorId + " is active, cancelling its timer(s).");
		List<ElevatorMoveTimer> toRemove = new LinkedList<>();
		for (ElevatorMoveTimer timer : elevatorMoveTimers) {
			if (timer.elevatorId == elevatorId) {
				toRemove.add(timer);
				timer.timer.cancel();
			}
		}
		for (ElevatorMoveTimer timer : toRemove) {
			elevatorMoveTimers.remove(timer);
		}
	}
}
