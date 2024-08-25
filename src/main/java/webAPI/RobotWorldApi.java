package webAPI;

import io.javalin.http.Context;
import database.orm.ConnectDB;
import database.orm.WorldDAI;
import database.orm.ObjectsDO;
import database.orm.WorldDO;

import java.util.*;

public class RobotWorldApi {

    private static final ConnectDB connectDB = new ConnectDB();
    private static final WorldDAI worldDAO = connectDB.worldDAO;

    public static void launchRobot(Context ctx) {
        String robotName = ctx.pathParam("name");

        if (robotName == null || robotName.isEmpty()) {
            ctx.status(400).json(Collections.singletonMap("error", "Robot name is required"));
            return;
        }

        Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
        String command = (String) requestBody.get("command");
        Map<String, Object> parameters = (Map<String, Object>) requestBody.get("parameters");

        if ("Launch".equals(command)) {
            String worldName = (String) parameters.get("worldName");

            // Fetch world data
            WorldDO worldData = worldDAO.getWorldData(worldName);
            if (worldData == null) {
                ctx.status(404).json(Collections.singletonMap("error", "World not found"));
                return;
            }

            // Simulate launching the robot in the world
            // You need to implement the actual logic here

            ctx.status(200).json(Collections.singletonMap("message", "Robot launched successfully in world " + worldName));
        } else {
            ctx.status(400).json(Collections.singletonMap("error", "Invalid command"));
        }
    }
    public static void getCurrentWorld(Context ctx) {
        String worldName = ctx.queryParam("world");

        if (worldName == null || worldName.isEmpty()) {
            // Fetch all worlds
            List<WorldDO> allWorlds = worldDAO.getAllWorlds();
            if (allWorlds.isEmpty()) {
                ctx.status(404).json(Collections.singletonMap("error", "No worlds found"));
                return;
            }

            List<Map<String, Object>> worldsResponse = new ArrayList<>();
            for (WorldDO world : allWorlds) {
                Map<String, Object> worldData = new HashMap<>();
                worldData.put("worldName", world.getName());
                worldData.put("width", world.getWidth());
                worldData.put("height", world.getHeight());

                List<Map<String, Object>> obstacles = new ArrayList<>();
                List<ObjectsDO> obstacleList = worldDAO.getObjectData(world.getName());
                if (obstacleList != null) {
                    for (ObjectsDO obs : obstacleList) {
                        Map<String, Object> obstacleData = new HashMap<>();
                        obstacleData.put("obstacleType", obs.getType());
                        obstacleData.put("x", obs.getXPosition());
                        obstacleData.put("y", obs.getYPosition());
                        obstacles.add(obstacleData);
                    }
                }

                worldData.put("obstacles", obstacles);
                worldsResponse.add(worldData);
            }

            ctx.json(worldsResponse);
        } else {
            // Fetch specific world data
            WorldDO worldData = worldDAO.getWorldData(worldName);
            if (worldData == null) {
                ctx.status(404).json(Collections.singletonMap("error", "World not found"));
                return;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("worldName", worldData.getName());
            response.put("width", worldData.getWidth());
            response.put("height", worldData.getHeight());

            List<Map<String, Object>> obstacles = new ArrayList<>();
            List<ObjectsDO> obstacleList = worldDAO.getObjectData(worldName);
            if (obstacleList != null) {
                for (ObjectsDO obs : obstacleList) {
                    Map<String, Object> obstacleData = new HashMap<>();
                    obstacleData.put("obstacleType", obs.getType());
                    obstacleData.put("x", obs.getXPosition());
                    obstacleData.put("y", obs.getYPosition());
                    obstacles.add(obstacleData);
                }
            }

            response.put("obstacles", obstacles);
            ctx.json(response);
        }
    }

    public static void restoreSavedWorld(Context ctx) {
        String worldName = ctx.pathParam("world");

        // Validate the world name parameter
        if (worldName == null || worldName.isEmpty()) {
            ctx.status(400).json(Collections.singletonMap("error", "World name is required"));
            return;
        }

        // Fetch world data from the database
        WorldDO worldData = worldDAO.getWorldData(worldName);

        if (worldData == null) {
            ctx.status(404).json(Collections.singletonMap("error", "World not found"));
            return;
        }

        // Prepare the response data
        Map<String, Object> response = new HashMap<>();
        response.put("worldName", worldData.getName());
        response.put("width", worldData.getWidth());
        response.put("height", worldData.getHeight());

        // Fetch obstacle data
        List<Map<String, Object>> obstacles = new ArrayList<>();
        List<ObjectsDO> obstacleList = worldDAO.getObjectData(worldName);

        if (obstacleList != null) {
            for (ObjectsDO obs : obstacleList) {
                Map<String, Object> obstacleData = new HashMap<>();
                obstacleData.put("obstacleType", obs.getType());
                obstacleData.put("x", obs.getXPosition());
                obstacleData.put("y", obs.getYPosition());

                obstacles.add(obstacleData);
            }
        }

        response.put("obstacles", obstacles);

        // Send the JSON response
        ctx.status(200).json(response);
    }

    public static void saveWorld(Context ctx) {
        // Extract world details from request body
        String worldName = ctx.formParam("worldName");
        String widthParam = ctx.formParam("width");
        String heightParam = ctx.formParam("height");

        if (worldName == null || worldName.isEmpty()) {
            ctx.status(400).json(Collections.singletonMap("error", "World name is required"));
            return;
        }

        int width;
        int height;

        try {
            width = Integer.parseInt(widthParam);
            height = Integer.parseInt(heightParam);
        } catch (NumberFormatException e) {
            ctx.status(400).json(Collections.singletonMap("error", "Invalid width or height"));
            return;
        }

        // Check if world already exists
        WorldDO existingWorld = worldDAO.getWorldData(worldName);
        if (existingWorld != null) {
            ctx.status(409).json(Collections.singletonMap("error", "World already exists"));
            return;
        }

        // Save the new world data
        WorldDO newWorld = new WorldDO();
        newWorld.setName(worldName);
        newWorld.setWidth(width);
        newWorld.setHeight(height);

        worldDAO.saveWorld(newWorld);

        ctx.status(201).json(Collections.singletonMap("message", "World saved successfully"));
    }
}
