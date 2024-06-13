package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Represents a command to reload ammunition for a robot.
 * When executed, this command simulates the reload time for the robot and replenishes its ammunition.
 */
public class ReloadCommand extends Command{

    /**
     * Constructs a ReloadCommand object.
     * Initializes the command name as "reload".
     * @param robot The robot for which the reload command is intended.
     */
    public ReloadCommand(Robot robot) {
        super("reload"); // Call to Command constructor
    }

    /**
     * Constructs a ReloadCommand object.
     * Initializes the command name as "reload".
     */
    public ReloadCommand() {
        super("reload");
    }

    /**
     * Executes the reload command for the given target robot.
     * @param target The robot executing the reload command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {
        // Simulate reload time and replenish ammunition
        
        int reloadTime = target.getReloadTime();
        target.reload(reloadTime);

        // Construct response data and state
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Done");
        Map<String, Object> state = target.getRobotState();

        // Set response to the robot
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

        // Set GUI response
        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
        
        return true;
    }
}