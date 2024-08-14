package database;

import robot_worlds_13.server.robot.world.Obstacle;
import robot_worlds_13.server.robot.world.SquareObstacle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlDB {
    public static void main(String[] args) {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        ArrayList<Obstacle> pits = new ArrayList<>();
        ArrayList<Obstacle> lakes = new ArrayList<>();
        SqlCommands sqlCommands = new SqlCommands();

        // First create types table
        sqlCommands.createTypesTable("types");
        sqlCommands.insertType("pit");
        sqlCommands.insertType("lake");
        sqlCommands.insertType("mountain");

        // create world table
        sqlCommands.createWorldTable("world");

        // Create obstacle table
        sqlCommands.createObstacleTable("obstacles");

        // Insert a world
        sqlCommands.insertWorld("tom", 50, 5);
        sqlCommands.insertObstacle( 5, 5, 4, 1);
        sqlCommands.insertObstacle( 5, 5, 4, 2);
        sqlCommands.insertObstacle( 5, 5, 4, 1);


        // Another world
        sqlCommands.insertWorld("lutho", 100, 100);
        sqlCommands.insertObstacle( 50, 20, 5, 3);
        sqlCommands.insertObstacle( 5, 35,5,  3);
        sqlCommands.insertObstacle( 2, 5,5,  1);

        Map<String, Object> worldData = sqlCommands.restoreWorldData("world", "obstacles", "types", "lutho");

        System.out.println("Name: " + worldData.get("name"));
        System.out.println("Width: " + worldData.get("width"));
        System.out.println("Height: "+ worldData.get("height"));

        ArrayList objects = (ArrayList) worldData.get("obstacles");
        for(Object object: objects){
            Map obs = (Map) object;
            Obstacle obstacle = new SquareObstacle((Integer) obs.get("x_position"), (Integer) obs.get("y_position"));
            switch ((String) obs.get("type")) {
                case "mountain":
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

        System.out.println(obstacles);
        System.out.println(pits);
        System.out.println(lakes);

    }
}
