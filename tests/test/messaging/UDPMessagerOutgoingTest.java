package test.messaging;

import org.junit.Before;
import org.junit.Test;

import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerOutgoing;

import static org.junit.Assert.assertEquals;

public class UDPMessagerOutgoingTest {

    private EventBuffer<TestEnum> eventBuffer;
    private UDPMessagerOutgoing udpMessagerOutgoing;

    @Before
    public void setUp() {
        eventBuffer = new EventBuffer<>();
        udpMessagerOutgoing = new UDPMessagerOutgoing(eventBuffer);
    }

    @Test
    public void testGetPort() {
        assertEquals(9200, udpMessagerOutgoing.getPort(Subsystem.ELEVATOR));
        assertEquals(9000, udpMessagerOutgoing.getPort(Subsystem.FLOOR));
        assertEquals(9100, udpMessagerOutgoing.getPort(Subsystem.SCHEDULER));
        assertEquals(-1, udpMessagerOutgoing.getPort(null));
    }

    public enum TestEnum {
        CONSTANT1,
        CONSTANT2,
        CONSTANT3
    }
}
