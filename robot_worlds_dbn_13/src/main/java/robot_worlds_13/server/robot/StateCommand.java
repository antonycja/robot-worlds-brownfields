package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class StateCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        
        // String stateMessage = target.getStatus();

        Map<String, Object> data = new HashMap<>();
        data.put("message", "NORMAL");
            
        Map<String, Object> state = new HashMap<>();
        state.put("position", new int[] {target.getPosition().getX(), target.getPosition().getY()});
        state.put("direction", target.getCurrentDirection());
        state.put("shields", target.shields);
        state.put("shots", target.ammo);

        target.setStatus(ServerProtocol.buildResponse("OK", data, state));

        return true;
    }

    public StateCommand (String argument) {
        super("state");
    }
}
