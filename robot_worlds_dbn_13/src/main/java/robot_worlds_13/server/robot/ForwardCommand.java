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
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = target.getRobotState();
        
        IWorld.UpdateResponse responseGiven = target.worldData.updatePosition(nrSteps);

        if (target.getStatus() == "REPAIR") {
            // repair
            data.clear();
            data.put("message", "Movement not allowed whilst repairing robot");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }

        if (target.getStatus() == "RELOAD") {
            // reload
            data.clear();
            data.put("message", "Movement not allowed whilst repairing robot");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }
        
        if (responseGiven == UpdateResponse.FAILED_OBSTRUCTED) {
            // obstacle
            data.clear();
            data.put("message", "Obstructed - There is an obstacle in the way");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_OBSTRUCTED_BY_ROBOT) {
            // robot
            data.clear();
            data.put("message", "Obstructed - There is a robot in the way");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            // outside world
            data.clear();
            data.put("message", "Obstructed - Trying to move outside world");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }

        if (target.updatePosition(nrSteps, "front")){
            
            data.clear();
            data.put("message", "Done");
            state.clear();
            state = target.getRobotState();
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

            data.clear();
            data.put("message", "MOVE");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
        }
        return true;
    }

    public ForwardCommand(String argument) {
        super("forward", argument);
    }
}

