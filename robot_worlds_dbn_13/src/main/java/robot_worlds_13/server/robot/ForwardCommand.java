package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.IWorld;
import robot_worlds_13.server.robot.world.IWorld.UpdateResponse;

public class ForwardCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());

        // System.out.println("in forward");
        // where towards is either "front" or "back"

        target.worldData.giveCurrentRobotInfo(target);
        
        IWorld.UpdateResponse responseGiven = target.worldData.updatePosition(nrSteps);
        
        if (responseGiven == UpdateResponse.FAILED_OBSTRUCTED) {
            // obstacle
            target.setStatus(ServerProtocol.buildResponse("ERROR",
            Map.of("message", "Obstructed by obstacle"), null));
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_OBSTRUCTED_BY_ROBOT) {
            // robot
            target.setStatus(ServerProtocol.buildResponse("ERROR",
            Map.of("message", "Obstructed by other robot"), null));
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            // outside world
            target.setStatus(ServerProtocol.buildResponse("ERROR",
            Map.of("message", "Trying to move outside world"), null));
            return true;
        }

        if (target.updatePosition(nrSteps, "front")){
            
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Done");
            Map<String, Object> state = new HashMap<>();
            state.put("position", new int[] {target.getPosition().getX(), target.getPosition().getY()});
            state.put("direction", target.getCurrentDirection());
            state.put("shields", target.shields);
            state.put("shots", target.ammo);
            state.put("status", target.getStatus());
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
        }
        else {
            System.out.println("Error");
        }



        return true;
    }

    public ForwardCommand(String argument) {
        super("forward", argument);
    }
}

