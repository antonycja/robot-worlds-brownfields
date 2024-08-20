package AcceptanceTests;
import AcceptanceTests.RobotWorldClient.RobotWorldClient;
import AcceptanceTests.RobotWorldClient.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

//As a player
//I want to command my robot to move forward a specified number of steps
//so that I can explore the world and not be a sitting duck in a battle.
public class MoveForwardTest {
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
    void MovingNoneExistentRobotShouldFail() {
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // And a robot called "HAL" is connected and launched
        serverClient.sendRequest(launchRequest);

        // When I send a command for "TOM" to move forward by 5 steps
        String request = "{" +
                "  \"robot\": \"TOM\"," +
                "  \"command\": \"forward\"," +
                "  \"arguments\": [\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an "ERROR" response with the message "Robot does not exist"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertEquals("Robot does not exist", response.get("data").get("message").asText());
    }

    @Test
    void MovingToEdgeIn1x1WorldShouldPass(){
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // And the world is of size 1x1 with no obstacles or pits
        // TODO

        // And a robot called "HAL" is already connected and launched
        serverClient.sendRequest(launchRequest);

        // When I send a command for "HAL" to move forward by 5 steps
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"forward\"," +
                "  \"arguments\": [\"1000\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an "OK" response with the message "At the NORTH edge"
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
//        assertEquals("At the NORTH edge", response.get("data").get("message").asText());

        // and the position information returned should be at co-ordinates [0,0]
        assertNotNull(response.get("data").get("position"));
//        assertEquals("[0,0]", response.get("data").get("position").toString());
    }

    @Test
    void ForwardWithInvalidStepsShouldFail() {
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // And a robot called "HAL" is already connected and launched
        serverClient.sendRequest(launchRequest);

        // When I send a command for "HAL" to move forward without specifying steps
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"forward\"," +
                "  \"arguments\": [\"k\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);
        System.out.println(response);
        // Then I should get an "ERROR" response with the message "Invalid argument"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertEquals("Unsupported command", response.get("data").get("message").asText());

    }
}
