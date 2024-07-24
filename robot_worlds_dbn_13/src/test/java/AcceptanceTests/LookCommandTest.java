package AcceptanceTests;

import AcceptanceTests.RobotWorldClient.RobotWorldClient;
import AcceptanceTests.RobotWorldClient.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robot_worlds_13.server.robot.Position;

import static org.junit.jupiter.api.Assertions.*;

public class LookCommandTest {
    private final static int DEFAULT_PORT = 5001;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private boolean isObstacle = false;
    private boolean Robots = false;
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
                "  \"arguments\": []" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }

    @Test
    public void testSeeObstacle() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        // When I send a valid look request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a response with an object of type OBSTACLE at a distance of 1 step.
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        JsonNode objects = response.get("data").get("objects");
        assertNotNull(objects);

        for (JsonNode object : objects) {
            if ("OBSTACLE".equals(object.get("type").asText())) {
                isObstacle = true;
                position = object.get("distance").asInt();
                break;
            }
        }

        assertTrue(isObstacle);
        assertEquals(1, position);
    }

    @Test
    public void testLookWithNonExistentRobot() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "  \"robot\": \"Bob\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
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
    public void testSeeRobotsAndObstacles() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        serverClient.sendRequest(launchRequest);

        String launchAnotherRobotRequest = "{" +
                "  \"robot\": \"TOM\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": []" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest);

        // When I send a valid look request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode response = serverClient.sendRequest(request);
        // Then I should get a response with an object of type OBSTACLE at a distance of 1 step.
        // Then I should get a response with an object of type OBSTACLE at a distance of 1 step.
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        JsonNode objects = response.get("data").get("objects");
        assertNotNull(objects);
        for (JsonNode object : objects) {
            System.out.println(object);
            if (object.get("type").toString().contains("OBSTACLE")){
                isObstacle = true;
                position = object.get("distance").asInt();
                System.out.println(object);
                break;
            }
            if (object.get("type").toString().contains("ROBOT")) {
                Robots = true;
                position = object.get("distance").asInt();
                break;
            }

        }

        assertTrue(isObstacle);
        assertTrue(Robots);
        assertEquals(1, position);
        assertEquals(3, position);
    }
}

/*
        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // Check for obstacles and other robots
        JsonNode objects = response.get("data").get("objects");
        assertNotNull(objects);

        int obstacleCount = 1;
        int robots = 3;

        for (JsonNode item : objects) {
            String type = item.get("type").asText();
            if ("OBSTACLE".equals(type)) {
                obstacleCount++;
            } else if ("ROBOT".equals(type)) {
                robots++;
            }
        }

        assertEquals(1, obstacleCount);
        assertEquals(3, robots);
    }
}
*/
