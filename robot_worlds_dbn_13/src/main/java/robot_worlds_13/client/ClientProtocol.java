package robot_worlds_13.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class is responsible for splitting a user's input into a command
 * and its associated arguments suitable for JSON formatting.
 */
public class ClientProtocol {
    static private Gson gson = new Gson();

    /**
     * Splits the input string into a command and its arguments.
     *
     * @param input the user input string, e.g., "forward 10" or "back 50"
     * @return a map containing the command and an array of arguments
     */
    public static Map<String, Object> jsonRequestBuilder (String input) {
        String[] parts = input.trim().split("\\s+", 2);  // Split on whitespace, limit 2 parts
        String command = parts[0];  // The first part is always the command

        Object[] arguments;
        if (parts.length > 1) {
            // If there are arguments, split them further by spaces
            arguments = parts[1].split("\\s+");
        } else {
            // No arguments provided
            arguments = new Object[0];
        }

        // Prepare the command and arguments for JSON formatting
        HashMap<String, Object> commandMap = new HashMap<>();
        commandMap.put("command", command);
        commandMap.put("arguments", arguments);

        return commandMap;
    }

    public static String jsonResponseUnpacker (String jsonResponse) {
        try {
            Map<String, Object> responseMap = gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>(){}.getType());
            
            if ("OK".equals(responseMap.get("result"))) {
                return "" + responseMap.get("data");
            } else {
                return "Error executing command: " + responseMap.get("data");
            }
        } catch (Exception e) {
            System.out.println("No json found");
            return jsonResponse;
        }
        
        
    }
}