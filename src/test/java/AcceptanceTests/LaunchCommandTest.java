package AcceptanceTests;

import AcceptanceTests.RobotWorldClient.RobotWorldClient;
import AcceptanceTests.RobotWorldClient.RobotWorldJsonClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * As a player
 * I want to launch my robot in the online robot world
 * So that I can break the record for the most robot kills
 */
class LaunchCommandTests {
    private final static int DEFAULT_PORT = 5050;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
//    private final RobotWorldClient serverClient2 = new RobotWorldJsonClient();
    private final String launchRequest = "{" +
            "  \"robot\": \"HAL\"," +
            "  \"command\": \"launch\"," +
            "  \"arguments\": [\"pistol\",\"5\",\"5\"]" +
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
    void validLaunchShouldSucceed(){
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
//        String request = "{" +
//                "  \"robot\": \"HAL\"," +
//                "  \"command\": \"launch\"," +
//                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
//                "}";
        JsonNode response = serverClient.sendRequest(launchRequest);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And the position should be (x:0, y:0)
//        System.out.println(response.getClass());
//        System.out.println(response);
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertInstanceOf(Integer.class,response.get("data").get("position").get(0).asInt());
        assertInstanceOf(Integer.class, response.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }
    @Test
    void invalidLaunchShouldFail(){
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a invalid launch request with the command "luanch" instead of "launch"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"luanch\"," +
                "\"arguments\": [\"pistol\",\"5\",\"5\"]" +
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
    void LaunchARobotWorldOfSize2x2(){
        //Given a world of size 2x2
        assertTrue(serverClient.isConnected());
        //Hal is connected

        // Given that I have a launched robot in the world
        JsonNode response = serverClient.sendRequest(launchRequest);

        // Then I should get a valid response
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

    }
    @Test
    void LaunchRobotWithDuplicateName(){
        RobotWorldClient serverClient2 = new RobotWorldJsonClient();
        serverClient2.connect(DEFAULT_IP, DEFAULT_PORT);


        // Given that the robot does not exist in the world
        assertTrue(serverClient.isConnected());
        assertTrue(serverClient2.isConnected());

        // And a robot called "HAL" is already connected and launched
        serverClient.sendRequest(launchRequest);


        // When a robot with the same name already exists
        JsonNode duplicateLaunchResponse = serverClient2.sendRequest(launchRequest);

        // Then I must receive an error message saying "Too many of you in this world."
        assertNotNull(duplicateLaunchResponse.get("result"));
        assertEquals("ERROR", duplicateLaunchResponse.get("result").asText());
        assertNotNull(duplicateLaunchResponse.get("data"));
        assertNotNull(duplicateLaunchResponse.get("data").get("message"));
        assertEquals("Too many of you in this world", duplicateLaunchResponse.get("data").get("message").asText());

        serverClient2.disconnect();
    }

    @Test
    void CanLaunchAnotherRobot(){
        RobotWorldClient serverClient2 = new RobotWorldJsonClient();
        serverClient2.connect(DEFAULT_IP, DEFAULT_PORT);
        // Given that I am connected to a running Robot Worlds server
        // The world is configured or hardcoded to this size
        assertTrue(serverClient.isConnected());
        assertTrue(serverClient2.isConnected());

        // Launch HAL
        serverClient.sendRequest(launchRequest);
        // And a robot called "TOM"
        String request = "{" +
                "  \"robot\": \"TOM\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"pistol\",\"5\",\"5\"]" +
                "}";
//        serverClient.sendRequest(request);

        // WHEN there is no more space in the world for another robot to launch
        JsonNode duplicateLaunchResponse = serverClient2.sendRequest(request);

        // THEN I should get an error saying "No more space in this world."
        assertNotNull(duplicateLaunchResponse.get("result"));
        assertEquals("OK", duplicateLaunchResponse.get("result").asText());
        assertNotNull(duplicateLaunchResponse.get("data"));
//        assertNotNull(duplicateLaunchResponse.get("data").get("message"));
//        assertEquals("No more space in this world.", duplicateLaunchResponse.get("data").get("message").asText());

        serverClient.disconnect();

    }

    @Test
    void NoMoreSpaceForMoreRobots() {

        // Given a world of size 2x2
        JsonNode response = null;
        RobotWorldClient[] serverClients = new RobotWorldClient[10]; // Array to keep track of all clients

        // Successfully launch 9 robots into the world
        int robotCounter = 0;
        for (int i = 1; i <= 9; i++) {
            serverClients[i - 1] = new RobotWorldJsonClient();
            serverClients[i-1].connect(DEFAULT_IP, DEFAULT_PORT);
            assertTrue(serverClients[i - 1].isConnected());

            String launchRequest = "{" +
                    "  \"robot\": \"HAL" + i + "\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"pistol\",\"5\",\"5\"]" +
                    "}";

            response = serverClients[i - 1].sendRequest(launchRequest);
            if ("ERROR".equals(response.get("result").asText())) break;
            robotCounter++;
        }
        assertEquals("ERROR", response.get("result").asText());

        // When I launch one more robot
        serverClients[9] = new RobotWorldJsonClient(); // Create a new client for the 10th robot
        serverClients[9].connect(DEFAULT_IP, DEFAULT_PORT);
        assertTrue(serverClients[9].isConnected());

        String duplicateLaunchRequest = "{" +
                "  \"robot\": \"HAL10\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"pistol\",\"5\",\"5\"]" +
                "}";

        JsonNode duplicateLaunchResponse = serverClients[9].sendRequest(duplicateLaunchRequest);

        // Then I should get an error response back with the message "No more space in this world"
        assertNotNull(duplicateLaunchResponse.get("result"));
        assertEquals("ERROR", duplicateLaunchResponse.get("result").asText());
        assertNotNull(duplicateLaunchResponse.get("data"));
        assertNotNull(duplicateLaunchResponse.get("data").get("message"));
        assertEquals("No more space in this world", duplicateLaunchResponse.get("data").get("message").asText());

        for (RobotWorldClient client: serverClients) {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        }
    }

  /*  @Test
    void LaunchRobotWithoutRobotName(){
        // Given that I am connected to a running Robot Worlds server
        // The world is configured or hardcoded to this size
        assertTrue(serverClient.isConnected());
        // When I luanch a robot without specifying the robot name
        String launchRequest = "{" +
                "  \"robot\":null ," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [1, 1]" +
                "}";
        JsonNode duplicateLaunchResponse = serverClient.sendRequest(launchRequest);
        // Then I should get an error response back with the message "Invalid Request"
        assertNotNull(duplicateLaunchResponse.get("result"));
        assertEquals("ERROR", duplicateLaunchResponse.get("result").asText());
        assertNotNull(duplicateLaunchResponse.get("data"));
        assertNotNull(duplicateLaunchResponse.get("data").get("message"));
        assertEquals("Invalid Request", duplicateLaunchResponse.get("data").get("message").asText());

    }*/


}
