

package AcceptanceTests;

import AcceptanceTests.RobotWorldClient.RobotWorldClient;
import AcceptanceTests.RobotWorldClient.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaveTheWorldTest {
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
    public void testSaveCommand_Success() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a valid save request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And the world should be saved successfully
        assertNotNull(response.get("data"));
        assertEquals("World 'world1' saved successfully.", response.get("data").get("message").asText());
    }

    @Test
    public void testSaveCommandWithNonExistentRobot() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // When I send a save request for a non-existent robot
        String request = "{" +
                "  \"robot\": \"NonExistentRobot\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an "ERROR" response with the message "Robot does not exist"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertEquals("Robot does not exist", response.get("data").get("message").asText());
    }

    @Test
    public void testSaveCommandWithInvalidArguments() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a save request with invalid arguments (e.g., missing world name)
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an "ERROR" response with the message "Invalid arguments"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertEquals("Invalid arguments", response.get("data").get("message").asText());
    }
    @Test
    public void testSaveTheWorldWithDifferentCollectionOfRobots(){

        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        String request1 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";

        String request2 = "{" +
                "  \"robot\": \"BOB\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";

        JsonNode response1 = serverClient.sendRequest(request1);
        JsonNode response2 = serverClient.sendRequest(request2);


        // Then I should get a valid response from the server
        assertNotNull(response1.get("result"));
        assertEquals("OK", response1.get("result").asText());

        // Then I should get a valid response from the server
        assertNotNull(response2.get("result"));
        assertEquals("OK", response2.get("result").asText());

        // And the world should be saved successfully
        assertNotNull(response1.get("data"));
        assertEquals("World 'world1' saved successfully.", response1.get("data").get("message").asText());

        // And the world should be saved successfully
        assertNotNull(response2.get("data"));
        assertEquals("World 'world1' saved successfully.", response2.get("data").get("message").asText());



    }
    @Test
    public void testSaveTheWorldOnMoreThanOneOccasion(){

        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        String request1 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";
        JsonNode response1 = serverClient.sendRequest(request1);

        String request2 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"world1\"]" +
                "}";
        JsonNode response2 = serverClient.sendRequest(request2);

        // Then I should get a valid response from the server
        assertNotNull(response1.get("result"));
        assertEquals("OK", response1.get("result").asText());

        // Then I should get a valid response from the server
        assertNotNull(response2.get("result"));
        assertEquals("OK", response2.get("result").asText());

        // And the world should be saved successfully
        assertNotNull(response1.get("data"));
        assertEquals("World 'world1' saved successfully.", response1.get("data").get("message").asText());

        // And the world should be saved successfully
        assertNotNull(response2.get("data"));
        assertEquals("World 'world1' saved successfully.", response2.get("data").get("message").asText());

    }
    @Test
    public void testSaveTheWorldWithOnlyTheSizePositionAndObstacleTypes(){

        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);


    }

}
