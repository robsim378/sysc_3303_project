/**
 * SYSC3303 Project
 * Group 1
 * @version 2.0
 */

package test.floor_subsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import sysc_3303_project.common.RequestData;
import sysc_3303_project.floor_subsystem.FloorSystem;

/**
 * A class for testing the Floor System mechanics
 * @author Liam Gaudet
 */
class FloorSystemTest {

	@SuppressWarnings("unchecked")
	@Test
	void testSingularEntry() {
		
        String floorFilePath = new File("").getAbsolutePath() + "\\resources-test\\testSingularEntry.txt";
		FloorSystem newFloorSystem = new FloorSystem(null, null, floorFilePath);
		ArrayList<RequestData> requestData = null;

		// Fixing the visibility of the method and calling it
        @SuppressWarnings("rawtypes")
		Class[] emptyArgs = new Class[0];
        
        Method method = null;
        
		try {
			method = FloorSystem.class.getDeclaredMethod("parseData", emptyArgs);
			method.setAccessible(true);
			requestData = (ArrayList<RequestData>) method.invoke(newFloorSystem);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
				
		// Now for the actual tests
		assertEquals(1, requestData.size());
		RequestData current = requestData.get(0);
		assertEquals("00:00:07.001 8 DOWN 2", current.toString());
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	void testMultipleEntry() {
		
        String floorFilePath = new File("").getAbsolutePath() + "\\resources-test\\testMultipleEntries.txt";
		FloorSystem newFloorSystem = new FloorSystem(null, null, floorFilePath);
		ArrayList<RequestData> requestData = null;

		// Fixing the visibility of the method and calling it
        @SuppressWarnings("rawtypes")
		Class[] emptyArgs = new Class[0];
        
        Method method = null;
        
		try {
			method = FloorSystem.class.getDeclaredMethod("parseData", emptyArgs);
			method.setAccessible(true);
			requestData = (ArrayList<RequestData>) method.invoke(newFloorSystem);
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
	
	@SuppressWarnings("unchecked")
	@Test
	void testInvalidEntries() {
		
        String floorFilePath = new File("").getAbsolutePath() + "\\resources-test\\testInvalidEntries.txt";
		FloorSystem newFloorSystem = new FloorSystem(null, null, floorFilePath);
		ArrayList<RequestData> requestData = null;
		
		// Fixing the visibility of the method and calling it
        @SuppressWarnings("rawtypes")
		Class[] emptyArgs = new Class[0];
        
        Method method = null;
        
		try {
			method = FloorSystem.class.getDeclaredMethod("parseData", emptyArgs);
			method.setAccessible(true);
			requestData = (ArrayList<RequestData>) method.invoke(newFloorSystem);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
				
		// Now for the actual tests
		assertEquals(1, requestData.size());
		RequestData current = requestData.get(0);
		assertEquals("00:00:02.001 3 DOWN 2", current.toString());
		
	}

}
