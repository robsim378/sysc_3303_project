/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.scheduler_subsystem;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;

import sysc_3303_project.floor_subsystem.FloorSystem;

import sysc_3303_project.scheduler_subsystem.states.SchedulerState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerWaitingState;
import logging.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 	@author Andrei Popescu
 *	The Scheduler class is responsible for passing requests from the Floor to the Elevator,
 *	and passing the responses from the Elevator back to the Floor. It acts as a monitor.
 */
public class Scheduler implements Runnable {
	
	private final EventBuffer<SchedulerEventType> inputBuffer;
	private final EventBuffer<Enum<?>> outputBuffer; 
	private SchedulerState state;
	private ElevatorTracker tracker;
	
	/**
	 * Creates a new Scheduler with no requests, which interacts with an Elevator and a FloorSystem via buffers.
	 * @param elevatorBuffer event buffer to send events to an Elevator
	 * @param floorBuffer event buffer to send events to a Floor
	 */
	public Scheduler(EventBuffer<SchedulerEventType> inputBuffer, EventBuffer<Enum<?>> outputBuffer) {
		this.inputBuffer = inputBuffer;
		this.outputBuffer = outputBuffer;
		state = new SchedulerWaitingState(this);

		tracker = new ElevatorTracker(ResourceManager.getResourceManager().getInt("count.elevators"));
	}
	
	/**
	 * Gets this Scheduler's input event buffer.
	 * @return this Scheduler's EventBuffer
	 */
	public EventBuffer<SchedulerEventType> getInputBuffer() {
		return inputBuffer;
	}
	
	/**
	 * Gets this Scheduler's output event buffer.
	 * @return this Scheduler's EventBuffer
	 */
	public EventBuffer<Enum<?>> getOutputBuffer() {
		return outputBuffer;
	}
	
	/**
	 * Returns the Direction that an elevator should move in from a stopped position.
	 * A return value of null indicates that the elevator should not resume its movement and instead stay stopped.
	 * @param elevatorId the ID of the elevator
	 * @return the Direction for the elevator to move in, can be null
	 */
	public Direction directionToMove(int elevatorId) {
		if (!tracker.hasRequests(elevatorId)) { //if idle, don't move
			return null;
		}
		boolean hasFurtherRequests = false;
		if (tracker.getElevatorDirection(elevatorId) == null) tracker.updateElevatorDirection(elevatorId, Direction.DOWN); //give idle elevator a temporary direction
		for (int floor : getFurtherFloors(elevatorId)) {
			hasFurtherRequests = hasFurtherRequests || tracker.hasLoadRequest(elevatorId, floor) || tracker.countUnloadRequests(elevatorId, floor) > 0;
		}
		if (hasFurtherRequests) {
			return tracker.getElevatorDirection(elevatorId);
		} else {
			return tracker.getElevatorDirection(elevatorId) == Direction.UP ? Direction.DOWN : Direction.UP; //turn around
		}
	}
	
	/**
	 * Checks whether an elevator should stop at a given floor.
	 * @param elevatorId the ID of the elevator
	 * @param floor the number/ID of the floor to check
	 * @return true if the elevator should stop, false otherwise
	 */
	public boolean shouldStop(int elevatorId, int floor) {
		tracker.updateElevatorFloor(elevatorId, floor);
		//stop if there is a request (load in correct direction or unload) at the floor
		boolean stopping = tracker.hasLoadRequestInDirection(elevatorId, floor, tracker.getElevatorDirection(elevatorId))
				|| (tracker.countUnloadRequests(elevatorId, floor) > 0);
		//also stop if reaching the top or bottom floor - shouldn't happen but failsafe
		stopping = stopping ||
				(floor == ResourceManager.getResourceManager().getInt("count.floors") && tracker.getElevatorDirection(elevatorId) == Direction.UP) ||
				(floor == 0 && tracker.getElevatorDirection(elevatorId)== Direction.DOWN);
		
		int[] furtherFloors = getFurtherFloors(elevatorId);
		boolean hasFurtherRequests = false;
		for (int f : furtherFloors) {
			if (f != floor && (tracker.hasLoadRequest(elevatorId, f) || tracker.countUnloadRequests(elevatorId, f) > 0)) {
				hasFurtherRequests = true;
			}
		}
		if (!hasFurtherRequests) {
			stopping = stopping || (tracker.hasLoadRequest(elevatorId, floor)); //if there are no further requests, accept a load request in the "other" direction, will turn around after
		}
		return stopping;
	}
	
