/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package test.scheduler_subsystem.states;

import org.junit.jupiter.api.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.events.RequestData;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.scheduler_subsystem.states.SchedulerProcessingState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerWaitingState;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ian Holmes
 *
 */
public class SchedulerWaitingStateTest extends SchedulerStateTest {

	@Override
    @Test
    public void handleFloorButtonPressedTest() {
		EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);

        SchedulerState testState = new SchedulerWaitingState(context);

        SchedulerState newState = testState.handleFloorButtonPressed(8, Direction.DOWN);

        Event<Enum<?>> testEvent = outputBuffer.getEvent();
        assertEquals(ElevatorEventType.CLOSE_DOORS, testEvent.getEventType());
        assertEquals(0, testEvent.getDestinationID());
        assertTrue(newState instanceof SchedulerProcessingState);
        assertTrue(context.getTracker().hasRequests(0));
    }
	
	/**
     * Tests reaction when the valid event "handleElevatorButtonPressed" is triggered
     */
	@Override
    @Test
    public void handleElevatorButtonPressedTest() {
		EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

		Scheduler context = new Scheduler(null, outputBuffer);
		assertFalse(context.getTracker().hasRequests(0));

        SchedulerState testState = new SchedulerWaitingState(context);

        SchedulerState newState = testState.handleElevatorButtonPressed(0, 6);

        assertTrue(newState instanceof SchedulerProcessingState);
        assertTrue(context.getTracker().hasRequests(0));
    }
}
