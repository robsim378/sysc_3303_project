package test;

import org.junit.Before;
import org.junit.Test;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;

import static org.junit.Assert.*;

public class EventBufferTest {

    private EventBuffer<String> buffer;

    @Before
    public void setUp() {
        buffer = new EventBuffer<>();
    }

    @Test
    public void testGetEvent() {
        Event<String> event1 = new Event<>("Event 1", this, "Payload 1");
        buffer.addEvent(event1);
        assertEquals(event1, buffer.getEvent());
    }

    @Test
    public void testGetEventWhenBufferIsEmpty() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500);
                buffer.addEvent(new Event<>("Event 1", this, "Payload 1"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        Event<String> retrievedEvent = buffer.getEvent();
        assertNotNull(retrievedEvent);
        assertEquals("Event 1", retrievedEvent.getEventType());
        assertEquals(this, retrievedEvent.getSender());
        assertEquals("Payload 1", retrievedEvent.getPayload());
    }

}
