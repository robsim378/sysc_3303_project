/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */
package test.floor_subsystem.states;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.RequestData;
import sysc_3303_project.floor_subsystem.states.FloorIdleState;
import sysc_3303_project.floor_subsystem.states.FloorState;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * A class for testing the Floor System state machine
 * @author Liam Gaudet
 */
public class FloorIdleStateTest {
	
	/**
	 * Only one flow of events so only one test
	 */
	@Test
	public void testFloorIdleStateButtonPress() {
		FloorState testState = new FloorIdleState(null);
		
		EventBuffer<SchedulerEventType> buffer = new EventBuffer<SchedulerEventType>();
		
		RequestData testInput = new RequestData(LocalTime.NOON, 2, Direction.DOWN, 4);
		
		FloorState newState = testState.handleButtonPressed(testInput, buffer);
		
		RequestData testOutput = (RequestData )buffer.getEvent().getPayload();
		
		assertEquals(LocalTime.NOON, testOutput.getRequestTime());
		assertEquals(2, testOutput.getCurrentFloor());
		assertEquals(Direction.DOWN, testOutput.getDirection());
		assertEquals(4, testOutput.getDestinationFloor());
		assertTrue(newState instanceof FloorIdleState);
	}
}
