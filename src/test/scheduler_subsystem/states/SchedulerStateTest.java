package test.scheduler_subsystem.states;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import sysc_3303_project.scheduler_subsystem.states.SchedulerState;
import sysc_3303_project.scheduler_subsystem.states.SchedulerWaitingState;

public class SchedulerStateTest {

	private SchedulerState testState;
    /**
     * Tests reaction when the invalid event "handleElevatorDoorsClosedTest" is triggered
     */
    @Test
    public void handleElevatorDoorsClosedTest() {
    	testState = new SchedulerWaitingState(null);

        assertThrows(IllegalStateException.class, () -> {
            testState.handleElevatorDoorsClosed(0,0);
        });
    }

    /**
     * Tests reaction when the invalid event "handleElevatorDoorsOpenTest" is triggered
     */
    @Test
    public void handleElevatorDoorsOpenTest() {
        testState = new SchedulerWaitingState(null);

        assertThrows(IllegalStateException.class, () -> {
            testState.handleElevatorDoorsClosed(0,0);
        });
    }

    /**
     * Tests reaction when the invalid event "handleElevatorStoppedTest" is triggered
     */
    @Test
    public void handleElevatorStoppedTest() {
        testState = new SchedulerWaitingState(null);

        assertThrows(IllegalStateException.class, () -> {
            testState.handleElevatorDoorsClosed(0,0);
        });
    }

    /**
     * Tests reaction when the invalid event "handleElevatorApproachingFloorTest" is triggered
     */
    @Test
    public void handleElevatorApproachingFloorTest() {
        testState = new SchedulerWaitingState(null);

        assertThrows(IllegalStateException.class, () -> {
            testState.handleElevatorDoorsClosed(0,0);
        });
    }

    /**
     * Tests reaction when the valid event "handleFloorButtonPressed" is triggered
     */
    @Test
    public void handleFloorButtonPressed() {
        testState = new SchedulerWaitingState(null);

        assertThrows(IllegalStateException.class, () -> {
            testState.handleFloorButtonPressed(0, null);
        });
    }

}
