package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Command to shut down the robot.
 */
public class ShutdownCommand extends Command {

    /**
     * Initializes the shutdown command.
     */
    public ShutdownCommand() {
        super("quit");
    }

    /**
     * Executes the shutdown command on the target robot.
     * @param target The robot to shut down.
     * @return Always returns false to indicate that the robot should be shut down.
     */
    @Override
    public boolean execute(Robot target) {
        target.worldData.giveCurrentRobotInfo(target);
        target.setResponseToRobot(ServerProtocol.buildResponse("Shutting down..."));

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = target.getRobotState();
        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
//        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
        return false;
    }
}
