/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package sysc_3303_project.execution;

import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerIncoming;
import sysc_3303_project.performance_tester.PerformanceEventType;
import sysc_3303_project.performance_tester.PerformanceTester;

import java.util.LinkedList;

/**
 * The main execution starting point for the performance subsystem
 * @author Robert, Ian
 *
 */
public class PerformanceMain {
	
	/**
	 * Runs the performance subsystem
	 * @param args command line arguments
	 */
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
