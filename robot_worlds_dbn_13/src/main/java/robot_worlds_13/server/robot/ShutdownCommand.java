package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("off");
    }

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
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
        return false;
    }
}
