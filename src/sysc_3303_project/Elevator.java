/**
 *
 */
package sysc_3303_project;

import java.time.LocalTime;

/**
 *
 * @author ianh6
 */
public class Elevator implements Runnable{

    private final Scheduler scheduler;
    private final int elevatorID;
    private int elevatorFloor;
    private Direction direction;

    /**
     *
     * @param scheduler
     * @param elevatorID
     */
    public Elevator(Scheduler scheduler, int elevatorID) {
        this.scheduler = scheduler;
        this.elevatorID = elevatorID;
        this.elevatorFloor = 0;
    }

    /**
     *
     * @param newFloor
     */
    private void moveElevator(int newFloor) {
        System.out.println("Elevator " + elevatorID + " moving " + this.direction + " to Floor " + newFloor);
        this.elevatorFloor = newFloor;
        System.out.println("Elevator " + elevatorID + " reached Floor " + this.elevatorFloor);
        // passengers load/unload
    }

    /**
     *
     * @param destinationFloor
     * @return
     */
    private RequestData generateResponse(int destinationFloor) {

        System.out.println("Elevator " + elevatorID + " generating response...");
        return new RequestData(
                LocalTime.now(),
                this.elevatorFloor,
                this.direction,
                destinationFloor
        );
    }

    /**
     *
     */
    public void run() {
        System.out.println("Elevator thread started");

        while (scheduler.hasRequests()) {
            RequestData request = scheduler.getRequest();
            System.out.println("Elevator " + elevatorID + " received request: " + request);
            int currentFloor = request.getCurrentFloor();
            int destinationFloor = request.getDestinationFloor();

            // the direction will not always be the same as a request in future iterations
            this.direction = request.getDirection();

            // go to the floor where the request came from
            moveElevator(currentFloor);
            RequestData response = generateResponse(destinationFloor);
            scheduler.addResponse(response);
            System.out.println("Elevator " + elevatorID + " sent response: " + response);

            // take the passenger to the destination floor
            moveElevator(destinationFloor);
            response = generateResponse(0); // 0 means no destination
            scheduler.addResponse(response);
            System.out.println("Elevator " + elevatorID + " sent response: " + response);
        }
    }
}

/*
TODO: must add functionality for:
    - determining direction (continue same direction until final floor request in that direction)
    - passengers loading/unloading
    - handling new requests while the elevator is moving
 */