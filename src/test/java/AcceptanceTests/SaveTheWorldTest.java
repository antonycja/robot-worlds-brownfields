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
        assertTrue(serverClient.isConnected(), "Server client should be connected");

        // And I have launched a robot into the world
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);
        assertNotNull(launchResponse, "Launch response should not be null");
        assertEquals("OK", launchResponse.get("result").asText(), "Launch command should be successful");

        // Check additional launch response details if available
        assertNotNull(launchResponse.get("data"), "Launch response should have a data field");
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
        assertEquals("Could not parse arguments", response.get("data").get("message").asText());
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
        assertEquals("Unsupported command", response.get("data").get("message").asText());
    }
    @Test
    public void testSaveTheWorldWithDifferentCollectionOfRobots(){

        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());
     // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // And I have launched a robot into the world
        assertNotNull(launchRequest, "Launch request is null");
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);
        assertNotNull(launchResponse, "Launch response is null");
        assertTrue(launchResponse.has("result") && "OK".equals(launchResponse.get("result").asText()),
                "Launch response was not successful");

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

        // Validate response from first robot
        assertNotNull(response1, "Response1 is null");
        assertNotNull(response1.get("result"), "Result field in Response1 is null");
        assertEquals("OK", response1.get("result").asText(), "Unexpected result in Response1");
        assertNotNull(response1.get("data"), "Data field in Response1 is null");
        assertEquals("World 'world1' saved successfully.", response1.get("data").get("message").asText(), "Unexpected message in Response1");

        // Validate response from second robot
        assertNotNull(response2, "Response2 is null");
        assertNotNull(response2.get("result"), "Result field in Response2 is null");
        assertEquals("OK", response2.get("result").asText(), "Unexpected result in Response2");
        assertNotNull(response2.get("data"), "Data field in Response2 is null");
        assertEquals("World 'world1' saved successfully.", response2.get("data").get("message").asText(), "Unexpected message in Response2");
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
        // And the world should be saved successfully
        assertNotNull(response1.get("data"));
        assertEquals("World 'world1' saved successfully.", response1.get("data").get("message").asText());


        // Then I should get a valid response from the server
        assertNotNull(response2.get("result"));
        assertEquals("OK", response2.get("result").asText());
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

                String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"save\"," +
                "  \"arguments\": [\"world1\"]," +
                "  \"obstacles\": [" +
                "    {" +
                "      \"type\": \"bottomless pits\"," +
                "      \"position\": {\"x\": 5, \"y\": 10}," +
                "      \"size\": {\"width\": 2, \"height\": 2}" +
                "    }," +
                "    {" +
                "      \"type\": \"mines\"," +
                "      \"position\": {\"x\": 15, \"y\": 20}," +
                "      \"size\": {\"width\": 2, \"height\": 2}" +
                "    }" +
                "  ]" +
                "}";

        // Send the request to the server
        JsonNode response = serverClient.sendRequest(request);



        // Check if the result is OK
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And the world should be saved successfully
        assertNotNull(response.get("data"));
        assertEquals("World 'world1' saved successfully.", response.get("data").get("message").asText());




    }

}


