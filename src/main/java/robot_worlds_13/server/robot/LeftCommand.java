package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Represents a command to turn a robot left by 90 degrees.
 * When executed, this command updates the robot's direction and generates appropriate responses for the robot and GUI.
 */
public class LeftCommand extends Command {

    /**
     * Executes the left command for the given target robot.
     * @param target The robot executing the left command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {
        // Retrieve current robot information from the world data
        target.worldData.giveCurrentRobotInfo(target);

        // Update the direction of the robot to the left
        target.worldData.updateDirection(false);
        target.updateDirection("left");

        // Construct response data and state
        Map<String, Object> data = new HashMap<>();
        data.put("message", target.getName()+" Turned left. Now facing " + target.getCurrentDirection());
        Map<String, Object> state = target.getRobotState();

        // Set response to the robot
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

        // Set GUI response
        data.clear();
        data.put("message", "LEFT");
        state.clear();
        state = target.getGUIRobotState();
//        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }


    /**
     * Constructs a LookCommand object.
     * Initializes the command name as "look".
     */
    public LeftCommand() {
        super("left");
    }
}
