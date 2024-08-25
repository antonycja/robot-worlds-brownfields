package robot_worlds_13.server.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Represents a command to allow a robot to look around and gather information about its surroundings.
 * When executed, this command utilizes the abstract world data to gather information about nearby obstacles.
 * It generates a list of obstructions and provides this information as a response to the robot and GUI.
 */
public class LookCommand extends Command{

    /**
     * Executes the look command for the given target robot.
     * @param target The robot executing the look command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {

        // Look around using abstract world data
        target.worldData.giveCurrentRobotInfo(target);


        // Return list with details of obstructions
        ArrayList<Object> obstructions = target.worldData.lookAround();

        // Construct response data and state
        Map<String, Object> data = new HashMap<>();
        data.put("objects", obstructions);
        Map<String, Object> state = target.getRobotState();

        // Set response to the robot
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

        // Set GUI response
        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
//        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }

    public LookCommand() {
        super("look");
    }
}
