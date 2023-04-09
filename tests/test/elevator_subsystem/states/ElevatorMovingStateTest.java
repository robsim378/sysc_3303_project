/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package test.elevator_subsystem.states;

import org.junit.jupiter.api.Test;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.elevator_subsystem.states.*;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorMovingStateTest extends ElevatorStateTest{

    /**
     * Tests reaction when the valid event "travelThroughFloorsTimer" is triggered
     */
    @Test
    public void testTravelThroughFloorsTimerEvent() {
        EventBuffer<Enum<?>> schedulerBuffer = new EventBuffer<>();
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();

        Elevator testContext = new Elevator(schedulerBuffer, contextBuffer, 0);

        testContext.setDirection(Direction.UP);

        Event<Enum<?>> testEvent = testContext.getOutputBuffer().getEvent();
        assertEquals(FloorEventType.UPDATE_ELEVATOR_DIRECTION, testEvent.getEventType());
        assertEquals(Direction.UP, testEvent.getPayload());
        
        ElevatorState testState = new ElevatorMovingState(testContext);

        ElevatorState newState = testState.travelThroughFloorsTimer();

        assertEquals(GuiEventType.DIRECTIONAL_LAMP_STATUS_CHANGE, testContext.getOutputBuffer().getEvent().getEventType());
        
        testEvent = testContext.getOutputBuffer().getEvent();
        
        assertEquals(SchedulerEventType.ELEVATOR_APPROACHING_FLOOR, testEvent.getEventType());
        assertEquals(1, testEvent.getPayload());
        assertTrue(newState instanceof ElevatorApproachingFloorsState);
    }

    /**
     * Tests on entry for state
     */
    @Test
    public void testOnEntry() {
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<>();
        Elevator context = new Elevator(null, contextBuffer, 0);
        ElevatorState testState = new ElevatorMovingState(context);

        testState.doEntry();

        Event<ElevatorEventType> newEvent = contextBuffer.getEvent();

        assertEquals(ElevatorEventType.MOVING_TIMER, newEvent.getEventType());
        assertNull(newEvent.getPayload());
    }

	@Override
	protected ElevatorState getState(Elevator context) {
		return new ElevatorMovingState(context);
	}
}
