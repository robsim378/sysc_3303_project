package test.messaging;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerIncoming;

public class UDPMessagerIncomingTest {

    private List<EventBuffer<TestEnum>> eventBuffers;
    private int listeningPort = 5000;
    private UDPMessagerIncoming<TestEnum> udpMessagerIncoming;

    // Define TestEnum at the top of the file
    private enum TestEnum {
        TEST_EVENT
    }

    @Before
    public void setUp() throws SocketException {
        eventBuffers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            eventBuffers.add(new EventBuffer<TestEnum>());

        }
        udpMessagerIncoming = new UDPMessagerIncoming<>(eventBuffers, Subsystem.FLOOR);
    }

    @Test
    public void testAddEventToCorrectEventBuffer() throws IOException {
        // create and send an event to a specific destination buffer
        Subsystem destSubsystem = Subsystem.FLOOR;
        int destID = 3;
        Subsystem srcSubsystem = Subsystem.SCHEDULER;
        int srcID = 2;
        TestEnum eventType = TestEnum.TEST_EVENT;
        String payload = "test payload";
        Event<TestEnum> event = new Event<>(destSubsystem, destID, srcSubsystem, srcID, eventType, payload);
        byte[] eventBytes = eventToBytes(event);
        DatagramPacket packet = new DatagramPacket(eventBytes, eventBytes.length, InetAddress.getLocalHost(), listeningPort);

        UDPMessagerIncoming thing = new UDPMessagerIncoming(eventBuffers, Subsystem.FLOOR);
        // wait for event to be added to the correct buffer
        Event<TestEnum> actualEvent = thing.parseEvent(packet);

        // assert that the event was added to the correct buffer
        assertEquals(destSubsystem, actualEvent.getDestinationSubsystem());
        assertEquals(destID, actualEvent.getDestinationID());
        assertEquals(srcSubsystem, actualEvent.getSourceSubsystem());
        assertEquals(srcID, actualEvent.getSourceID());
        assertEquals(eventType, actualEvent.getEventType());
        assertEquals(payload, actualEvent.getPayload());
    }

    private byte[] eventToBytes(Event<TestEnum> event) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(event);
        out.flush();
        return bos.toByteArray();
    }
}
