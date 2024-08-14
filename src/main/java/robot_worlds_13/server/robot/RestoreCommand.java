package robot_worlds_13.server.robot;

import database.SqlCommands;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;
import robot_worlds_13.server.robot.world.SquareObstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RestoreCommand {
    private final String worldName;
    private final SqlCommands sqlCommands = new SqlCommands();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Obstacle> lakes = new ArrayList<>();
    private ArrayList<Obstacle> pits = new ArrayList<>();

    public RestoreCommand(String worldName) {
        this.worldName = worldName;
    }
    public RestoreCommand() {
        this.worldName = promptForWorldName();
    }

    private String promptForWorldName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the world to restore: ");
        return scanner.nextLine().trim();
    }

    public void restoreWorld(AbstractWorld world) {

        try {
            // Instantiate SQL
//            sqlCommands = new SqlCommands();
            String worldTableName = "world";
            String obstacleTableName = "objects";
            String typeTableName = "types";

            Map<String, Object> worldData = sqlCommands.restoreWorldData(worldTableName, obstacleTableName, typeTableName, this.worldName);

            String worldName = (String) worldData.get("name");
            Integer worldWidth = (Integer) worldData.get("width");
            Integer worldHeight = (Integer) worldData.get("height");

            ArrayList objects = (ArrayList) worldData.get("obstacles");
            if (!this.worldName.equals(worldName)) {
                System.out.println("The world '" + this.worldName + "' does not exist!!");
                System.out.println("Aborting Restore...");
                System.out.println("Successfully Reverted back.");
            } else {
                for (Object object : objects) {
                    Map obs = (Map) object;
                    Obstacle obstacle = new SquareObstacle((Integer) obs.get("x_position"), (Integer) obs.get("y_position"));
                    switch ((String) obs.get("type")) {
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
                            System.out.println("Something went wrong with finding your obstacle.");
                            break;
                    }
                }

                world.setObstacles(obstacles);
                world.setBottomLessPits(pits);
                world.setLakes(lakes);

//                System.out.println(obstacles);
//                System.out.println(pits);
//                System.out.println(lakes);

                System.out.println("World restored successfully with the name: " + worldName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
