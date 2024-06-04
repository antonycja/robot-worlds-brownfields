package robot_worlds_13.server.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class LookCommand extends Command{
    
    @Override
    public boolean execute(Robot target) {
        
        // look using abstract world
        target.worldData.giveCurrentRobotInfo(target);
        

        // return list with details
        ArrayList<Object> obstructions = target.worldData.lookAround();

        Map<String, Object> data = new HashMap<>();
        data.put("objects", obstructions);
        Map<String, Object> state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
        return true;
    }

    public LookCommand() {
        super("look");
    }
}
