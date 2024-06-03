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
        HashMap<String, ArrayList<Object>> obstructions = target.worldData.lookAround();
        // for (String obstacleType: obstructions.keySet()) {
        //     if (obstacleType.equals("North")) {
                
        //     }
        // }

        // // set status with look
        // target.setStatus("Turned left.");

        Map<String, Object> data = new HashMap<>();
        data.put("message", obstructions); // should be removed
        Map<String, Object> state = new HashMap<>();
        state.put("position", new int[] {target.getPosition().getX(), target.getPosition().getY()});
        state.put("direction", target.getCurrentDirection());
        target.setStatus(ServerProtocol.buildResponse("OK", data, state));
        return true;
    }

    public LookCommand() {
        super("look");
    }
}
