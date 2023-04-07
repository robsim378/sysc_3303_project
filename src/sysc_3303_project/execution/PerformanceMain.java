package sysc_3303_project.execution;

import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerIncoming;
import sysc_3303_project.performance_tester.PerformanceEventType;
import sysc_3303_project.performance_tester.PerformanceTester;

import java.util.LinkedList;

public class PerformanceMain {
    public static void main(String[] args) {

        EventBuffer<PerformanceEventType> inputBuffer = new EventBuffer<>();

        PerformanceTester performanceTester = new PerformanceTester(inputBuffer);

        UDPMessagerIncoming<PerformanceEventType> in = new UDPMessagerIncoming<PerformanceEventType>(new LinkedList<>() {{add(inputBuffer);}}, Subsystem.PERFORMANCE);

        Thread performanceThread = new Thread(performanceTester);

        Thread inUdpThread = new Thread(in);
        inUdpThread.start();
        performanceThread.start();

    }
}
