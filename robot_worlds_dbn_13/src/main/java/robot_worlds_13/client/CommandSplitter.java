package robot_worlds_13.client;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for splitting a user's input into a command
 * and its associated arguments suitable for JSON formatting.
 */
public class CommandSplitter {

    /**
     * Splits the input string into a command and its arguments.
     *
     * @param input the user input string, e.g., "forward 10" or "back 50"
     * @return a map containing the command and an array of arguments
     */
    public static Map<String, Object> splitCommand(String input) {
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
}