/**
 * 
 */
package sysc_3303_project.scheduler_subsystem;

import java.util.HashMap;
import java.util.List;

import sysc_3303_project.common.Direction;

/**
 * @author Andrei Popescu
 *
 */
public class ElevatorTracker {
	
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
	
	private class ElevatorInfo {
		
		public List<LoadRequest> loadRequests;
		public List<Integer> unloadRequests;
		public int floor;
		public Direction direction = null;
		
		public ElevatorInfo() {}
	}
	
	private HashMap<Integer, ElevatorInfo> elevatorTrackingInfo;
	
	public ElevatorTracker() {
		elevatorTrackingInfo = new HashMap<>();
	}
	
	public void registerElevator(int elevatorId, int floor) {
		if (!elevatorTrackingInfo.containsKey(elevatorId)) {
			ElevatorInfo newElevatorInfo = new ElevatorInfo();
			newElevatorInfo.floor = floor;
			newElevatorInfo.direction = null;
			elevatorTrackingInfo.put(elevatorId, newElevatorInfo);
			elevatorTrackingInfo.get(elevatorId).floor = floor;
		}
	}
	
	public Direction getElevatorDirection(int elevatorId) {
		return elevatorTrackingInfo.get(elevatorId).direction;
	}
	
	public void updateElevatorFloor(int elevatorId, int floor) {
		elevatorTrackingInfo.get(elevatorId).floor = floor;
	}
	
	public void updateElevatorDirection(int elevatorId, Direction direction) {
		elevatorTrackingInfo.get(elevatorId).direction = direction;
	}
	
	public void addLoadRequest(int elevatorId, int floor, Direction direction) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		LoadRequest request = new LoadRequest(floor, direction);
		if (!info.loadRequests.contains(request)) info.loadRequests.add(request);
	}
	
	public void addUnloadRequest(int elevatorId, int floor) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		info.unloadRequests.add(floor);
	}
	
	public boolean loadElevator(int elevatorId, int floor) {
		boolean wasCompleted = hasLoadRequest(elevatorId, floor); //not sure if this return value will be used
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		LoadRequest toRemove = null;
		for (LoadRequest lr : info.loadRequests) {
			if (lr.floor == floor && lr.direction == getElevatorDirection(elevatorId)) {
				toRemove = lr;
			}
		}
		if (toRemove != null) { 
			info.loadRequests.remove(toRemove);
		}
		return wasCompleted;
	}
	
	public int unloadElevator(int elevatorId, int floor) {
		int unloadCount = countUnloadRequests(elevatorId, floor);
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		while (info.unloadRequests.remove((Integer) floor)) { //remove all matching unload requests
			
		}
		
		return unloadCount;
	}
	
	private boolean hasLoadRequestInDirection(int elevatorId, int floor, Direction direction) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		for (LoadRequest lr : info.loadRequests) {
			if (lr.floor == floor && lr.direction == direction) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasLoadRequest(int elevatorId, int floor) {
		return hasLoadRequestInDirection(elevatorId, floor, getElevatorDirection(elevatorId));
	}
	
	public int countUnloadRequests(int elevatorId, int floor) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		return (int) info.unloadRequests.stream().filter(r -> r == floor).count();
	}
	
	public boolean hasRequests(int elevatorId) {
		ElevatorInfo info = elevatorTrackingInfo.get(elevatorId);
		return !(info.loadRequests.isEmpty() && info.unloadRequests.isEmpty());
	}
}
