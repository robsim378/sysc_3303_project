package test.Messaging;

import org.junit.Before;
import org.junit.Test;

import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.common.Subsystem;
import sysc_3303_project.common.SystemProperties;
import sysc_3303_project.messaging.UDPMessagerOutgoing;

import static org.junit.Assert.assertEquals;

public class UDPMessagerOutgoingTest {

    private EventBuffer<TestEnum> eventBuffer;
    private UDPMessagerOutgoing<TestEnum> udpMessagerOutgoing;

    @Before
    public void setUp() {
        eventBuffer = new EventBuffer<>();
        udpMessagerOutgoing = new UDPMessagerOutgoing<>(eventBuffer);
    }

    @Test
    public void testGetPort() {
        assertEquals(SystemProperties.ELEVATOR_PORT, udpMessagerOutgoing.getPort(Subsystem.ELEVATOR));
        assertEquals(SystemProperties.FLOOR_PORT, udpMessagerOutgoing.getPort(Subsystem.FLOOR));
        assertEquals(SystemProperties.SCHEDULER_PORT, udpMessagerOutgoing.getPort(Subsystem.SCHEDULER));
        assertEquals(-1, udpMessagerOutgoing.getPort(null));
    }

    public enum TestEnum {
        CONSTANT1,
        CONSTANT2,
        CONSTANT3
    }
}
