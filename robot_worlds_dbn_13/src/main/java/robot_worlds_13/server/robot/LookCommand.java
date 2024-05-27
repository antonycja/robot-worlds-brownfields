package robot_worlds_13.server.robot;

import java.util.ArrayList;
import java.util.HashMap;

public class LookCommand extends Command{
    
    @Override
    public boolean execute(Robot target) {
        
        // look using abstract world
        target.worldData.giveCurrentRobotInfo(target);
        

        // return list with details
        HashMap<String, ArrayList<Object>> obstructions = target.worldData.lookAround();
        for (String obstacleType: obstructions.keySet()) {
            if (obstacleType.equals("North")) {
                
            }
        }

        // set status with look
        target.setStatus("Turned left.");
        return true;
    }

    public LookCommand() {
        super("look");
    }
}
