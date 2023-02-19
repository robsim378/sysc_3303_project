package test;


import org.junit.Test;
import sysc_3303_project.common.DelayTimerThread;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;

import static org.junit.Assert.*;

public class DelayTimerThreadTest {

    @Test
    public void testRun() throws InterruptedException {
        EventBuffer<String> buffer = new EventBuffer<>();
        Event<String> event = new Event<>("Test event", this, "Payload");
        DelayTimerThread<String> timer = new DelayTimerThread<>(100, event, buffer);
        Thread thread = new Thread(timer);

        thread.start();
        thread.join();

        // Check that the event was added to the buffer after the delay
        assertEquals(event, buffer.getEvent());
    }
}






