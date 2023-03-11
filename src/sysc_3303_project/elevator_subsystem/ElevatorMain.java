package sysc_3303_project.elevator_subsystem;

import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.SystemProperties;
import sysc_3303_project.messaging.UDPMessagerIncoming;
import sysc_3303_project.messaging.UDPMessagerOutgoing;

import java.util.LinkedList;
import java.util.List;

public class ElevatorMain {

    public static void main(String[] args) {
        EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();
        List<EventBuffer<ElevatorEventType>> inputBuffersList = new LinkedList<>();
        List<Elevator> elevators = new LinkedList<>();

        for (int i = 0; i < SystemProperties.MAX_ELEVATOR_NUMBER; i++) {
            EventBuffer<ElevatorEventType> inputBuffer = new EventBuffer<>();

            Elevator elevator = new Elevator(outputBuffer, inputBuffer, i);

            elevators.add(elevator);
        }
        UDPMessagerOutgoing out = new UDPMessagerOutgoing(outputBuffer);
        UDPMessagerIncoming<ElevatorEventType> in = new UDPMessagerIncoming<>(inputBuffersList, SystemProperties.SCHEDULER_PORT);

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
