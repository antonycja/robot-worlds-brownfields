package robot_worlds_13.server.robot;

import database.SqlCommands;
import database.orm.ObjectsDO;
import database.orm.WorldDO;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;
import robot_worlds_13.server.robot.world.SquareObstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static database.orm.ConnectDB.worldDAO;

public class RestoreCommand {
    private final String worldName;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Obstacle> lakes = new ArrayList<>();
    private ArrayList<Obstacle> pits = new ArrayList<>();

    private String name;
    private Integer worldWidth;
    private Integer worldHeight;


    public RestoreCommand(String worldName) {
        this.worldName = (worldName.isEmpty()) ? promptForWorldName() : worldName;
    }

    public RestoreCommand() {
        this.worldName = promptForWorldName();
    }

    private String promptForWorldName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the world to restore: ");
        return scanner.nextLine().trim();
    }

    private List<ObjectsDO> getWorldData(String worldName){
        WorldDO worldData = worldDAO.getWorldData(worldName);
        this.name = worldData.getName();
        this.worldWidth = worldData.getWidth();
        this.worldHeight = worldData.getHeight();
        return worldDAO.getObjectData(worldName);
    }

    private void verifyWorldExists(){
        if (worldDAO.countWorldName(this.worldName) == 1) { return;}
        System.out.println("The world '" + this.worldName + "' does not exist!!");
        System.out.println("Aborting Restore...");
        throw new IllegalStateException("Cannot restore a world that doesn't exist.");
    }

    private void restoreWorldData(AbstractWorld world){
        world.setName(worldName);
        world.width = worldWidth;
        world.height = worldHeight;
    }

    private void restoreObjects(ObjectsDO object){
        Obstacle obstacle = new SquareObstacle(object.getXPosition(), object.getYPosition(), object.getSize(), object.getType());
        switch (object.getType()) {
            case "obstacle":
                obstacles.add(obstacle);
                break;
            case "lake":
                lakes.add(obstacle);
                break;
            case "pit":
                pits.add(obstacle);
                break;
            default:
                System.out.println("Unknown obstacle type: " + object.getType());
                break;
        }
    }

    public void restoreWorld(AbstractWorld world) {
        try {
            verifyWorldExists();
            final List<ObjectsDO> objectsList= getWorldData(worldName);
            restoreWorldData(world);
            objectsList.forEach(object -> restoreObjects(object));
            world.setObstacles(obstacles);
            world.setBottomLessPits(pits);
            world.setLakes(lakes);

            System.out.println("World restored successfully with the name: " + world.getName());

        } catch (IllegalStateException e) {System.out.println("Restore process terminated: " + e.getMessage());}
    }
}
