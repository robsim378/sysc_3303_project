/**
SYSC3303 Project
Group 1
@version 1.0
*/

package sysc_3303_project;

/**
 * @author Ian Holmes
 * Represents an Elevator to move between Floors.
 */
public class Elevator implements Runnable{

    private final Scheduler scheduler;
    private final int elevatorID;
    private int elevatorFloor;
    private Direction direction;

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
    }

    /**
     * Move the elevator to a desired floor.
     *
     * @param newFloor int, the floor to move to
     */
    private void moveElevator(int newFloor) {
        System.out.println("Elevator " + elevatorID + " moving " + this.direction + " to Floor " + newFloor);
        this.elevatorFloor = newFloor;
        System.out.println("Elevator " + elevatorID + " reached Floor " + this.elevatorFloor);
        // passengers load/unload
    }

    /**
     * The run method for the Elevator thread.
     * Elevator receives a request, moves, then responds.
     */
    public void run() {
        System.out.println("Elevator thread started");

        while (true) {
            RequestData request = scheduler.getRequest();
            System.out.println("Elevator " + elevatorID + " received request: " + request);
            int currentFloor = request.getCurrentFloor();
            int destinationFloor = request.getDestinationFloor();

            // the direction will not always be the same as a request in future iterations
            this.direction = request.getDirection();

            // go to the floor where the request came from
            moveElevator(currentFloor);
            // take the passenger to the destination floor
            moveElevator(destinationFloor);

            scheduler.addResponse(request);
            System.out.println("Elevator " + elevatorID + " sent response: " + request);
        }
    }
}

/*
TODO: must add functionality for:
    - determining direction (continue same direction until final floor request in that direction)
    - passengers loading/unloading
    - handling new requests while the elevator is moving
 */