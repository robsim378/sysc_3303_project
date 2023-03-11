package sysc_3303_project.elevator_subsystem;

import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerIncoming;
import sysc_3303_project.common.messaging.UDPMessagerOutgoing;

import java.util.LinkedList;
import java.util.List;

public class ElevatorMain {

    public static void main(String[] args) {
        EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();
        List<EventBuffer<ElevatorEventType>> inputBuffersList = new LinkedList<>();
        List<Elevator> elevators = new LinkedList<>();

        int count = ResourceManager.getResourceManager().getInt("count.elevators");
        
        for (int i = 0; i < count; i++) {
            EventBuffer<ElevatorEventType> inputBuffer = new EventBuffer<>();

            Elevator elevator = new Elevator(outputBuffer, inputBuffer, i);

            elevators.add(elevator);
        }
        UDPMessagerOutgoing out = new UDPMessagerOutgoing(outputBuffer);
        UDPMessagerIncoming<ElevatorEventType> in = new UDPMessagerIncoming<>(inputBuffersList, Subsystem.ELEVATOR);

        Thread outUdpThread = new Thread(out);
        Thread inUdpThread = new Thread(in);
        Thread elevatorThread;

        outUdpThread.start();
        inUdpThread.start();
        for (Elevator e : elevators) {
            elevatorThread = new Thread(e);
            elevatorThread.start();
        }
    }
}
