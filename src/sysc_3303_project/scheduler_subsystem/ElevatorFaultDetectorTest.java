package sysc_3303_project.scheduler_subsystem;
import org.junit.jupiter.api.Test;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.scheduler_subsystem.ElevatorFaultDetector;
import sysc_3303_project.scheduler_subsystem.Scheduler;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorFaultDetectorTest {

    @Test
    void testAddAndClearTimers() {
        EventBuffer<SchedulerEventType> schedulerEventBuffer = new EventBuffer<>();
        EventBuffer<Enum<?>> elevatorEventBuffer = new EventBuffer<>();
        Scheduler scheduler = new Scheduler(schedulerEventBuffer, elevatorEventBuffer);
        ElevatorFaultDetector faultDetector = new ElevatorFaultDetector(scheduler);

        // Add a timer for elevator 1 with a delay of 500 milliseconds
        faultDetector.addTimer(1, 500);

        // Verify that the timer was added
        assertEquals(1, faultDetector.elevatorMoveTimers.size());
        assertEquals(1, faultDetector.elevatorMoveTimers.get(0).getElevatorId());

        // Clear the timers for elevator 1
        faultDetector.clearTimers(1);

        // Verify that the timer was cleared
        assertEquals(0, faultDetector.elevatorMoveTimers.size());
    }

}
