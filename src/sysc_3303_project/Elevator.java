/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
*/

package sysc_3303_project;

/**
 * @author Ian Holmes & Robert Simionescu
 * Represents an Elevator to move between Floors.
 * Context class for the elevator state machine.
 */
public class Elevator implements Runnable {

    private final Scheduler scheduler;
    private final int elevatorID;
    private int elevatorFloor;
    private Direction direction;
    private ElevatorState state;


    /**
     * Constructor for the Elevator class.
     *
     * @param scheduler Scheduler, the scheduler to receive requests from
     * @param elevatorID int, the ID of the Elevator
     */
    public Elevator(Scheduler scheduler, int elevatorID) {
        this.scheduler = scheduler;
        this.elevatorID = elevatorID;
        this.elevatorFloor = 0;
        state = new ElevatorDoorsClosedState();
    }

    /**
     * Getter for the elevatorID
     * @return int, the ID number of this elevator.
     */
    public int getElevatorID() {
        return elevatorID;
    }


    /**
     * Getter for the elevator's current floor
     * @return int, the current floor the elevator is on.
     */
    public int getFloor() {
        return elevatorFloor;
    }

    /**
     * Setter for the direction
     * @param direction Direction, the direction to move the elevator.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Move the elevator one floor towards its destination
     */
    public void moveElevator() {    // TODO: This should maybe be moved into ElevatorMovingState, but I'm not sure.
        int resultFloor;
        if (direction == Direction.UP) {
            resultFloor = elevatorFloor + 1;
        }
        else {
            resultFloor = elevatorFloor - 1;
        }
        System.out.println("Elevator " + elevatorID + " moving from floor " + elevatorFloor + " to floor " + resultFloor);
        elevatorFloor = resultFloor;

        // Sleep for however long
    }


    /**
     * The run method for the Elevator thread.
     * Contains the state machine for the Elevator.
     */
    public void run() {
        System.out.println("Elevator thread started");

        String command; // TODO: Maybe make this an enum

        while (true) {
            command = ""; // TODO: Get this from the scheduler
            switch (command.toUpperCase()) {
                case "OPEN_DOORS":
                    state = state.openDoors(this);
                    break;
                case "CLOSE_DOORS":
                    state = state.closeDoors(this);
                    break;
                case "SET_DIRECTION":
                    state = state.setDirection(this, Direction.UP); // TODO: Pass the appropriate direction once the command format has been decided upon
                    break;
                case "DO_NOT_STOP_AT_NEXT_FLOOR":
                    state = state.doNotStopAtNextFloor(this);
                    break;
                case "STOP_AT_NEXT_FLOOR":
                    state = state.stopAtNextFloor(this);
                    break;
            }
        }
    }
}
