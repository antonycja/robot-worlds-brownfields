package robot_worlds_13.server.robot;

import database.SqlCommands;
import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;

import java.util.*;

public class SaveCommand {
    private String worldName;
    private final List<String> obstacleTypes = List.of("obstacle", "lake", "pit");

    public SaveCommand(String worldName) {
        if (worldName.isEmpty()){
            worldName = promptForWorldName();
        }
        this.worldName = worldName;
    }

    public SaveCommand() {
        this.worldName = promptForWorldName();
    }

    private String promptForWorldName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a name for the world: ");
        return scanner.nextLine().trim();
    }

    public String saveWorld(AbstractWorld world) {
        int width= world.width;
        int height = world.height;
        Map<String, Object> data = new HashMap<>();

        try {
            // Instantiate SQL
            SqlCommands sqlCommands = new SqlCommands();
            // create the table for obstacle types
            sqlCommands.createTypesTable("types");
            // Insert data into types table
            for(String type: obstacleTypes ) {
                sqlCommands.insertType(type);
            }

            // Create worlds table
            sqlCommands.createWorldTable("world");
            // Insert data into worlds
            sqlCommands.insertWorld(worldName, width, height);

            // Create obstacles table
            sqlCommands.createObstacleTable("objects");
            // Insert data in objects table
            // Get obstacle type
            for (Obstacle obs: world.getObstacles()) {
                int size = obs.getSize();
                int x = obs.getBottomLeftX();
                int y = obs.getBottomLeftY();
                int id = obstacleTypes.indexOf("obstacle")+1;
                sqlCommands.insertObstacle( x, y, size, id);
            }
            // Get lake type
            for (Obstacle obs: world.getLakes()) {
                int size = obs.getSize();
                int x = obs.getBottomLeftX();
                int y = obs.getBottomLeftY();
                int id = obstacleTypes.indexOf("lake")+1;
                sqlCommands.insertObstacle( x, y, size, id);
            }
            // Get pit type
            for (Obstacle obs: world.getBottomLessPits()) {
                int size = obs.getSize();
                int x = obs.getBottomLeftX();
                int y = obs.getBottomLeftY();
                int id = obstacleTypes.indexOf("pit")+1;
                sqlCommands.insertObstacle( x, y, size, id);
            }
            System.out.println("World saved successfully with the name: " + this.worldName);
            sqlCommands.closeConnection();

            data.put("message", "World saved successfully");
            ServerProtocol.buildResponse("OK", data);
            System.out.println(ServerProtocol.buildResponse("OK", data));

            return "World saved successfully with the name: " + this.worldName;

        } catch (IllegalArgumentException e) {

            data.clear();
            data.put("message", "World name already exists");
            ServerProtocol.buildResponse("ERROR", data);
            System.out.println(ServerProtocol.buildResponse("ERROR", data));

            System.out.println("World with name '" + this.worldName + "' already exists, skipping this process.");
            return "World with name '" + this.worldName + "' already exists, skipping this process.";
        }
    }
}
