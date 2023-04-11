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
	
	/**
	 * Timer for fault detection
	 * @author Andrei
	 *
	 */
	public class ElevatorMoveTimer {
		public Timer timer;
		public int elevatorId;
		
		/**
		 * Constructor
		 * @param timer
		 * @param elevatorId
		 */
		private ElevatorMoveTimer(Timer timer, int elevatorId) {
			this.timer = timer;
			this.elevatorId = elevatorId;
		}

		/**
		 * Getter for ID
		 */
		public int getElevatorId() {
			return elevatorId;
		}
	}

	// List of timers to hold onto
	private List<ElevatorMoveTimer> elevatorMoveTimers;
	
	// Reference to the scheduler
	private Scheduler scheduler;
	
	/**
	 * Constructor for fault detector
	 * @param scheduler	Scheduler, reference to the context
	 */
	public ElevatorFaultDetector(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.elevatorMoveTimers = new LinkedList<>();
	}
	
	/**
	 * Adds a timer to check for
	 * @param elevatorId	int, ID to check for
	 * @param delay			double, how long to wait for
	 */
	public void addTimer(int elevatorId, double delay) {
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
		}, (long) (delay*2));
	}
	
	/**
	 * Clears a timer for an elevator
	 * @param elevatorId	int, elevator to clear a timer for
	 */
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
	
	/**
	 * Getter
	 * @return	List<>, elevator move timers
	 */
	public List<ElevatorMoveTimer> getElevatorMoveTimers(){
		return elevatorMoveTimers;
	}
}
