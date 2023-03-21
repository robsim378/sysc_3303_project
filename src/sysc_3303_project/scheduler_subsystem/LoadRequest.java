/**
 * 
 */
package sysc_3303_project.scheduler_subsystem;

import sysc_3303_project.common.Direction;

/**
 * 
 * @author Andrei Popescu
 * Class that represents a load request within the Scheduler subsystem.
 * Acts like a pair with a direction and floor. Provides direct access to these attributes since it is
 * treated like a datatype/struct as opposed to a proper class.
 */
public class LoadRequest {
	public int floor;
	public Direction direction;
	
	/**
	 * Constructs a new load request.
	 * @param floor the floor that the request originates from
	 * @param direction the direction (up/down) that the requester wants to travel in
	 */
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
