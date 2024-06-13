package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Represents a command to query and provide information about the orientation of a robot.
 * When executed, this command retrieves the current direction of the robot and provides it as a response.
 */
public class OrientationCommand extends Command {

    /**
     * Constructs an OrientationCommand object.
     * Initializes the command name as "orientation".
     */
    public OrientationCommand() {
        super("orientation");
    }

    /**
     * Executes the orientation command for the given target robot.
     * @param target The robot executing the orientation command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {

        // Construct response data and state with the current orientation of the robot
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = target.getRobotState();

        // Retrieve current robot information from the world data
        target.worldData.giveCurrentRobotInfo(target);
        target.setResponseToRobot(ServerProtocol.buildResponse("Orientation: " + String.valueOf(target.getCurrentDirection())));

        // Set GUI response
        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }
}