	/**
	 * Gets the list of floors that an elevator would pass by if it kept going in its current direction.
	 * @param elevatorId 
	 * @return
	 */
	private int[] getFurtherFloors(int elevatorId) {
		if (tracker.getElevatorDirection(elevatorId) == Direction.UP) {
			
			return IntStream.range(tracker.getElevatorFloor(elevatorId) + 1, ResourceManager.getResourceManager().getInt("count.floors")).toArray();
		} else if (tracker.getElevatorDirection(elevatorId) == Direction.DOWN) {
			return IntStream.rangeClosed(tracker.getElevatorFloor(elevatorId) - 1, 0).toArray();
		} else {
			return new int[0];
		}
	}
	
	/**
	 * Assigns a new load request to the elevator in the best position to handle it.
	 * @param floor the floor ID/number that the new request is at
	 * @param direction the direction that the new request is going in
	 * @return the ID of the elevator that the request was assigned to
	 */
	public int assignLoadRequest(int floor, Direction direction) {
		List<Integer> onTheWay = new LinkedList<>();
		List<Integer> notOnTheWay = new LinkedList<>();
		List<Integer> priorityList = new LinkedList<>();
		for (int id = 0; id < ResourceManager.getResourceManager().getInt("count.elevators"); id++) {
			boolean elevatorOnTheWay = false;
			for (int f : getFurtherFloors(id)) {
				if (f == floor) {
					elevatorOnTheWay = true;
				}
			}
			if (elevatorOnTheWay) {
				onTheWay.add(id);
			} else {
				notOnTheWay.add(id);
			}
		}
		Comparator<Integer> assignPriority = (id, id2) -> tracker.getElevatorRequestCount(id) - tracker.getElevatorRequestCount(id2);
		//sort each set by fewest requests first
		onTheWay.sort(assignPriority);
		notOnTheWay.sort(assignPriority);
		Logger.getLogger().logNotification("Scheduler", onTheWay.toString());
		Logger.getLogger().logNotification("Scheduler", notOnTheWay.toString());
		//combine the 2 so elevators that are on the way are always considered first
		priorityList.addAll(onTheWay);
		priorityList.addAll(notOnTheWay);
		//due to how the onTheWay/notOnTheWay lists are generated, ties are broken by lowest ID number first
		int elevatorId = priorityList.get(0);
		tracker.addLoadRequest(elevatorId, floor, direction);
		Logger.getLogger().logNotification("Scheduler", "Assigned load request to elevator " + elevatorId + ": " + floor + " " + direction);
		return elevatorId;
	}
	
	/**
	 * Gets the scheduler's elevator tracker.
	 * @return this Scheduler's ElevatorTracker
	 */
	public ElevatorTracker getTracker() {
		return tracker;
	}
	
	/**
	 * Parses and events from this Scheduler's event buffer in an infinite loop.
	 */
	public void eventLoop() {
		while (true) {

			Event<SchedulerEventType> evt = inputBuffer.getEvent();
			SchedulerState newState = null;


    		Logger.getLogger().logNotification(this.getClass().getName(), "Event: " + evt.getEventType() + ", State: " + state.getClass().getName());    		

			
			switch(evt.getEventType()) {
				case ELEVATOR_DOORS_CLOSED:
					newState = state.handleElevatorDoorsClosed(evt.getSourceID(), (int) evt.getPayload());
					break;
				case ELEVATOR_DOORS_OPENED:
					newState = state.handleElevatorDoorsOpened(evt.getSourceID(), (int) evt.getPayload());
					break;
				case ELEVATOR_APPROACHING_FLOOR:
					newState = state.handleElevatorApproachingFloor(evt.getSourceID(), (int) evt.getPayload());
					break;
				case ELEVATOR_STOPPED:
					newState = state.handleElevatorStopped(evt.getSourceID(), (int) evt.getPayload());
					break;
				case FLOOR_BUTTON_PRESSED:
					newState = state.handleFloorButtonPressed(evt.getSourceID(), (Direction) evt.getPayload());
					break;
				case ELEVATOR_BUTTON_PRESSED:
					newState = state.handleElevatorButtonPressed(evt.getSourceID(), (int) evt.getPayload());
					break;
			}
			
			if (newState != null) {
//				state.doExit();
				state = newState;
				state.doEntry();
			}
		}
	}

	@Override
	public void run() {
		Logger.getLogger().logNotification(this.getClass().getName(), "Scheduler thread running");
		try {
			eventLoop();
		} catch (IllegalStateException e) {
			Logger.getLogger().logError(this.getClass().getName(), e.toString());
		}
		
	}
}
