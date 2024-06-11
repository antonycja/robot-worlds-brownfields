package clienttests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

import robot_worlds_13.client.*;

public class ClientProtocolTest {
    @Test
    public void testJsonRequestBuilderSingleWordCommand() {
        String input = "forward";
        Map<String, Object> result = ClientProtocol.jsonRequestBuilder(input);
        assertArrayEquals(new Object[0], (Object[]) result.get("arguments"));
        assertEquals("forward", result.get("command"));
    }

    @Test
    public void testJsonRequestBuilderCommandWithArguments() {
        String input = "move 10 left";
        Map<String, Object> result = ClientProtocol.jsonRequestBuilder(input);
        assertArrayEquals(new Object[]{"10", "left"}, (Object[]) result.get("arguments"));
        assertEquals("move", result.get("command"));
    }

    @Test
    public void testJsonRequestBuilderWithRobotName() {
        String input = "turn right";
        String robotName = "Robo1";
        Map<String, Object> result = ClientProtocol.jsonRequestBuilder(input, robotName);
        assertArrayEquals(new Object[]{"right"}, (Object[]) result.get("arguments"));
        assertEquals("Robo1", result.get("robot"));
        assertEquals("turn", result.get("command"));
    }

    @Test
    public void testJsonResponseUnpackerOkResponse() {
        String jsonResponse = "{\"result\":\"OK\",\"data\":{\"message\":\"Operation successful\"}}";
        String response = ClientProtocol.jsonResponseUnpacker(jsonResponse);
        assertEquals("Operation successful", response);
    }

    @Test
    public void testJsonResponseUnpackerErrorResponse() {
        String jsonResponse = "{\"result\":\"ERROR\",\"data\":{\"message\":\"Command not found\"}}";
        String response = ClientProtocol.jsonResponseUnpacker(jsonResponse);
        assertEquals("Command not found", response);
    }

    @Test
    public void testJsonResponseUnpackerNoJsonFound() {
        String jsonResponse = "Invalid JSON format";
        String response = ClientProtocol.jsonResponseUnpacker(jsonResponse);
        assertEquals("Invalid JSON format", response);
    }
}
