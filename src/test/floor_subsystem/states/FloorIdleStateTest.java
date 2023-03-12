/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package test.floor_subsystem.states;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.events.RequestData;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.floor_subsystem.FloorSystem;
import sysc_3303_project.floor_subsystem.InputFileController;
import sysc_3303_project.floor_subsystem.states.FloorIdleState;
import sysc_3303_project.floor_subsystem.states.FloorState;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class for testing the Floor System state machine
 * @author Liam Gaudet
 */
public class FloorIdleStateTest {
	private InputFileController fileController;
	EventBuffer<Enum<?>> outgoingBuffer;
	FloorSystem floor;
	ArrayList<FloorSystem> floors = new ArrayList<FloorSystem>();

	@Before
	public void setup() {
		// Create the outgoing message buffer shared by all floors
		outgoingBuffer = new EventBuffer<>();

		EventBuffer<FloorEventType> floorBuffer = new EventBuffer<FloorEventType>();
		floor = new FloorSystem(0, floorBuffer, outgoingBuffer);

		floors.add(floor);

		String floorFilePath = new File("").getAbsolutePath() + "/resources/testing_examples";
		fileController = new InputFileController(floorFilePath, floors);

		// Create all necessary threads
		Thread inputFileThread = new Thread(fileController);
		Thread floorThread = new Thread(floor);
		floorThread.start();
		inputFileThread.start();
		
	}


	/**
	 * Only one flow of events so only one test
	 */
	@Test
	public void testFloorIdleStateButtonPress() {
		
		setup();
		
		// The requestData to test with
		RequestData testInput = new RequestData(LocalTime.NOON, 2, Direction.DOWN, 4);

		FloorState newState = floor.getState().handleButtonPressed(testInput);

		Event<?> testOutput = floor.getOutputBuffer().getEvent();


		assertEquals(Subsystem.SCHEDULER,  testOutput.getDestinationSubsystem());
		assertEquals(0,  testOutput.getDestinationID());
		assertEquals(Subsystem.FLOOR,  testOutput.getSourceSubsystem());
		assertEquals(0,  testOutput.getSourceID());
		assertEquals(SchedulerEventType.FLOOR_BUTTON_PRESSED,  testOutput.getEventType());
		assertEquals(Direction.DOWN, (Direction) testOutput.getPayload());

		assertTrue(newState instanceof FloorIdleState);
	}
}
