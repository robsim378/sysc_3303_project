/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package sysc_3303_project.scheduler_subsystem;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.common.SystemProperties;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.scheduler_subsystem.states.SchedulerState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerWaitingState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import logging.Logger;

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
		tracker = new ElevatorTracker(SystemProperties.MAX_ELEVATOR_NUMBER);
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
	
	public Direction directionToMove(int elevatorId) {
		if (!tracker.hasRequests(elevatorId)) {
			return null;
		}
		boolean hasFurtherRequests = false;
		for (int floor : getFurtherFloors(elevatorId)) {
			hasFurtherRequests = hasFurtherRequests || tracker.hasLoadRequest(elevatorId, floor) || tracker.countUnloadRequests(elevatorId, floor) > 0;
		}
		return null;
	}
	
	private int[] getFurtherFloors(int elevatorId) {
		if (tracker.getElevatorDirection(elevatorId) == Direction.DOWN) {
			return IntStream.range(tracker.getElevatorFloor(elevatorId), SystemProperties.MAX_FLOOR_NUMBER).toArray();
		} else if (tracker.getElevatorDirection(elevatorId) == Direction.DOWN) {
			return IntStream.rangeClosed(tracker.getElevatorFloor(elevatorId), 0).toArray();
		} else {
			return new int[0];
		}
	}
	
	public int assignLoadRequest(int floor, Direction direction) {
		int[] ids = IntStream.range(0, SystemProperties.MAX_ELEVATOR_NUMBER).toArray();
		List<Integer> onTheWay = new LinkedList<>();
		List<Integer> notOnTheWay = new LinkedList<>();
		List<Integer> priorityList = new LinkedList<>();
		for (int id : ids) {
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
		//combine the 2 so elevators that are on the way are always considered first
		priorityList.addAll(onTheWay);
		priorityList.addAll(notOnTheWay);
		//due to how the onTheWay/notOnTheWay lists are generated, ties are broken by lowest ID number first
		int elevatorId = onTheWay.get(0);
		tracker.addLoadRequest(elevatorId, floor, direction);
		return elevatorId;
	}
	
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
				state.doExit();
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
