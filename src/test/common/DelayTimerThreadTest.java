package test.common;


import org.junit.Test;

import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.DelayedEvent;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;

import static org.junit.Assert.*;

public class DelayTimerThreadTest {

    @Test
    public void testRun() throws InterruptedException {
        EventBuffer<TestEnum> buffer = new EventBuffer<>();
        Event<TestEnum> event = new Event<TestEnum>(Subsystem.ELEVATOR, 0, Subsystem.ELEVATOR, 0, TestEnum.VALUE1, "Payload");
        DelayedEvent<TestEnum> timer = new DelayedEvent<>(100, event, buffer);
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






