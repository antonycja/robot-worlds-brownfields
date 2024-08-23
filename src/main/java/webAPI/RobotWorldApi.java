package webAPI;

import io.javalin.http.Context;
import database.SqlCommands;

import java.util.*;

public class RobotWorldApi {

    private static final SqlCommands sqlCommands = new SqlCommands();

    public static void getCurrentWorld(Context ctx) {
        String worldName = ctx.queryParam("world"); // Assuming you get the world name from a query parameter
        if (worldName == null || worldName.isEmpty()) {
            ctx.status(400).json(Collections.singletonMap("error", "World name is required"));
            return;
        }

        // Fetch world data
        Map<String, Object> worldData = sqlCommands.restoreWorldData("world", "objects", "types", worldName);

        if (worldData == null || worldData.isEmpty()) {
            ctx.status(404).json(Collections.singletonMap("error", "World not found"));
            return;
        }

        ctx.json(worldData);
    }

    public static void restoreSavedWorld(Context ctx) {
        String worldName = ctx.pathParam("world");

        if (worldName == null || worldName.isEmpty()) {
            ctx.status(400).json(Collections.singletonMap("error", "World name is required"));
            return;
        }

        // Fetch world data
        Map<String, Object> worldData = sqlCommands.restoreWorldData("world", "objects", "types", worldName);

        if (worldData == null || worldData.isEmpty()) {
            ctx.status(404).json(Collections.singletonMap("error", "World not found"));
            return;
        }

        List<Map<String, Object>> obstacles = new ArrayList<>();
        List<Map<String, Object>> obstacleList = (List<Map<String, Object>>) worldData.get("objects");

        if (obstacleList != null) {
            for (Map<String, Object> obs : obstacleList) {
                Map<String, Object> obstacleData = new HashMap<>();
                obstacleData.put("obstacleType", obs.get("type")); // Adjust based on actual column names
                obstacleData.put("x", obs.get("x_position")); // Adjust based on actual column names
                obstacleData.put("y", obs.get("y_position")); // Adjust based on actual column names

                obstacles.add(obstacleData);
            }
        }

        worldData.put("obstacle", obstacles);
        ctx.json(worldData);
    }
}
