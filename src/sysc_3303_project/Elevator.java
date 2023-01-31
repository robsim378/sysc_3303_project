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
     * @param currFloor
     * @param destinationFloor
     */
    private void moveElevator(int currFloor, int destinationFloor) {
        System.out.println("Elevator " + elevatorID + " moving " + this.direction + " to Floor " + currFloor);
        this.elevatorFloor = currFloor;
        System.out.println("Elevator " + elevatorID + " reached Floor " + this.elevatorFloor);
        // passengers load/unload
        System.out.println("Elevator " + elevatorID + " moving " + this.direction + " to Floor " + destinationFloor);
        this.elevatorFloor = destinationFloor;
        System.out.println("Elevator " + elevatorID + " reached Floor " + this.elevatorFloor);
        // passengers load/unload

        /*
        TODO: Implement elevator movement
            must add functionality for:
            - determining direction (continue same direction until final floor request in that direction)
            - passengers loading/unloading
         */
    }

    /**
     *
     * @param request
     * @return
     */
    private RequestData generateResponse(RequestData request) {

        System.out.println("Elevator " + elevatorID + " generating response...");
        return new RequestData(
                LocalTime.now(),
                this.elevatorFloor,
                request.getDirection(),
                0
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

            // the direction will not always be the same as a request in future iterations
            this.direction = request.getDirection();
            moveElevator(request.getCurrentFloor(), request.getDestinationFloor());

            RequestData response = generateResponse(request);
            scheduler.addResponse(response);
            System.out.println("Elevator " + elevatorID + " sent response: " + response);
        }
    }
}