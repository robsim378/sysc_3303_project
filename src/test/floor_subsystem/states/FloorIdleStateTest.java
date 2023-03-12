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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import sysc_3303_project.common.Direction;
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
	private int numFloors;
	EventBuffer<Enum<?>> outgoingBuffer;
	FloorSystem floor;
	ArrayList<FloorSystem> floors;

	@BeforeAll
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
		// The requestData to test with
		RequestData testInput = new RequestData(LocalTime.NOON, 2, Direction.DOWN, 4);





//		String floorFilePath = new File("").getAbsolutePath() + "\\resources-test\\testSingularEntry.txt";
//		fileController = new InputFileController(floorFilePath, floors);
//
//		// Fixing the visibility of the method and calling it
//		@SuppressWarnings("rawtypes")
//		Class[] emptyArgs = new Class[0];
//		ArrayList<RequestData> requestData = null;
//		Method method = null;
//
//		try {
//			method = InputFileController.class.getDeclaredMethod("parseData", emptyArgs);
//			method.setAccessible(true);
//			requestData = (ArrayList<RequestData>) method.invoke(fileController);
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail();
//		}





		FloorState newState = floor.getState().handleButtonPressed(testInput);

		RequestData testOutput = (RequestData)floor.getOutputBuffer().getEvent().getPayload();



		assertEquals(LocalTime.NOON, testOutput.getRequestTime());
		assertEquals(2, testOutput.getCurrentFloor());
		assertEquals(Direction.DOWN, testOutput.getDirection());
		assertEquals(4, testOutput.getDestinationFloor());
		assertTrue(newState instanceof FloorIdleState);
	}
}
