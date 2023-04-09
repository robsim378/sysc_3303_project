/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package test.elevator_subsystem.states;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosingState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpenState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.gui_subsystem.transfer_data.DoorStatus;
import sysc_3303_project.scheduler_subsystem.Scheduler;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorDoorsOpenStateTest extends ElevatorStateTest{

    /**
     * Tests reaction when the valid event "closeDoors" is triggered
     */
	@Override
    @Test
    public void testCloseDoorsEvent() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);
        
        ElevatorState newState = testState.closeDoors();
        
        assertTrue(newState instanceof ElevatorDoorsClosingState);
    }
	
	@Test
    public void testCloseDoorsUnloading() {
		Elevator context = new Elevator(new EventBuffer<>(), new EventBuffer<>(), 0);
        ElevatorState testState = new ElevatorDoorsOpenState(context);
        testState.doEntry();
        Event<Enum<?>> testEvent = context.getOutputBuffer().getEvent();
        		
        assertEquals(GuiEventType.ELEVATOR_DOOR_STATUS_CHANGE, testEvent.getEventType());
        assertEquals(DoorStatus.DOORS_OPEN, testEvent.getPayload());
        
        ElevatorState newState = testState.closeDoors();
        
        assertNull(newState);
    }


    /**
     * Tests reaction when the valid event "candlePassengersUnloaded" is triggered
     */
    @Test
    public void testHandlePassengersUnloaded() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        ElevatorState newState = testState.handlePassengersUnloaded();

        assertNull(newState);
    }
    
    /**
     * Tests reaction when the event "openDoorsTimer" is triggered
     */
    @Override
    @Test
    public void testCloseDoorsTimer() {
        testState = getState(null);
        ElevatorState newState = testState.closeDoorsTimer();
        assertNull(newState);
    }


	@Override
	protected ElevatorState getState(Elevator context) {
		return new ElevatorDoorsOpenState(context);
	}

}
