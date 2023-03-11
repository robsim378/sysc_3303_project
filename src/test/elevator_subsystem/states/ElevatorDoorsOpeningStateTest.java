/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package test.elevator_subsystem.states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpenState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpeningState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorDoorsOpeningStateTest extends ElevatorStateTest{

    
    /**
     * Tests reaction when the valid event "openDoorsTimer" is triggered
     */
    @Test
    public void testOpenDoorsTimer() {
        EventBuffer<Enum<?>> schedulerBuffer = new EventBuffer<>();
        Elevator context = new Elevator(schedulerBuffer, null, 0);
        ElevatorState testState = new ElevatorDoorsOpeningState(context);
        
        ElevatorState newState = testState.openDoorsTimer();
        
        assertTrue(newState instanceof ElevatorDoorsOpenState);
        
        @SuppressWarnings("rawtypes")
        Event addedEvent = (schedulerBuffer.getEvent());
        
        assertEquals(SchedulerEventType.ELEVATOR_DOORS_OPENED, addedEvent.getEventType());
        assertNull(addedEvent.getPayload());
    }

    /**
     * Tests on entry for state
     */
    @Test
    public void testOnEntry() {
        EventBuffer<ElevatorEventType> contextBuffer = new EventBuffer<ElevatorEventType>();
        Elevator context = new Elevator(null, contextBuffer, 0);
        ElevatorState testState = new ElevatorDoorsOpeningState(context);
        
        testState.doEntry();
        
        Event<ElevatorEventType> newEvent = contextBuffer.getEvent();
        
        assertEquals(ElevatorEventType.OPEN_DOORS_TIMER, newEvent.getEventType());
        
        assertNull(newEvent.getPayload());
    }
}
