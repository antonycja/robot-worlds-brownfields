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

        return true;
    }

    public StateCommand (String argument) {
        super("state");
    }
}
