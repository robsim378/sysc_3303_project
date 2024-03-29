
package test.elevator_subsystem;

import org.junit.Before;
import org.junit.Test;

import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.Elevator;
import sysc_3303_project.elevator_subsystem.ElevatorEventType;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;



/**
 *	@author Khalid Merai, Andrei Popescu
 *	This class includes tests for the Elevator class.
 * 	Due to the visibility restrictions on Elevator's methods/fields,
 * 	reflection is used to access them.
 */
public class ElevatorTest {

	private EventBuffer<Enum<?>> outputBuffer;
	private EventBuffer<ElevatorEventType> eventBuffer;
	private Elevator elevator;
	@Before
	public void setUp() {
		// Initialize the scheduler and event buffers, and the elevator
		outputBuffer = new EventBuffer<>();
		eventBuffer = new EventBuffer<>();
		elevator = new Elevator(outputBuffer, eventBuffer, 1);
	}

		/**
		 * Test the getElevatorID method
		 */
		@Test
		public void testGetElevatorID() {
			// Ensure that the elevator ID is correctly returned
			assertEquals(1, elevator.getElevatorID());
		}

		/**
		 * Test the getFloor method
		 */
		@Test
		public void testGetFloor() {
			// Ensure that the current floor is initially 0
			assertEquals(0, elevator.getFloor());
		}

		/**
		 * Test the setDirection and getDirection methods
		 */
		@Test
		public void testSetDirection() {
			// Set the elevator's direction to UP and ensure that it is correctly returned
			elevator.setDirection(Direction.UP);
			assertEquals(Direction.UP, elevator.getDirection());
		}

	/**
	 * Tests that the moveElevator method properly updates the elevator floor.
	 */
	@Test
	public void testMoveElevator() {
		// Move the elevator up one floor and ensure that the current floor is correctly updated
		elevator.setDirection(Direction.UP);
		elevator.moveElevator();
		assertEquals(1, elevator.getFloor());

		// Change the elevator's direction to DOWN and move it down one floor,
		// ensuring that the current floor is correctly updated
		elevator.setDirection(Direction.DOWN);
		elevator.moveElevator();
		assertEquals(0, elevator.getFloor());
	}



	/**
		 * Test the elevator state machine by simulating a series of events and
		 * ensuring that the elevator's state transitions and event processing
		 * behave as expected
		 */
		@Test
		public void testElevatorStateMachine() throws InterruptedException {
			// Start the elevator thread
			Thread elevatorThread = new Thread(elevator);
			elevatorThread.start();

			// Close the elevator doors and ensure that the close doors event is generated
			eventBuffer.addEvent(new Event<>(Subsystem.ELEVATOR, 1, Subsystem.SCHEDULER, 0, ElevatorEventType.CLOSE_DOORS, null));
			TimeUnit.MILLISECONDS.sleep(500);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.ELEVATOR_DOOR_STATUS_CHANGE);
			assertEquals(outputBuffer.getEvent().getEventType(), SchedulerEventType.ELEVATOR_PING);
			assertEquals(outputBuffer.getEvent().getEventType(), SchedulerEventType.ELEVATOR_DOORS_CLOSED);

			// Start moving the elevator up and ensure that the moving timer event is generated,
			// and that the elevator's direction is correctly set to UP
			eventBuffer.addEvent(new Event<>(Subsystem.ELEVATOR, 1, Subsystem.SCHEDULER, 0, ElevatorEventType.START_MOVING_IN_DIRECTION, Direction.UP));
			TimeUnit.MILLISECONDS.sleep(500);
			assertEquals(elevator.getDirection(), Direction.UP);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.ELEVATOR_DOORS_FAULT);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.ELEVATOR_DOOR_STATUS_CHANGE);
			assertEquals(outputBuffer.getEvent().getEventType(), FloorEventType.UPDATE_ELEVATOR_DIRECTION);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.DIRECTIONAL_LAMP_STATUS_CHANGE);
			assertEquals(outputBuffer.getEvent().getEventType(), SchedulerEventType.ELEVATOR_APPROACHING_FLOOR);
			
			// Continue moving
			eventBuffer.addEvent(new Event<>(Subsystem.ELEVATOR, 1, Subsystem.SCHEDULER, 0, ElevatorEventType.CONTINUE_MOVING, null));
			TimeUnit.MILLISECONDS.sleep(500);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.ELEVATOR_AT_FLOOR);
			assertEquals(outputBuffer.getEvent().getEventType(), SchedulerEventType.ELEVATOR_APPROACHING_FLOOR);
			
			// Stop the elevator at the next floor
			eventBuffer.addEvent(new Event<>(Subsystem.ELEVATOR, 1, Subsystem.SCHEDULER, 0, ElevatorEventType.STOP_AT_NEXT_FLOOR, null));
			TimeUnit.MILLISECONDS.sleep(500);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.ELEVATOR_AT_FLOOR);
			assertEquals(outputBuffer.getEvent().getEventType(), SchedulerEventType.ELEVATOR_STOPPED);

			// Open the elevator doors
			eventBuffer.addEvent(new Event<>(Subsystem.ELEVATOR, 1, Subsystem.SCHEDULER, 0, ElevatorEventType.OPEN_DOORS, null));
			TimeUnit.MILLISECONDS.sleep(500);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.ELEVATOR_LAMP_STATUS_CHANGE);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.ELEVATOR_DOOR_STATUS_CHANGE);
			assertEquals(outputBuffer.getEvent().getEventType(), GuiEventType.ELEVATOR_DOOR_STATUS_CHANGE);
			assertEquals(outputBuffer.getEvent().getEventType(), SchedulerEventType.ELEVATOR_PING);
			assertEquals(outputBuffer.getEvent().getEventType(), SchedulerEventType.ELEVATOR_DOORS_OPENED);
		}
	}