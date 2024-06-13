package clienttests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.mockito.Mockito;
import robot_worlds_13.client.*; 

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientTest {

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
    }

    @Test
    public void testIsValidPortNumber() {
        assertTrue(Client.isValidPortNumber("8080"));
        assertFalse(Client.isValidPortNumber("70000"));
        assertFalse(Client.isValidPortNumber("abc"));
    }

    @Test
    public void testIsValidIPAddress() {
        assertTrue(Client.isValidIPAddress("192.168.1.1"));
        assertTrue(Client.isValidIPAddress("255.255.255.255"));
        assertFalse(Client.isValidIPAddress("256.256.256.256"));
        assertFalse(Client.isValidIPAddress("abc.def.ghi.jkl"));
    }

    @Test
    public void testIsValidLaunch() {
        assertTrue(Client.isValidLaunch("launch ranger robot1"));
        assertFalse(Client.isValidLaunch("launch invalid_make robot1"));
        assertFalse(Client.isValidLaunch("start ranger robot1"));
        assertFalse(Client.isValidLaunch("launch ranger"));
    }

    @Test
    public void testGetAttributes() {
        ArrayList<Object> attributes = Client.getAttributes("sagebot");
        assertEquals(2, attributes.size());
        assertEquals(50, attributes.get(0));
        assertEquals(50, attributes.get(1));

        attributes = Client.getAttributes("invalid_make");
        assertEquals(0, attributes.size());
    }

    @Test
    public void testSendJsonRequest() throws IOException {
        // Mocking Socket and DataOutputStream
        Socket mockSocket = Mockito.mock(Socket.class);
        DataOutputStream mockDout = Mockito.mock(DataOutputStream.class);

        Client.sThisClient = mockSocket;
        Client.dout = mockDout;
        Client.robotName = "robot1";

        Map<String, Object> commandDetails = new HashMap<>();
        commandDetails.put("command", "testCommand");

        Client.sendJsonRequest(commandDetails);

        // Verify if the DataOutputStream writeUTF method was called with the correct JSON string
        String expectedJson = "{\"command\":\"testCommand\",\"robot\":\"robot1\"}";
        Mockito.verify(mockDout).writeUTF(expectedJson);
        Mockito.verify(mockDout).flush();
    }
}


