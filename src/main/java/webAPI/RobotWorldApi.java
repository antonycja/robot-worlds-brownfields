package webAPI;

import io.javalin.http.Context;
import robot_worlds_13.server.robot.RestoreCommand;
import robot_worlds_13.server.robot.world.Obstacle;
import robot_worlds_13.server.robot.world.AbstractWorld;

import java.util.*;

public class RobotWorldApi {
    static AbstractWorld world;

    public static void getCurrentWorld(Context ctx) {

        Map<String, Object> worldData = new HashMap<>();

        if (world != null) {
            List<Map<String, Object>> obstacles = new ArrayList<>();
//            List<Obstacle> obstacleList = world.get();
            Map<String, Object> obstacleData = new HashMap<>();

            for (Obstacle obstacle : world.getAllObstacles()) {
                obstacleData.put("obstacleType", obstacle.getType());
                obstacleData.put("x", obstacle.getBottomLeftX());
                obstacleData.put("y", obstacle.getBottomLeftY());

                obstacles.add(obstacleData);
            }

            worldData.put("obstacles", obstacles);
            worldData.put("name", "currentWorld");
            //worldData.put("worldSize", );
        }

        ctx.json(worldData);
    }

/*
    public static void restoreSavedWorld(Context ctx) {
        String worldName = ctx.pathParam("world");
        RestoreCommand.restoreWorld(worldName);

        World world = World.getInstance();

        Map<String, Object> worldData = new HashMap<>();

        if (world != null) {
            List<Map<String, Object>> obstacles = new ArrayList<>();
            List<Obstacle> obstacleList = world.getType;

            for (Obstacle obstacle : obstacleList) {
                Map<String, Object> obstacleData = new HashMap<>();
                obstacleData.put("obstacleType", obstacle.getType());
                obstacleData.put("x", obstacle.getBottomLeftX());
                obstacleData.put("y", obstacle.getBottomLeftY());

                obstacles.add(obstacleData);
            }

            worldData.put("obstacles", obstacles);
            worldData.put("name", worldName);
            worldData.put("worldSize", l);
        }
        ctx.json(worldData);
    }

 */
}

