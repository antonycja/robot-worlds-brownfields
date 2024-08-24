package robot_worlds_13.server.robot;

import database.orm.ObjectsDO;
import database.orm.WorldDO;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;

import java.util.List;

/**
 * Represents a location where items or data can be deposited.
 * This class serves as a placeholder for future implementation of dump-related functionalities.
 */
public class Dump {
    public Dump(AbstractWorld world) {
        List<Obstacle> obstacles = world.getAllObstacles();
        System.out.println("Here is a list of all objects in '"+world.getName()+"' world:");
        System.out.printf("%-20s %-10s %-10s%n", "Type", "Size", "X,Y Position");
        System.out.println("------------------------------------------------------");
        obstacles.forEach(object -> displayObjects(object));
        System.out.println("------------------------------------------------------");
    }
    private void displayObjects(Obstacle obstacle) {
        String type = obstacle.getType();
        int size = obstacle.getSize();
        String obs_start = obstacle.getBottomLeftX()+","+obstacle.getBottomLeftY();
        String obs_end = " ("+obstacle.getBottomLeftX()+obstacle.getSize()+","+obstacle.getBottomLeftY()+obstacle.getSize() + ")";
        System.out.printf("%-20s %-10d %-10s%n", type, size, obs_start+obs_end);
    }
}
