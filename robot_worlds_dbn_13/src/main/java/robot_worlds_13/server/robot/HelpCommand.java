package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Represents a command to provide help information about available commands to a robot.
 * When executed, this command provides a list of supported commands and their usage instructions.
 */
public class HelpCommand extends Command {

    /**
     * Constructs a HelpCommand object.
     * Initializes the command name as "help".
     */
    public HelpCommand() {
        super("help");
    }

    /**
     * Executes the help command for the given target robot.
     * @param target The robot to which the help information will be provided.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {
        // Help message providing information about available commands
        String message = "I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move back by specified number of steps, e.g. 'BACK 10'\n" +
                "LEFT - turn left by 90 degrees\n"+
                "RIGHT - turn right by 90 degrees\n" +
                "OFF  - Shut down robot";

        // Construct response data and state
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        Map<String, Object> state = target.getRobotState();

        // Set response to the robot
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

        data.clear();
        data.put("message", "NONE");
        state.clear();

        // Set GUI response
        state = target.getGUIRobotState();
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }
}
