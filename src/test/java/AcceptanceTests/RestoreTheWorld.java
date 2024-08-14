package AcceptanceTests;


import AcceptanceTests.RobotWorldClient.RobotWorldClient;
import AcceptanceTests.RobotWorldClient.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestoreTheWorld {
    private final static int DEFAULT_PORT = 5050;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private final String launchRequest = "{" +
            "  \"robot\": \"HAL\"," +
            "  \"command\": \"launch\"," +
            "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
            "}";

    @BeforeEach
    void connectToServer() {
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer() {
        serverClient.disconnect();
    }

    @Test
    public void testRestoreCommand_Success() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // And I have saved the world state
        String saveRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";
        JsonNode saveResponse = serverClient.sendRequest(saveRequest);
        assertEquals("OK", saveResponse.get("result").asText());

        // When I send a valid restore request to the server
        String restoreRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"restore\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";
        JsonNode restoreResponse = serverClient.sendRequest(restoreRequest);

        // Then I should get a valid response from the server
        assertNotNull(restoreResponse.get("result"));
        assertEquals("OK", restoreResponse.get("result").asText());

        // And the world should be restored successfully
        assertNotNull(restoreResponse.get("data"));
        assertEquals("World 'world1' restored successfully.", restoreResponse.get("data").get("message").asText());
    }

    @Test
    public void testRestoreCommandWithNonExistentWorld() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a restore request for a non-existent world
        String restoreRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"restore\"," +
                "  \"arguments\": [\"nonexistent_world\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(restoreRequest);

        // Then I should get an "ERROR" response with the message "World does not exist"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertEquals("World does not exist", response.get("data").get("message").asText());
    }

    @Test
    public void testRestoreCommandWithInvalidArguments() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a restore request with invalid arguments (e.g., missing world name)
        String restoreRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"restore\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode response = serverClient.sendRequest(restoreRequest);

        // Then I should get an "ERROR" response with the message "Invalid arguments"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertEquals("Invalid arguments", response.get("data").get("message").asText());
    }

    @Test
    public void testRestoreCommandWithNonExistentRobot() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // When I send a restore request for a non-existent robot
        String restoreRequest = "{" +
                "  \"robot\": \"NonExistentRobot\"," +
                "  \"command\": \"restore\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(restoreRequest);

        // Then I should get an "ERROR" response with the message "Robot does not exist"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertEquals("Robot does not exist", response.get("data").get("message").asText());
    }
    @Test
    public void testRestoreCommandWithSavedWorld(){

        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a valid save request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"restore\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And the world should be saved successfully
        assertNotNull(response.get("data"));
        assertEquals("World 'world1' restored successfully.", response.get("data").get("message").asText());
    }

}

