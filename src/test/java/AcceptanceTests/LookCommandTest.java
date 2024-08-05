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
    private final static int DEFAULT_PORT = 5050;
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
    public void request_look_command_with_a_spelling_error(){

        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        // And I have launched a robot into the world
        assertTrue(serverClient.isConnected());
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"luk\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);
        // Then I should get an "ERROR" response with the message "Robot does not exist"

        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertEquals("Unsupported command", response.get("data").get("message").asText());



}

    @Test
    public void testSeeRobotsAndObstacles() {
        // Given that you're connected to a robot world server
        assertTrue(serverClient.isConnected());

        /*Given a world of size 2x2
        and the world has an obstacle at coordinate [0,1]
        and I have successfully launched 8 robots into the world*/
        serverClient.sendRequest(launchRequest);

        String launchAnotherRobotRequest = "{" +
                "  \"robot\": \"HAL2\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest);

        String launchAnotherRobotRequest2 = "{" +
                "  \"robot\": \"HAL3\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest2);

        String launchAnotherRobotRequest3 = "{" +
                "  \"robot\": \"HAL4\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest3);

        String launchAnotherRobotRequest4 = "{" +
                "  \"robot\": \"HAL2\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest4);


        String launchAnotherRobotRequest5 = "{" +
                "  \"robot\": \"HAL2\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest5);

        String launchAnotherRobotRequest6 = "{" +
                "  \"robot\": \"HAL3\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest6);

        String launchAnotherRobotRequest7 = "{" +
                "  \"robot\": \"HAL4\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest7);

        String launchAnotherRobotRequest8 = "{" +
                "  \"robot\": \"HAL2\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launchAnotherRobotRequest8);


        //When I ask the first robot to look
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
            }
            if ("ROBOT".equals(object.get("type").asText())) {
                Robots = true;
                position = object.get("distance").asInt();
            }
        }

        // one object being an OBSTACLE that is one step away
        //and three objects should be Robots that is one step away
        assertTrue(Robots);
        assertTrue(isObstacle);
        assertEquals(1, position);
        }
    }


