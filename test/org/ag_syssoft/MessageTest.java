package org.ag_syssoft;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    static Message message;
    @BeforeAll
    static void setUp() {
        message = new Message();
    }

    @Test
    void addAndQuery() {
        message.add("payload_key", "value");
        assertEquals("value", message.query("payload_key"));
        assertNull(message.query("non_existent_key"));
        assertNull(message.queryHeader("payload_key"));
        assertFalse(message.isControlMessage());
    }

    @Test
    void controlMessage() {
        Message controlMessage = new Message(true);
        assertTrue(controlMessage.isControlMessage());
    }

    @Test
    void fluentAPI() {
        message.add("payload_key", "value").addHeader("header_key", "value").add("payload_int", 1).addHeader("header_int", 2);
        assertEquals("value", message.query("payload_key"));
        assertEquals("value", message.queryHeader("header_key"));
        assertEquals("1", message.query("payload_int"));
        assertEquals("2", message.queryHeader("header_int"));
    }
    @Test
    void addHeaderAndQueryHeader() {
        message.addHeader("header_key", "value");
        assertEquals("value", message.queryHeader("header_key"));
        assertNull(message.queryHeader("non_existent_key"));
        assertNull(message.query("header_key"));
    }

    @Test
    void serializeAndDeserialize() throws Exception {
        message.add("payload_key", "value");
        message.addHeader("header_key", "value");
        String json = message.toJson();
        // System.out.println(json);
        assertEquals("{\"controlMessage\":false,\"header\":{\"header_key\":\"value\"},\"payload\":{\"payload_key\":\"value\"}}", json);
        Message deserialized = Message.fromJson(json);
        assertEquals(message.getPayload(), deserialized.getPayload());
        assertEquals(message.getHeader(), deserialized.getHeader());
    }
}