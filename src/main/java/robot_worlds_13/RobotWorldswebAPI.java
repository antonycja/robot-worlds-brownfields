/*
package robot_worlds_13;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import robot_worlds_13.client.Client;
import robot_worlds_13.server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RobotWorldsWebAPI implements Runnable {

    private final Javalin server;
    RequestMessage message = new RequestMessage();
    private String clientName;
    private Client client;
    private boolean isRunning = false;

    public RobotWorldsWebAPI() throws IOException {
        this.server = Javalin.create();

        // Define routes for world data and robot actions
        this.server.get("world", context -> {
            context.contentType("application/json");
            context.result(worldRequest());
        });

        this.server.post("robot/{name}", context -> {
            context.contentType("application/json");
            String name = context.pathParam("name");
            String body = context.body();
            if (body.contains("launch")) {
                context.result(launchRobot(name, body));
            } else if (body.equals("look")) {
                context.result(lookCommand(name));
            } else if (body.contains("forward") || body.contains("back")) {
                context.result(forwardBack(name, body));
            } else if (body.contains("turn")) {
                context.result(turnCommand(name, body));
            }
        });
    }

    public static void main(String[] args) throws IOException {
        RobotWorldServerWebAPI api = new RobotWorldServerWebAPI();
        Thread task = new Thread(api);
        task.start();
        api.start();
    }

    public Javalin start() throws IOException {
        // Initialize the client connection to the server
        client = new Client("localhost", 4000);
        return this.server.start(3000);
    }

    public Javalin stop() {
        return this.server.stop();
    }

    private String worldRequest() throws JsonProcessingException {
        // Fetch the world data from Server class
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<String> worldRobots = new ArrayList<>();
        ArrayList<String> obstacles = new ArrayList<>();

        // Use the Server class to fetch the robots and obstacles
        Server server = new Server();
        server.robotNames().forEach(worldRobots::add);
        server.getObstacles().forEach(obstacle -> obstacles.add(obstacle.toString()));

        map.put("worldsize", Math.abs(server.getWorldSize()));
        map.put("robots", worldRobots);
        map.put("obstacles", obstacles);
        return mapper.writeValueAsString(map);
    }

    private String launchRobot(String robotName, String command) {
        // Launch the robot using Client class
        clientName = robotName;
        if (command.split(" ").length == 1) {
            command = "launch tank";
        }

        String request = message.constructMessage(clientName, command + " " + clientName);
        return client.sendRequest(request);
    }

    private String forwardBack(String robotName, String movement) {
        // Move the robot forward or back
        clientName = robotName;
        String request = message.constructMessage(clientName, movement);
        return client.sendRequest(request);
    }

    private String lookCommand(String robotName) {
        // Robot looks around the world
        clientName = robotName;
        String request = message.constructMessage(clientName, "look");
        return client.sendRequest(request);
    }

    private String turnCommand(String robotName, String direction) {
        // Turn the robot in a direction
        clientName = robotName;
        String request = message.constructMessage(clientName, direction);
        return client.sendRequest(request);
    }

    @Override
    public void run() {
        // Start the server using multi-threading
        if (!isRunning) {
            isRunning = true;
            Server server = new Server();
            try {
                server.startServer();  // Assuming this method starts the server
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
*/
