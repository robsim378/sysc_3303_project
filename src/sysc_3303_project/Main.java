/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package sysc_3303_project;

import java.io.File;

/**
 * @author Robert Simionescu
 */
public class Main {
    public static void main(String[] args) {
        Thread elevator, floorSystem;
        Scheduler scheduler;

        scheduler = new Scheduler();

        elevator = new Thread(new Elevator(scheduler, 0), "Elevator");

        String floorFilePath = new File("").getAbsolutePath() + "/resources/testing_examples";
        floorSystem = new Thread(new FloorSystem(scheduler, floorFilePath), "FloorSystem");
        Thread schedulerThread = new Thread(scheduler, "Scheduler");

        elevator.start();
        floorSystem.start();
        schedulerThread.start();
    }
}