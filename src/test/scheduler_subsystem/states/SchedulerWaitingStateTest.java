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
public class SchedulerWaitingStateTest extends SchedulerStateTest{

	@Override
    @Test
    public void handleFloorButtonPressed() {
        EventBuffer<ElevatorEventType> elevatorBuffer = new EventBuffer<>();

        Scheduler context = new Scheduler(elevatorBuffer, null);
        SchedulerState testState = new SchedulerWaitingState(context);

        RequestData testInput = new RequestData(LocalTime.NOON, 2, Direction.DOWN, 4);

        SchedulerState newState = testState.handleFloorButtonPressed(testInput);

        assertTrue(context.getPendingRequests().contains(testInput));

        Event<ElevatorEventType> testEvent = context.getElevatorBuffer().getEvent();

        assertEquals(ElevatorEventType.CLOSE_DOORS, testEvent.getEventType());
        assertNull(testEvent.getPayload());
        assertTrue(newState instanceof SchedulerProcessingState);
    }
}
