package test.common;


import org.junit.Before;
import org.junit.Test;

import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;

import static org.junit.Assert.*;

public class EventBufferTest {

    private EventBuffer<EventTypes> buffer;

    @Before
    public void setUp() {
        buffer = new EventBuffer<>();
    }

    @Test
    public void testGetEvent() {
        Event<EventTypes> event1 = new Event<>(Subsystem.FLOOR, 1, Subsystem.SCHEDULER, 1, EventTypes.FLOOR_REQUEST, "Payload 1");
        buffer.addEvent(event1);
        assertEquals(event1, buffer.getEvent());
    }

    @Test
    public void testGetEventWhenBufferIsEmpty() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500);
                Event<EventTypes> event2 = new Event<>(Subsystem.FLOOR, 2, Subsystem.SCHEDULER, 2, EventTypes.FLOOR_REQUEST, "Payload 2");
                buffer.addEvent(event2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        Event<EventTypes> retrievedEvent = buffer.getEvent();
        assertNotNull(retrievedEvent);
        assertEquals(Subsystem.FLOOR, retrievedEvent.getDestinationSubsystem());
        assertEquals(1, retrievedEvent.getDestinationID());
        assertEquals(Subsystem.SCHEDULER, retrievedEvent.getSourceSubsystem());
        assertEquals(1, retrievedEvent.getSourceID());
        assertEquals(EventTypes.FLOOR_REQUEST, retrievedEvent.getEventType());
        assertEquals("Payload 2", retrievedEvent.getPayload());
    }

    private enum EventTypes {
        FLOOR_REQUEST,
        ELEVATOR_REQUEST,
        ELEVATOR_STATUS_UPDATE
    }
}
