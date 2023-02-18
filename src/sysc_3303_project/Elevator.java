/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
*/

package sysc_3303_project;

/**
 * @author Ian Holmes
 * Represents an Elevator to move between Floors.
 */
public class Elevator implements Runnable{

    private enum ElevatorState {STATIONARY_DOORS_OPEN, STATIONARY_DOORS_CLOSED, DOORS_OPENING, DOORS_CLOSING, MOVING}
    private final EventBuffer<SchedulerEventType> schedulerBuffer;
    private final int elevatorID;
    private int elevatorFloor;
    private Direction direction;
    private ElevatorState state;
    private int destinationFloor;
    private EventBuffer<ElevatorEventType> eventBuffer;
    

    /**
     * Constructor for the Elevator class.
     *
     * @param scheduler Scheduler, the scheduler to receive requests from
     * @param elevatorID int, the ID of the Elevator
     */
    public Elevator(EventBuffer<SchedulerEventType> schedulerBuffer, int elevatorID) {
        this.schedulerBuffer = schedulerBuffer;
        this.elevatorID = elevatorID;
        this.elevatorFloor = 0;
        state = ElevatorState.STATIONARY_DOORS_OPEN;
        eventBuffer = new EventBuffer<>();
    }
    
    public EventBuffer<ElevatorEventType> getEventBuffer() {
		return eventBuffer;
	}

    /**
     * Move the elevator one floor towards its destination
     */
    private void moveElevator() {
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

        while (true) {
            switch (state) {
                case STATIONARY_DOORS_OPEN:
                    System.out.println("Elevator " + elevatorID + " doors open");
                    // TODO: Wait for scheduler to send "close door" signal
                    state = ElevatorState.DOORS_CLOSING;
                    break;
                case STATIONARY_DOORS_CLOSED:
                    System.out.println("Elevator " + elevatorID + " doors closed");
                    // TODO: Wait for scheduler to send "move" or "open door" signal
                    // if "open door" signal
                    state = ElevatorState.DOORS_OPENING;
                    // if "move" signal
                    state = ElevatorState.MOVING;
                    break;
                case DOORS_CLOSING:
                    System.out.println("Elevator " + elevatorID + " closing doors");
                    // Wait for doors to finish closing
                    state = ElevatorState.STATIONARY_DOORS_CLOSED;
                    break;
                case DOORS_OPENING:
                    System.out.println("Elevator " + elevatorID + " opening doors");
                    // Wait for doors to finish opening
                    state = ElevatorState.STATIONARY_DOORS_OPEN;
                    break;
                case MOVING:
                    // TODO: get destinationFloor from scheduler
                    // Check to see if destinationFloor has changed after every floor in case a button is pressed between
                    // the current floor and the destination floor (e.g. going from floor 2 -> 5 and someone on floor 4
                    // presses the up button.)
                    if (elevatorFloor == destinationFloor) {
                        System.out.println("Elevator " + elevatorID + " reached floor " + elevatorFloor);
                        state = ElevatorState.STATIONARY_DOORS_CLOSED;
                    }
                    else {
                        moveElevator();
                    }

                    break;
            }
        }
    }
}

/*
TODO: must add functionality for:
    - determining direction (continue same direction until final floor request in that direction)
    - passengers loading/unloading
    - handling new requests while the elevator is moving
 */