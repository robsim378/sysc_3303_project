/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project.floor_subsystem;

import java.io.File;
import java.util.ArrayList;

import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerIncoming;
import sysc_3303_project.common.messaging.UDPMessagerOutgoing;

/**
 * @author Robert Simionescu
 * Main class for the Floor subsystem. Instantiates all the necessary floors and sets up communication with other subsystems.
 */
public class FloorMain {
    public static void main(String[] args) {
        // Read the number of floors to instantiate
    	int numFloors = ResourceManager.getResourceManager().getInt("count.floors");

        // Create the outgoing message buffer shared by all floors
        EventBuffer<Enum<?>> outgoingBuffer = new EventBuffer<>();
        UDPMessagerOutgoing out = new UDPMessagerOutgoing(outgoingBuffer);

        // Create the list of all floors and their buffers
        ArrayList<FloorSystem> floors = new ArrayList<>();
        ArrayList<EventBuffer<FloorEventType>> floorBuffers = new ArrayList<>();

        // Initialize all the floors and their buffers
        for (int i = 0; i < numFloors; i++) {
            EventBuffer<FloorEventType> floorBuffer = new EventBuffer<>();
            floorBuffers.add(floorBuffer);
            floors.add(new FloorSystem(i, floorBuffer, outgoingBuffer));
        }

        // Create the incoming message buffer used by all floors
        UDPMessagerIncoming<FloorEventType> in = new UDPMessagerIncoming<FloorEventType>(floorBuffers, Subsystem.FLOOR);

        // Initialize the file controller that reads the input file and sends its requests to the appropriate floors
        String floorFilePath = new File("").getAbsolutePath() + "/resources/testing_examples";
        InputFileController messageController = new InputFileController(floorFilePath, floors);

        // Create and run all threads
        Thread outThread = new Thread(out);
        Thread inThread = new Thread(in);
        Thread inputFileThread = new Thread(messageController);
        outThread.start();
        inThread.start();
        for (FloorSystem floor : floors) {
            Thread floorThread = new Thread(floor);
            floorThread.start();
        }
        inputFileThread.start();
    }
}