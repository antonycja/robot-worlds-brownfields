package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class RightCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        
        target.worldData.giveCurrentRobotInfo(target);

        target.worldData.updateDirection(true);
        target.updateDirection("right");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Done");
        Map<String, Object> state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

        data.clear();
        data.put("message", "RIGHT");
        state.clear();
        state = target.getGUIRobotState();
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }

    public RightCommand() {
        super("right");
    }
}
