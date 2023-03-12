/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.scheduler_subsystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import logging.Logger;
import sysc_3303_project.common.Direction;

/**
 * @author Andrei Popescu
 *
 */
public class ElevatorTracker {
	
	/**
	 * 
	 * @author Andrei Popescu
	 * Internal class that represents a load request.
	 */
	private class LoadRequest {
		public int floor;
		public Direction direction;
		
		public LoadRequest(int floor, Direction direction) {
			this.floor = floor;
			this.direction = direction;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof LoadRequest)) {
				return false;
			}
			LoadRequest other = (LoadRequest) o;
			return (this.floor == other.floor && this.direction == other.direction);
		}
	}
	
	/**
	 * 
	 * @author Andrei Popescu
	 * Internal class to maintain information about an Elevator.
	 */
	private class ElevatorInfo {
		
		public List<LoadRequest> loadRequests;
		public List<Integer> unloadRequests;
		public int floor;
		public Direction direction = null;
		
		public ElevatorInfo() {
			loadRequests = new LinkedList<>();
			unloadRequests = new LinkedList<>();
			floor = 0;
		}
	}
	
	private HashMap<Integer, ElevatorInfo> elevatorTrackingInfo;
	
	/**
	 * Creates a new ElevatorTracker to track elevators.
	 * @param elevatorCount the number of elevators to track
	 */
	public ElevatorTracker(int elevatorCount) {
		elevatorTrackingInfo = new HashMap<>();
		for (int i = 0; i < elevatorCount; i++) {
			registerElevator(i, 0); //all elevators start at the ground floor
		}
	}
	
	/**
	 * Registers a new elevator to this tracker.
	 * @param elevatorId the ID of the new elevator
	 * @param floor the floor the new elevator is at
	 */
	private void registerElevator(int elevatorId, int floor) {
		if (!elevatorTrackingInfo.containsKey(elevatorId)) {
			ElevatorInfo newElevatorInfo = new ElevatorInfo();
			newElevatorInfo.floor = floor;
			newElevatorInfo.direction = null;
			elevatorTrackingInfo.put(elevatorId, newElevatorInfo);
		}
	}
	
	/**
	 * Gets the current floor of an elevator.
	 * @param elevatorId the ID of the elevator
	 * @return the floor that the elevator is at
	 */
	public int getElevatorFloor(int elevatorId) {
		return elevatorTrackingInfo.get(elevatorId).floor;
	}
	
	/**
	 * Gets the current direction of an elevator. This will be null if the elevator is idle.
	 * The elevator's direction will not be null if it is temporarily stopped to load/unload passengers.
	 * @param elevatorId the ID of the elevator
	 * @return the direction that the elevator is moving in
	 */
	public Direction getElevatorDirection(int elevatorId) {
		return elevatorTrackingInfo.get(elevatorId).direction;
	}
	
	/**
	 * Gets the total number of requests that an elevator is serving (load and unload).
	 * @param elevatorId the ID of the elevator
	 * @return the number of requests for the elevator
	 */
	public int getElevatorRequestCount(int elevatorId) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		return info.loadRequests.size() + info.unloadRequests.size();
	}
	
	/**
	 * Sets the current floor of an elevator.
	 * @param elevatorId the ID of the elevator
	 * @param floor the floor ID/number the elevator is at
	 */
	public void updateElevatorFloor(int elevatorId, int floor) {
		Logger.getLogger().logNotification("ElevatorTracker", "Elevator " + elevatorId + " is at floor " + floor);
		elevatorTrackingInfo.get(elevatorId).floor = floor;
	}
	
	/**
	 * Sets the current direction of an elevator.
	 * @param elevatorId the ID of the elevator
	 * @param direction the direction the elevator is going in, or null if idle
	 */
	public void updateElevatorDirection(int elevatorId, Direction direction) {
		Logger.getLogger().logNotification("ElevatorTracker", "Elevator " + elevatorId + " is moving in direction " + direction);
		elevatorTrackingInfo.get(elevatorId).direction = direction;
	}
	
	/**
	 * Adds a load request to a specified elevator.
	 * Corresponds to one or more identical floor button presses.
	 * @param elevatorId the ID of the elevator to add the request to
	 * @param floor the ID/number of the floor to load passengers from
	 * @param direction the direction of the load request
	 */
	public void addLoadRequest(int elevatorId, int floor, Direction direction) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		LoadRequest request = new LoadRequest(floor, direction);
		if (!info.loadRequests.contains(request)) info.loadRequests.add(request);
	}
	
	/**
	 * Adds an unload request to the specified elevator.
	 * Corresponds to one elevator button press.
	 * @param elevatorId the ID of the elevator to add the request to
	 * @param floor the ID/number of the floor to unload passengers at
	 */
	public void addUnloadRequest(int elevatorId, int floor) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		info.unloadRequests.add(floor);
	}
	
	/**
	 * Loads a specified elevator at a floor, according to its load requests.
	 * Attempts to serve a request in the elevator's current direction; if there is none,
	 * attempts to serve one in the opposite direction.
	 * @param elevatorId the ID of the elevator to load
	 * @param floor the ID/number of the floor to load passengers from 
	 * @return the Direction of the served load request, or null if none served
	 */
	public Direction loadElevator(int elevatorId, int floor) {
		boolean wasCompleted = hasLoadRequestInDirection(elevatorId, floor, getElevatorDirection(elevatorId));
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		LoadRequest toRemove = null;
		for (LoadRequest lr : info.loadRequests) {
			if (lr.floor == floor && lr.direction == getElevatorDirection(elevatorId)) {
				toRemove = lr;
			}
		}
		if (!wasCompleted && hasLoadRequest(elevatorId, floor)) { //if there is a request in the other direction but none in current direction
			for (LoadRequest lr : info.loadRequests) {
				if (lr.floor == floor) {
					toRemove = lr;
					wasCompleted = true;
				}
			}
		}
		if (toRemove != null) { 
			info.loadRequests.remove(toRemove);
			return toRemove.direction;
		}
		return null;
	}
	
	/**
	 * Unloads a specified elevator at a floor, according to its unload requests.
	 * @param elevatorId the ID of the elevator to unload
	 * @param floor the ID/number of the floor to unload at
	 * @return the number of unload requests served
	 */
	public int unloadElevator(int elevatorId, int floor) {
		int unloadCount = countUnloadRequests(elevatorId, floor);
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		while (info.unloadRequests.remove((Integer) floor)) { //remove all matching unload requests
			
		}
		return unloadCount;
	}
	
	/**
	 * Checks if a specified elevator has load requests in a specific direction at a floor.
	 * @param elevatorId the ID of the elevator to load
	 * @param floor the ID/number of the floor to load at
	 * @param direction the direction of load requests to check for
	 * @return true if there is a matching load request, false otherwise
	 */
	public boolean hasLoadRequestInDirection(int elevatorId, int floor, Direction direction) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		for (LoadRequest lr : info.loadRequests) {
			if (lr.floor == floor && lr.direction == direction) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a specified elevator has a load request in any direction at a floor.
	 * @param elevatorId the ID of the elevator to load
	 * @param floor the ID/number of the floor to load at
	 * @return true if there is a matching load request, false otherwise
	 */
	public boolean hasLoadRequest(int elevatorId, int floor) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		for (LoadRequest lr : info.loadRequests) {
			if (lr.floor == floor) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the number of unload requests for an elevator at a floor.
	 * @param elevatorId the ID of the elevator to check
	 * @param floor the ID/number of the floor to unload at
	 * @return the number of matching unload requests
	 */
	public int countUnloadRequests(int elevatorId, int floor) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		return (int) info.unloadRequests.stream().filter(r -> r == floor).count();
	}
	
	/**
	 * Checks if an elevator has any requests to serve (load or unload).
	 * @param elevatorId the ID of the elevator to check
	 * @return true if the elevator has any requests, false otherwise.
	 */
	public boolean hasRequests(int elevatorId) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		return !(info.loadRequests.isEmpty() && info.unloadRequests.isEmpty());
	}
}
