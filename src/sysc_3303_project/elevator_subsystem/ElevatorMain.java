package sysc_3303_project.elevator_subsystem;

import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.SystemProperties;
import sysc_3303_project.messaging.UDPMessagerIncoming;
import sysc_3303_project.messaging.UDPMessagerOutgoing;

import java.util.LinkedList;

public class ElevatorMain {

    public static void main(String[] args) {

        for (int i = 0; i < SystemProperties.MAX_ELEVATOR_NUMBER; i++) {
            EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();
            EventBuffer<ElevatorEventType> inputBuffer = new EventBuffer<>();

            Elevator elevator = new Elevator(outputBuffer, inputBuffer, i);

            UDPMessagerOutgoing out = new UDPMessagerOutgoing(outputBuffer);
            UDPMessagerIncoming<ElevatorEventType> in = new UDPMessagerIncoming<>(new LinkedList<>() {{add(inputBuffer);}}, SystemProperties.SCHEDULER_PORT);

            Thread elevatorThread = new Thread(elevator);
            Thread outUdpThread = new Thread(out);
            Thread inUdpThread = new Thread(in);

            outUdpThread.start();
            inUdpThread.start();
            elevatorThread.start();
        }
    }
}
