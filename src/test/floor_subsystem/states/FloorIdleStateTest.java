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
		
		assertEquals(LocalTime.NOON, ((RequestData)buffer.getEvent().getPayload()).getRequestTime());
		assertEquals(2, ((RequestData)buffer.getEvent().getPayload()).getCurrentFloor());
		assertEquals(Direction.DOWN, ((RequestData)buffer.getEvent().getPayload()).getDirection());
		assertEquals(4, ((RequestData)buffer.getEvent().getPayload()).getDestinationFloor());
		assertTrue(newState instanceof FloorIdleState);
	}
}
