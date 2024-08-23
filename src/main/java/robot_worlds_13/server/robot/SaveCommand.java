package robot_worlds_13.server.robot;

import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;
import static database.orm.ConnectDB.worldDAO;
import java.util.*;

public class SaveCommand {
    private final String worldName;
    private final List<String> obstacleTypes = List.of("obstacle", "lake", "pit");

    public SaveCommand(String worldName) {
        this.worldName = (worldName.isEmpty()) ? promptForWorldName() : worldName;
        addObstacleTypes();
    }

    public SaveCommand() {
        this.worldName = promptForWorldName();
        addObstacleTypes();
    }

    private String promptForWorldName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a name for the world: ");
        return scanner.nextLine().trim();
    }

    public void saveWorld(AbstractWorld world) {
        int width = world.width;
        int height = world.height;
        Map<String, Object> data = new HashMap<>();

        List<Obstacle> obstacleList = world.getAllObstacles();

        if (worldDAO.countWorldName(worldName) == 0 ){
            addObstacles(obstacleList);
            worldDAO.addWorld(worldName, width,height);
            System.out.println("World saved successfully with the name: " + this.worldName);
            successResponse(data);
        }
        else {
            System.out.println("'" + worldName+"' already exists, try another name or 'worlds' to see all saved worlds. Failed to Save.");
            errorResponse(data);
        }
    }

    private void addObstacles(List<Obstacle> obstacleList){
        for (Obstacle obstacle:obstacleList){
            String typeName = obstacle.getType();
            if(worldDAO.countTypeByName(typeName) > 0){
                worldDAO.addObstacle(worldName, obstacle.getBottomLeftX(),obstacle.getBottomLeftY(),obstacle.getSize(),obstacle.getType());
            } else {System.out.println("'" + typeName + "' does not exist in types table. Failed to Save.");}
        }
    }

    private void addObstacleTypes() {
        for (String type : obstacleTypes) {worldDAO.addType(type);}
    }

    private void errorResponse(Map<String, Object> data){
        data.clear();
        data.put("message", "World name already exists");
        ServerProtocol.buildResponse("ERROR", data);
        System.out.println(ServerProtocol.buildResponse("ERROR", data));
    }
    private void successResponse(Map<String, Object> data){
        data.put("message", "World saved successfully");
        ServerProtocol.buildResponse("OK", data);
        System.out.println(ServerProtocol.buildResponse("OK", data));
    }
}
