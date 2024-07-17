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
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

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
        // Given that the robot is in the world
        // And is ready for the next command
        // When the player sends state command
        // Then the robot should return a state containing the status of normal
        // And the response message should be ok
        // And the full state information should be returned.
        assertTrue(serverClient.isConnected());

        // When I send a valid state request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"state\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And I should get the state of the robot
        assertNotNull(response.get("data").get("message"));
        assertEquals(response.get("data").get("message").asText(), "Robot does not exist");

//        assertEquals(0, response.get("data").get("position").get(0).asInt());
//        assertEquals(0, response.get("data").get("position").get(1).asInt());

    }
    @Test
    void invalidStateShouldFail(){
        // Given that I have a launched robot in the world
        assertTrue(serverClient.isConnected());

        // When I send an invalid state request with the command "sate" instead of "launch"
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
}