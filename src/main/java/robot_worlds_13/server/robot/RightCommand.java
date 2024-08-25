package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Represents a command to turn a robot right by 90 degrees.
 * When executed, this command updates the robot's direction to the right and generates appropriate responses for the robot and GUI.
 */
public class RightCommand extends Command {

    /**
     * Executes the right command for the given target robot.
     * @param target The robot executing the right command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {
        // Retrieve current robot information from the world data
        target.worldData.giveCurrentRobotInfo(target);

        // Update the direction of the robot to the right
        target.worldData.updateDirection(true);
        target.updateDirection("right");

        // Construct response data and state
        Map<String, Object> data = new HashMap<>();
        data.put("message", target.getName()+" Turned right. Now facing " + target.getCurrentDirection());
        Map<String, Object> state = target.getRobotState();

        // Set response to the robot
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

        // Set GUI response
        data.clear();
        data.put("message", "RIGHT");
        state.clear();
        state = target.getGUIRobotState();
//        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }

    /**
     * Constructs a RightCommand object.
     * Initializes the command name as "right".
     */
    public RightCommand() {
        super("right");
    }
}
