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
    private final static int DEFAULT_PORT = 5001;
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

}
