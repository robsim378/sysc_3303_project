/**
 * 
 */
package test.scheduler_subsystem;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.lang.NullPointerException;

import sysc_3303_project.common.Direction;
import sysc_3303_project.scheduler_subsystem.ElevatorTracker;
import sysc_3303_project.scheduler_subsystem.LoadRequest;
/**
 * @author Andrei Popescu
 *
 */
public class ElevatorTrackerTest {

	private ElevatorTracker tracker;
	
	@Before
	public void setUp() {
		tracker = new ElevatorTracker(2);
    }
	
	@Test
	public void getElevatorFloorTest() {
		assertEquals(0, tracker.getElevatorFloor(0));
		assertEquals(0, tracker.getElevatorFloor(1));
		assertThrows(NullPointerException.class, () -> tracker.getElevatorFloor(2));
    }
	
	@Test
	public void getElevatorDirectionTest() {
		assertEquals(null, tracker.getElevatorDirection(0));
		assertThrows(NullPointerException.class, () -> tracker.getElevatorFloor(2));
	}
	
	@Test
	public void getElevatorRequestCountTest() {
		assertEquals(0, tracker.getElevatorRequestCount(0));
		tracker.addLoadRequest(0, new LoadRequest(0, Direction.UP));
		tracker.addUnloadRequest(0, 0);
		assertEquals(2, tracker.getElevatorRequestCount(0));
	}
	
	@Test
	public void updateElevatorDirectionTest() {
		assertEquals(null, tracker.getElevatorDirection(0));
		tracker.updateElevatorDirection(0, Direction.DOWN);
		assertEquals(Direction.DOWN, tracker.getElevatorDirection(0));
	}
	
	@Test
	public void updateElevatorFloorTest() {
		assertEquals(0, tracker.getElevatorFloor(0));
		tracker.updateElevatorFloor(0, 4);
		assertEquals(4, tracker.getElevatorFloor(0));
	}
	
	@Test
	public void checkAddLoadRequestTest() {
		assertFalse(tracker.hasRequests(0));
		assertEquals(0, tracker.getElevatorRequestCount(0));
		tracker.addLoadRequest(0, new LoadRequest(0, Direction.UP));
		assertEquals(1, tracker.getElevatorRequestCount(0));
		assertTrue(tracker.hasRequests(0));
		assertTrue(tracker.hasLoadRequest(0, 0));
		assertTrue(tracker.hasLoadRequestInDirection(0, 0, Direction.UP));
		assertFalse(tracker.hasLoadRequestInDirection(0, 0, Direction.DOWN));
	}
	
	@Test
	public void checkAddUnloadRequestTest() {
		assertEquals(0, tracker.getElevatorRequestCount(0));
		tracker.addUnloadRequest(0, 2);
		assertEquals(1, tracker.getElevatorRequestCount(0));
		assertEquals(1, tracker.countUnloadRequests(0, 2));
		tracker.addUnloadRequest(0, 2);
		assertEquals(2, tracker.getElevatorRequestCount(0));
		assertEquals(2, tracker.countUnloadRequests(0, 2));
	}
	
	@Test
	public void loadElevatorTest() {
		tracker.addLoadRequest(0, new LoadRequest(0, Direction.UP));
		tracker.addLoadRequest(0, new LoadRequest(0, Direction.DOWN));
		tracker.updateElevatorDirection(0, Direction.DOWN);
		assertTrue(tracker.hasRequests(0));
		assertEquals(Direction.DOWN, tracker.loadElevator(0, 0));
		assertTrue(tracker.hasRequests(0));
		assertEquals(Direction.UP, tracker.loadElevator(0, 0));
		assertFalse(tracker.hasRequests(0));
		assertEquals(null, tracker.loadElevator(0, 0));
	}
	
	@Test
	public void unloadElevatorTest() {
		tracker.addUnloadRequest(0, 1);
		tracker.addUnloadRequest(0, 1);
		tracker.updateElevatorDirection(0, Direction.UP);
		assertTrue(tracker.hasRequests(0));
		assertEquals(2, tracker.unloadElevator(0, 1));
		assertEquals(0, tracker.unloadElevator(0, 0));
	}
}
