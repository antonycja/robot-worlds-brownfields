package robot_worlds_13.client;

import java.util.ArrayList;
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
        
        // command
        String command = parts[0];  // The first part is always the command
        
        // 
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

    public static Map<String, Object> jsonRequestBuilder (String input, String nameOfRobot) {
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
        commandMap.put("robot", nameOfRobot);
        commandMap.put("command", command);
        commandMap.put("arguments", arguments);

        return commandMap;
    }

    public static String jsonResponseUnpacker (String jsonResponse) {
        try {
            // Map<String, Object> responseMap = gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>(){}.getType());
            Map<String, Object> responseMap = gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>(){}.getType());

            // first check for error commands
            if ("ERROR".equals(responseMap.get("result"))) {
                Map<String, Object> dataReceived = (Map<String, Object>) responseMap.get("data");
                if (dataReceived.containsKey("message"))
                    return "ERROR: " + (String) dataReceived.get("message");
                else {
                    return (String) responseMap.get("data");
                }
            }


            // if display message
            if ("DISPLAY".equals(responseMap.get("result"))) {
                Map<String, Object> dataReceived = (Map<String, Object>) responseMap.get("data");
                if (dataReceived.containsKey("message"))
                    return (String) dataReceived.get("message");
                else {
                    return (String) responseMap.get("data");
                }
            }

            String message = "";
            // if (responseMap.containsKey("result")) {
            //     String resultOfCommand = (String) responseMap.get("result");
            //     // message += resultOfCommand;
            // }
            
            if (responseMap.get("data") != null) {
                if (responseMap.get("data") instanceof Map) {
                    Map<String, Object> innerMap = (Map<String, Object>) responseMap.get("data");
                    message += "Data: \n";
                    if (innerMap.get("message") != null) {
                        String messageResponse = (String) innerMap.get("message");
                        message += "    Message: " + messageResponse + "\n";
                    }
                    if (innerMap.get("distance") != null) { // robots steps away
                        String distance = String.valueOf(innerMap.get("distance"));
                        message += "    Distance: " + distance  + "\n";
                    }
                    if (innerMap.get("robot") != null) { // its name
                        String robot = (String) innerMap.get("robot");
                        message += "    Robot: " + robot  + "\n";
                    }
                    if (innerMap.get("state") != null) { // nested
                        message += "    State: " + "\n";
                        Map<String, Object> stateMap = (Map<String, Object>) innerMap.get("state");
                        String status = "        Status: " + stateMap.get("status") + "\n";
                        message += status;
                        String position = "        Position: " + stateMap.get("position") + "\n";
                        message += position;
                        String direction = "        Direction: " + stateMap.get("direction") + "\n";
                        message += direction;
                        String shots = "        Shots: " + (String.valueOf((int) Math.round((double) stateMap.get("shots")))) + "\n";
                        message += shots;
                        String shields = "        Shields: " + (String.valueOf((int) Math.round((double) stateMap.get("shields")))) + "\n";
                        message += shields;
                    }
                    if (innerMap.get("objects") != null) {  // obstacles one, nested within
                        message += "    Objects: " + "\n";
                        ArrayList<Object> objects = (ArrayList<Object>) innerMap.get("objects");
                        for (Object object: objects) {
                            Map<String, Object> obstacleMap = gson.fromJson(((String) object), new TypeToken<Map<String, Object>>(){}.getType());
                            String direction = "        Direction: " + obstacleMap.get("direction") + "\n";
                            message += direction;
                            String type = "        Type: " + obstacleMap.get("type") + "\n";
                            message += type;
                            String distance = "        Distance: " + String.valueOf(obstacleMap.get("distance")) + "\n";
                            message += distance;
                            message += "\n";
                        }
                    }
                    if (innerMap.get("position") != null) {
                        Object position = (Object) innerMap.get("position");
                        message += "    Position: " + position  + "\n";
                    }
                    if (innerMap.get("visibility") != null) {
                        String visibility = String.valueOf((int) Math.round((double)innerMap.get("visibility")));
                        message += "    Visibility: " + visibility  + " steps\n";
                    }
                    if (innerMap.get("reload") != null) {
                        String reload = String.valueOf((int) Math.round((double)innerMap.get("reload")));
                        message += "    Reload: " + reload  + " seconds\n";
                    }
                    if (innerMap.get("repair") != null) {
                        String repair = String.valueOf((int) Math.round((double)innerMap.get("repair")));
                        message += "    Repair: " + repair  + " seconds\n";
                    }
                    if (innerMap.get("shields") != null) {
                        String shields = String.valueOf((int) Math.round((double)innerMap.get("shields")));
                        message += "    Shields: " + shields + " hits\n";
                    }
                    

                } else {
                    System.out.println("result is not a JSON object.");
                }
            }

            if (responseMap.get("state") != null) {
                if (responseMap.get("state") instanceof Map) {
                    message += "State: \n";
                    Map<String, Object> innerMap = (Map<String, Object>) responseMap.get("state");
                    String status = "    Status: " + innerMap.get("status") + "\n";
                    message += status;
                    String position = "    Position: " + innerMap.get("position") + "\n";
                    message += position;
                    String direction = "    Direction: " + innerMap.get("direction") + "\n";
                    message += direction;
                    String shots = "    Shots: " + (String.valueOf((int) Math.round((double) innerMap.get("shots")))) + "\n";
                    message += shots;
                    String shields = "    Shields: " + (String.valueOf((int) Math.round((double) innerMap.get("shields")))) + "\n";
                    message += shields;
                    
                    return message;
                } else {
                    System.out.println("state is not a JSON object.");
                    return "No";
                }
            }

            if (responseMap.get("command") != null) {
                return (String) responseMap.get("command");
                }
            

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("No json found");
            return jsonResponse;
        }
        return jsonResponse;
    }

    // gets the position of the robot
    public static String getRobotFormattedPosition (String jsonResponse) {
        Map<String, Object> responseMap = gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>(){}.getType());
        if (responseMap.get("state") != null) {
            if (responseMap.get("state") instanceof Map) {
                Map<String, Object> innerMap = (Map<String, Object>) responseMap.get("state");
                return innerMap.get("position") + "\n";
            }
        }
        return "";
    }

    public static String formatJsonString(String jsonString) {
        try {
            Map<String, Object> dataMap = gson.fromJson(jsonString, new TypeToken<Map<String, Object>>(){}.getType());
            return formatData(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to parse JSON: " + e.getMessage();
        }
    }

    /**
     * Formats a map of data into a user-friendly string, with each key-value pair on a new line.
     * Recursively formats nested maps or JSON strings.
     * @param data The map containing the data to format.
     * @return A formatted string representation of the map.
     */
    private static String formatData(Map<String, Object> data) {
        StringBuilder formattedString = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                // Recursively format nested maps
                formattedString.append(entry.getKey()).append(":\n");
                formattedString.append(formatData((Map<String, Object>) value));
            } else if (value instanceof String && isJSONString((String) value)) {
                // Check if the string is JSON and parse it if so
                formattedString.append(entry.getKey()).append(":\n");
                Map<String, Object> nestedMap = gson.fromJson((String) value, new TypeToken<Map<String, Object>>(){}.getType());
                formattedString.append(formatData(nestedMap));
            } else {
                // Append each key-value pair in the desired format
                formattedString.append(entry.getKey()).append(": ").append(value).append("\n");
            }
        }
        return formattedString.toString();
    }

    /**
     * Checks if a string is in JSON format.
     * @param str The string to check.
     * @return true if the string is a JSON formatted string, false otherwise.
     */
    private static boolean isJSONString(String str) {
        try {
            gson.fromJson(str, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}