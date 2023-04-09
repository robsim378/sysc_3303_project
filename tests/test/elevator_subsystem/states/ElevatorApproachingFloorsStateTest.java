/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package test.elevator_subsystem.states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.states.ElevatorApproachingFloorsState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosedState;
import sysc_3303_project.elevator_subsystem.states.ElevatorMovingState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorApproachingFloorsStateTest extends ElevatorStateTest{

    
    
    /**
     * Tests reaction when the invalid event "openDoors" is triggered
     */
    @Override
    @Test
    public void testStopAtNextFloorEvent() {
        EventBuffer<Enum<?>> schedulerBuffer = new EventBuffer<>();
        Elevator context = new Elevator(schedulerBuffer, null, 0);
        context.setDirection(Direction.UP);
        
        Event<Enum<?>> testEvent = context.getOutputBuffer().getEvent();
        assertEquals(FloorEventType.UPDATE_ELEVATOR_DIRECTION, testEvent.getEventType());
        assertEquals(Direction.UP, testEvent.getPayload());

        ElevatorState testState = new ElevatorApproachingFloorsState(context);

        ElevatorState newState = testState.stopAtNextFloor();
        
        assertTrue(newState instanceof ElevatorDoorsClosedState);
        assertEquals(1, context.getFloor());
        
        assertEquals(GuiEventType.DIRECTIONAL_LAMP_STATUS_CHANGE, context.getOutputBuffer().getEvent().getEventType());
        assertEquals(GuiEventType.ELEVATOR_AT_FLOOR, context.getOutputBuffer().getEvent().getEventType());
        
        Event<Enum<?>> suppliedEvent = schedulerBuffer.getEvent();
        
        assertEquals(1, suppliedEvent.getPayload());
        assertEquals(SchedulerEventType.ELEVATOR_STOPPED, suppliedEvent.getEventType());

    }
    
    /**
     * Tests reaction when the invalid event "openDoors" is triggered
     */
    @Override
    @Test
    public void testContinueMovingEvent() {
        EventBuffer<Enum<?>> outputBuffer = new EventBuffer<>();

        Elevator context = new Elevator(outputBuffer, null, 0);
        context.setDirection(Direction.UP);
        
        Event<Enum<?>> testEvent = context.getOutputBuffer().getEvent();
        assertEquals(FloorEventType.UPDATE_ELEVATOR_DIRECTION, testEvent.getEventType());
        assertEquals(Direction.UP, testEvent.getPayload());

        
        ElevatorState testState = new ElevatorApproachingFloorsState(context);

        ElevatorState newState = testState.continueMoving();
        
        assertTrue(newState instanceof ElevatorMovingState);
        assertEquals(1, context.getFloor());

    }

	@Override
	protected ElevatorState getState(Elevator context) {
		return new ElevatorApproachingFloorsState(context);
	}
    

}
