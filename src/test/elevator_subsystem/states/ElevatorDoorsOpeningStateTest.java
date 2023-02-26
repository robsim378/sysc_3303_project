package test.elevator_subsystem.states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpenState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpeningState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

public class ElevatorDoorsOpeningStateTest {

	/**
	 * Tests reaction when the invalid event "CloseDoors" is triggered
	 */
    @Test
    public void testCloseDoors() {
        ElevatorState testState = new ElevatorDoorsOpeningState(null);
        
        Exception e = assertThrows(IllegalStateException.class, testState::closeDoors);

        String expectedMessage = "closeDoors() must be called from the ElevatorDoorsOpenState.";

        assertEquals(expectedMessage, e.getMessage());

    }
    
	/**
	 * Tests reaction when the invalid event "openDoors" is triggered
	 */
    @Test
    public void testOpenDoors() {
        ElevatorState testState = new ElevatorDoorsOpeningState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoors);

        String expectedMessage = "openDoors() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "setDirection" is triggered
	 */
    @Test
    public void testSetDirection() {
        ElevatorState testState = new ElevatorDoorsOpeningState(null);

        Exception e = assertThrows(IllegalStateException.class, () -> {
            testState.setDirection(null);
        });

        String expectedMessage = "setDirection() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "stopAtNextFloor" is triggered
	 */
    @Test
    public void testStopAtNextFloor() {
        ElevatorState testState = new ElevatorDoorsOpeningState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::stopAtNextFloor);

        String expectedMessage = "stopAtNextFloor() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "continueMoving" is triggered
	 */
    @Test
    public void testContinueMoving() {
        ElevatorState testState = new ElevatorDoorsOpeningState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::continueMoving);

        String expectedMessage = "continueMoving() must be called from the ElevatorApproachingFloorsState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the valid event "openDoorsTimer" is triggered
	 */
    @Test
    public void testOpenDoorsTimer() {
    	EventBuffer<SchedulerEventType> schedulerBuffer = new EventBuffer<SchedulerEventType>();
    	Elevator context = new Elevator(schedulerBuffer, null, 0);
        ElevatorState testState = new ElevatorDoorsOpeningState(context);
        
        ElevatorState newState = testState.openDoorsTimer();
        
        assertTrue(newState instanceof ElevatorDoorsOpenState);
        
        @SuppressWarnings("rawtypes")
		Event addedEvent = (schedulerBuffer.getEvent());
        
        assertEquals(SchedulerEventType.ELEVATOR_DOORS_OPENED, addedEvent.getEventType());
        assertNull(addedEvent.getPayload());
        assertEquals(context, addedEvent.getSender());

    }
    
	/**
	 * Tests reaction when the invalid event "travelThroughFloorsTimer" is triggered
	 */
    @Test
    public void testTravelThroughFloorTimer() {
        ElevatorState testState = new ElevatorDoorsOpeningState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::travelThroughFloorsTimer);

        String expectedMessage = "travelThroughFloorsTimer() must be called from the ElevatorMovingState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "closeDoorsTimer" is triggered
	 */
    @Test
    public void testCloseDoorsTimer() {
        ElevatorState testState = new ElevatorDoorsOpeningState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoorsTimer);

        String expectedMessage = "closeDoorsTimer() must be called from the ElevatorDoorsClosingState.";

        assertEquals(expectedMessage, e.getMessage());

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
