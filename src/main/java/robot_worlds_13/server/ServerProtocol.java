package robot_worlds_13.server;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

/**
 * This class provides methods for building server responses in JSON format.
 */
public class ServerProtocol {
    private static Gson gson = new Gson();

    /**
     * Builds a JSON response with a result, data, and state.
     * @param result The result of the operation.
     * @param data Additional data to include in the response.
     * @param state The state of the operation.
     * @return A JSON string representing the response.
     */
    public static String buildResponse(String result, Map<String, Object> data, Map<String, Object> state) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", result);
        responseMap.put("data", data);
        responseMap.put("state", state);

        return gson.toJson(responseMap);
    }

    /**
     * Builds a JSON response with a result and data.
     * @param result The result of the operation.
     * @param data Additional data to include in the response.
     * @return A JSON string representing the response.
     */
    public static String buildResponse(String result, Map<String, Object> data) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", result);
        responseMap.put("data", data);
        return gson.toJson(responseMap);
    }

    /**
     * Builds a JSON response with only the state.
     * @param state The state of the operation.
     * @return A JSON string representing the response.
     */
    public static String buildResponse(Map<String, Object> state) {
        Map<String, Object> responseMap = new HashMap<>();
        if (state != null) {
            responseMap.put("state", state);
        }
        return gson.toJson(responseMap);
    }

    /**
     * Builds a JSON response with a command.
     * @param response The command response.
     * @return A JSON string representing the response.
     */
    public static String buildResponse(String response) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("command", response);
        return gson.toJson(responseMap);
    }
}
