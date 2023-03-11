package test.common;


import org.junit.Test;
import sysc_3303_project.common.DelayTimerThread;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.Subsystem;

import static org.junit.Assert.*;

public class DelayTimerThreadTest {

    @Test
    public void testRun() throws InterruptedException {
        EventBuffer<TestEnum> buffer = new EventBuffer<>();
        Event<TestEnum> event = new Event<TestEnum>(Subsystem.ELEVATOR, 0, Subsystem.ELEVATOR, 0, TestEnum.VALUE1, "Payload");
        DelayTimerThread<TestEnum> timer = new DelayTimerThread<>(100, event, buffer);
        Thread thread = new Thread(timer);

        thread.start();
        thread.join();

        // Check that the event was added to the buffer after the delay
        assertEquals(event, buffer.getEvent());
    }
    
    
    private enum TestEnum {
    	VALUE1,
    	VALUE2
    }
}






