package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class StateCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        
        String stateMessage = target.getStatus();

        Map<String, Object> data = new HashMap<>();
        data.put("message", stateMessage);
            
        Map<String, Object> state = new HashMap<>();
        state.put("position", new int[] {target.getPosition().getX(), target.getPosition().getY()});

        
        target.setStatus(ServerProtocol.buildResponse("OK", data, state));

        return true;
    }

    public StateCommand (String argument) {
        super("state");
    }
}
