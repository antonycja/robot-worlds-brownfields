package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Command to retrieve the state of the robot.
 */
public class StateCommand extends Command {

    /**
     * Executes the state command to retrieve the state of the target robot.
     * @param target The robot to retrieve the state from.
     * @return Always returns true to indicate successful execution.
     */
    @Override
    public boolean execute(Robot target) {
        
        // String stateMessage = target.getStatus();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = target.getRobotState();

        data.put("message", "done");
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data ,state));

        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
//        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }

    /**
     * Initializes the state command with a given argument.
     * @param argument The argument associated with the state command (unused).
     */
    public StateCommand (String argument) {
        super("state");
    }
}
