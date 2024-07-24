package AcceptanceTests;

import AcceptanceTests.RobotWorldClient.RobotWorldClient;
import AcceptanceTests.RobotWorldClient.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class LookCommandTest {
    private final static int DEFAULT_PORT = 5001;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private boolean isObstacle = false;
    private int position = 0;
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
    public void testExecute_Success() {

        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a valid look request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from a server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }
    @Test
    public void See_obstacle(){
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a valid look request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);
        // When I ask the robot to look
        // Then I should get an response back with an object of type OBSTACLE at a distance of 1 step.

        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        assertNotNull(response.get("data").get("objects").toString());
        JsonNode objects = response.get("data").get("objects");

        for (JsonNode object : objects) {
            if (object.get("type").toString().contains("OBSTACLE")) {
                isObstacle = true;
                position = Integer.parseInt(object.get("distance").toString());
                break;
            }
        }
        assertTrue(isObstacle);
        assertEquals(1, position);


    }

    @Test
    public void See_robots_and_obstacles() {
        //Given I launch a robot and in a  world size 2x2
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a valid look request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from a server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));

        // Check for obstacles and other robots
        JsonNode observation = response.get("state").get("type"); // Adjust path according to your actual JSON
      /*  and the world has an obstacle at coordinate [0,1]
        and I have successfully launched 8 robots into the world*/
        int obstacleCount = 0;
        int robotCount = 8;

        // Iterate through the observation to count the items
        for (JsonNode item : observation) {
            String type = item.get("type").asText();
            if ("OBSTACLE".equals(type)) {
                obstacleCount++;
            } else if ("ROBOT".equals(type)) {
                robotCount++;
            }

        }
       /* Then I should get an response back with
        one object being an OBSTACLE that is one step away
        and three objects should be ROBOTs that is one step away*/
        assertEquals(1, obstacleCount);
        assertEquals(3, robotCount, response.get("result").asText());
    }
}