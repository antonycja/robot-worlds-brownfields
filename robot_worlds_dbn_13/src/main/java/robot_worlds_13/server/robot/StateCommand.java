package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class StateCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        
        // String stateMessage = target.getStatus();
            
        Map<String, Object> state = target.getRobotState();

        target.setResponseToRobot(ServerProtocol.buildResponse(state));

        Map<String, Object> data = new HashMap<>();
        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }

    public StateCommand (String argument) {
        super("state");
    }
}
