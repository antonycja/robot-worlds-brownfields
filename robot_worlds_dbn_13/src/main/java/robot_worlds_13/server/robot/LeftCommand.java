package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class LeftCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        
        target.worldData.giveCurrentRobotInfo(target);

        target.worldData.updateDirection(false);
        target.updateDirection("left");

        Map<String, Object> data = new HashMap<>();
        data.put("message", "Done");
        Map<String, Object> state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
        return true;
    }

    public LeftCommand() {
        super("left");
    }
}
