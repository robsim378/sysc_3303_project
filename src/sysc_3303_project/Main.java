/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
 */
package sysc_3303_project;

import java.io.File;

import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.floor_subsystem.FloorSystem;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * @author Robert Simionescu
 * Main class used to run the elevator program. Initializes elevator, floorsystem, and scheduler threads and starts them.
 */
public class Main {
    /**
     * main function that initializes everything to run the elevator program.
     * @param args
     */
    public static void main(String[] args) {
        Thread elevator, floorSystem;
        Scheduler scheduler;
        
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();
        EventBuffer<FloorEventType> floorBuffer = new EventBuffer<>();

        scheduler = new Scheduler(elevatorBuffer, floorBuffer);

        elevator = new Thread(new Elevator(scheduler.getEventBuffer(), elevatorBuffer, 0), "Elevator");

        String floorFilePath = new File("").getAbsolutePath() + "/resources/testing_examples";
        
        floorSystem = new Thread(new FloorSystem(scheduler.getEventBuffer(), floorBuffer, floorFilePath), "FloorSystem");
        
        Thread schedulerThread = new Thread(scheduler, "Scheduler");

        elevator.start();
        floorSystem.start();
        schedulerThread.start();
    }
}