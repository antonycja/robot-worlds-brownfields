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

    public static void getCurrentWorld(Context ctx) {
        String worldName = ctx.queryParam("world"); // Assuming you get the world name from a query parameter
        if (worldName == null || worldName.isEmpty()) {
            ctx.status(400).json(Collections.singletonMap("error", "World name is required"));
            return;
        }

        // Fetch world data
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
                obstacleData.put("obstacleType", obs.getType()); // Adjust based on actual column names
                obstacleData.put("x", obs.getXPosition()); // Adjust based on actual column names
                obstacleData.put("y", obs.getYPosition()); // Adjust based on actual column names

                obstacles.add(obstacleData);
            }
        }

        response.put("obstacles", obstacles);
        ctx.json(response);
    }

    public static void restoreSavedWorld(Context ctx) {
        String worldName = ctx.pathParam("world");

        if (worldName == null || worldName.isEmpty()) {
            ctx.status(400).json(Collections.singletonMap("error", "World name is required"));
            return;
        }

        // Fetch world data
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
