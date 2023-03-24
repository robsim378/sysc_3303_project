/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */

package test.floor_subsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.events.RequestData;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.floor_subsystem.FloorSystem;
import sysc_3303_project.floor_subsystem.InputFileController;

/**
 * A class for testing the Floor System mechanics
 * @author Liam Gaudet, Robert Simionescu
 */
class FloorSystemTest {
	private InputFileController fileController;
	private int numFloors;
	EventBuffer<Enum<?>> outgoingBuffer;
	FloorSystem floor;
	ArrayList<FloorSystem> floors;


	@Before
	public void setup() {
		// Create the outgoing message buffer shared by all floors
		outgoingBuffer = new EventBuffer<>();

		EventBuffer<FloorEventType> floorBuffer = new EventBuffer<FloorEventType>();
		floor = new FloorSystem(0, floorBuffer, outgoingBuffer);

		floors = new ArrayList<FloorSystem>();
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
	 * Test method to check parsing of a file with a single valid entry.
	 * Parses the file using the private method "parseData" through reflection,
	 * and checks if the parsed data matches the expected output.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testSingularEntry() {
		String floorFilePath = new File("").getAbsolutePath() + "\\resources-test\\testSingularEntry.txt";
		fileController = new InputFileController(floorFilePath, floors);

		// Fixing the visibility of the method and calling it
		@SuppressWarnings("rawtypes")
		Class[] emptyArgs = new Class[0];
		ArrayList<RequestData> requestData = null;
		Method method = null;

		try {
			method = InputFileController.class.getDeclaredMethod("parseData", emptyArgs);
			method.setAccessible(true);
			requestData = (ArrayList<RequestData>) method.invoke(fileController);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Now for the actual tests
		assertEquals(1, requestData.size());
		RequestData current = requestData.get(0);
		assertEquals("00:00:07.001 0 UP 2", current.toString());

	}

	/**
	 * Test method to check parsing of a file with multiple valid entries.
	 * Parses the file using the private method "parseData" through reflection,
	 * and checks if the parsed data matches the expected output.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testMultipleEntry() {

		String floorFilePath = new File("").getAbsolutePath() + "\\resources-test\\testMultipleEntries.txt";
		fileController = new InputFileController(floorFilePath, floors);

		// Fixing the visibility of the method and calling it
		@SuppressWarnings("rawtypes")
		Class[] emptyArgs = new Class[0];
		ArrayList<RequestData> requestData = null;
		Method method = null;

		try {
			method = InputFileController.class.getDeclaredMethod("parseData", emptyArgs);
			method.setAccessible(true);
			requestData = (ArrayList<RequestData>) method.invoke(fileController);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		// Now for the actual tests
		assertEquals(3, requestData.size());
		RequestData current = requestData.get(0);
		assertEquals("00:00:00.001 1 UP 2", current.toString());
		current = requestData.get(1);
		assertEquals("00:00:01.001 2 UP 6", current.toString());
		current = requestData.get(2);
		assertEquals("00:00:02.001 3 DOWN 2", current.toString());
	}

	/**
	 * Test method to check parsing of a file with a mix of valid and invalid entries.
	 * Parses the file using the private method "parseData" through reflection,
	 * and checks if the parsed data matches the expected output.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testInvalidEntries() {

		String floorFilePath = new File("").getAbsolutePath() + "\\resources-test\\testInvalidEntries.txt";
		fileController = new InputFileController(floorFilePath, floors);

		// Fixing the visibility of the method and calling it
		@SuppressWarnings("rawtypes")
		Class[] emptyArgs = new Class[0];
		ArrayList<RequestData> requestData = null;
		Method method = null;

		try {
			method = InputFileController.class.getDeclaredMethod("parseData", emptyArgs);
			method.setAccessible(true);
			requestData = (ArrayList<RequestData>) method.invoke(fileController);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Now for the actual tests
		assertEquals(1, requestData.size());
		RequestData current = requestData.get(0);
		assertEquals("00:00:02.001 3 DOWN 2", current.toString());

	}
	
	/**
	 * Test method to check parsing of a file with a single valid entry.
	 * Parses the file using the private method "parseData" through reflection,
	 * and checks if the parsed data matches the expected output.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testErrorInitiatingEntry() {
		String floorFilePath = new File("").getAbsolutePath() + "\\resources-test\\testErrorInitiatingEntry.txt";
		fileController = new InputFileController(floorFilePath, floors);

		// Fixing the visibility of the method and calling it
		@SuppressWarnings("rawtypes")
		Class[] emptyArgs = new Class[0];
		ArrayList<RequestData> requestData = null;
		Method method = null;

		try {
			method = InputFileController.class.getDeclaredMethod("parseData", emptyArgs);
			method.setAccessible(true);
			requestData = (ArrayList<RequestData>) method.invoke(fileController);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Now for the actual tests
		assertEquals(1, requestData.size());
		RequestData current = requestData.get(0);
		assertEquals("00:00:02.001 3 DOWN 2 2", current.toString());
	}


}
