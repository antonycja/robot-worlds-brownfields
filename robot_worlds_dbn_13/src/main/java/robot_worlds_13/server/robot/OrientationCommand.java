package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class OrientationCommand extends Command {
    
    public OrientationCommand() {
        super("orientation");
    }

    @Override
    public boolean execute(Robot target) {

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = target.getRobotState();
        target.worldData.giveCurrentRobotInfo(target);
        target.setResponseToRobot(ServerProtocol.buildResponse("Orientation: " + String.valueOf(target.getCurrentDirection())));

        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }
}
