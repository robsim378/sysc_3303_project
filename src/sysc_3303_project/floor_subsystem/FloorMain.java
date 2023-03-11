/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package sysc_3303_project;

public class FloorMain {
    private static final int numFloors = 10;
    public static void main(String[] args) {
        // Create the outgoing message buffer shared by all floors
        EventBuffer<Enum<?>> outgoingBuffer = new EventBuffer<>();
        UDPMessagerOutgoing out = new UDPMessagerOutgoing(outgoingBuffer);

        ArrayList<FloorSystem> floors = new ArrayList<>;
        ArrayList<EventBuffer<>> floorBuffers = new ArrayList<>;

        for (int i = 0; i < numFloors; i++) {
            EventBuffer<FloorEventType> floorBuffer = new EventBuffer<>();
            floors.add(new FloorSystem(, outgoingBuffer))
        }

        UDPMessagerIncoming<FloorEventType> in = new UDPMessagerIncoming<FloorEventType>(floorBuffers, SystemProperties.FLOOR_PORT);

        String floorFilePath = new File("").getAbsolutePath() + "/resources/testing_examples";
        FloorMessageController messageController = new FloorMessageController(floorBuffers, outgoingBuffer,floorFilePath, floors);

        // Create all necessary threads
        Thread outThread = new Thread(out);
        Thread inThread = new Thread(in);
        thread inputFileThread = new Thread(messageController);
        outThread.start();
        inThread.start();
        for (FloorSystem floor : floors) {
            Thread floorThread = new Thread(floor);
            floorThread.start();
        }
        inputFileThread.start();
    }
}