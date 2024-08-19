package AcceptanceTests;
import AcceptanceTests.RobotWorldClient.RobotWorldClient;
import AcceptanceTests.RobotWorldClient.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * As a player
 * I want to be able to run the state command
 * So that I can see the current state (updated information) of my robot without having to move.
 */
public class StateCommandTest {
    private final static int DEFAULT_PORT = 5050;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private final String launchRequest = "{" +
            "  \"robot\": \"HAL\"," +
            "  \"command\": \"launch\"," +
            "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
            "}";

    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    @Test
    void robotExitsInWorldShouldFail(){
        assertTrue(serverClient.isConnected());
        // Given that the robot does not exist in the world
        final RobotWorldClient serverClient2 = new RobotWorldJsonClient();
        serverClient2.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClient2.sendRequest(launchRequest);
        // When I send a valid state request to the server
        String request = "{" +
                "  \"robot\": \"TOM\"," +
                "  \"command\": \"state\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response from the server
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And I should get a message stating that the robot doesn't exist
        assertNotNull(response.get("data").get("message"));
//        assertEquals(response.get("data").get("message").asText(), "Robot does not exist");

        serverClient2.disconnect();
    }

    @Test
    void invalidStateShouldFail(){
        assertTrue(serverClient.isConnected());
        // Given that I have a launched robot in the world
        serverClient.sendRequest(launchRequest);
        // When I send an invalid state request with the command "sate" instead of "state"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"sate\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And the message "Unsupported command"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Unsupported command"));
    }

    @Test
    void validStateShouldPass() {
        assertTrue(serverClient.isConnected());
        // Given that I have a launched robot in the world
        serverClient.sendRequest(launchRequest);

        // When I send a valid state request
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"state\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And the state should be valid
        assertNotNull(response.get("state"));
    }
}

