/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package test.elevator_subsystem.states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.states.ElevatorApproachingFloorsState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsClosedState;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpenState;
import sysc_3303_project.elevator_subsystem.states.ElevatorMovingState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * @author Liam Gaudet and Ian Holmes
 *
 */
public class ElevatorApproachingFloorsStateTest {

	/**
	 * Tests reaction when the invalid event "closeDoors" is triggered
	 */
    @Test
    public void testCloseDoors() {
        ElevatorState testState = new ElevatorApproachingFloorsState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoors);

        String expectedMessage = "closeDoors() must be called from the ElevatorDoorsOpenState.";

        assertEquals(expectedMessage, e.getMessage());

    }
    
    /**
	 * Tests reaction when the invalid event "openDoors" is triggered
	 */
    @Test
    public void testOpenDoors() {
        ElevatorState testState = new ElevatorApproachingFloorsState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoors);

        String expectedMessage = "openDoors() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
	/**
	 * Tests reaction when the invalid event "setDirection" is triggered
	 */
    @Test
    public void testSetDirection() {
        ElevatorState testState = new ElevatorDoorsOpenState(null);

        Exception e = assertThrows(IllegalStateException.class, () -> {
            testState.setDirection(null);
        });

        String expectedMessage = "setDirection() must be called from the ElevatorDoorsClosedState.";

        assertEquals(expectedMessage, e.getMessage());
    }
    
    /**
	 * Tests reaction when the invalid event "openDoors" is triggered
	 */
    @Test
    public void testStopAtNextFloor() {
    	EventBuffer<SchedulerEventType> schedulerBuffer = new EventBuffer<SchedulerEventType>();
    	Elevator context = new Elevator(schedulerBuffer, null, 0);
    	context.setDirection(Direction.UP);
        ElevatorState testState = new ElevatorApproachingFloorsState(context);

        ElevatorState newState = testState.stopAtNextFloor();
        
        assertTrue(newState instanceof ElevatorDoorsClosedState);
        assertEquals(1, context.getFloor());
        
        Event<SchedulerEventType> suppliedEvent = schedulerBuffer.getEvent();
        
        assertEquals(1, suppliedEvent.getPayload());
        
        assertEquals(SchedulerEventType.ELEVATOR_STOPPED, suppliedEvent.getEventType());

    }
    
    /**
	 * Tests reaction when the invalid event "openDoors" is triggered
	 */
    @Test
    public void testContinueMoving() {
    	
    	Elevator context = new Elevator(null, null, 0);
    	context.setDirection(Direction.UP);
        ElevatorState testState = new ElevatorApproachingFloorsState(context);

        ElevatorState newState = testState.continueMoving();
        
        assertTrue(newState instanceof ElevatorMovingState);
        assertEquals(1, context.getFloor());

    }
    
    /**
	 * Tests reaction when the invalid event "openDoorsTimer" is triggered
	 */
    @Test
    public void testOpenDoorsTimer() {
        ElevatorState testState = new ElevatorApproachingFloorsState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::openDoorsTimer);

        String expectedMessage = "openDoorsTimer() must be called from the ElevatorDoorsOpeningState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
    /**
	 * Tests reaction when the invalid event "travelThroughFloorsTimer" is triggered
	 */
    @Test
    public void testTravelThroughFloorTimer() {
        ElevatorState testState = new ElevatorApproachingFloorsState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::travelThroughFloorsTimer);

        String expectedMessage = "travelThroughFloorsTimer() must be called from the ElevatorMovingState.";

        assertEquals(expectedMessage, e.getMessage());


    }
    
    /**
	 * Tests reaction when the invalid event "closeDoorsTimer" is triggered
	 */
    @Test
    public void testCloseDoorsTimer() {
        ElevatorState testState = new ElevatorApproachingFloorsState(null);

        Exception e = assertThrows(IllegalStateException.class, testState::closeDoorsTimer);

        String expectedMessage = "closeDoorsTimer() must be called from the ElevatorDoorsClosingState.";

        assertEquals(expectedMessage, e.getMessage());


    }

}
