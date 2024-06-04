package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.IWorld.UpdateResponse;

public class BackCommand extends robot_worlds_13.server.robot.Command {

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());

        target.worldData.giveCurrentRobotInfo(target);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = target.getRobotState();


        // -nrsteps since the update in abstract world has no front vs back
        if (target.worldData.updatePosition(-nrSteps) == UpdateResponse.FAILED_OBSTRUCTED) {
            // obstacle
            data.clear();
            data.put("message", "Obstructed - There is an obstacle in the way");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }

        else if (target.worldData.updatePosition(-nrSteps) == UpdateResponse.FAILED_OBSTRUCTED_BY_ROBOT) {
            // robot
            data.clear();
            data.put("message", "Obstructed - There is a robot in the way");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }

        else if (target.worldData.updatePosition(-nrSteps) == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            // outside world
            data.clear();
            data.put("message", "Obstructed - Trying to move outside world");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }

        // where towards is either "front" or "back"
        if (target.updatePosition(nrSteps, "back")){
            data.clear();
            data.put("message", "Done");
            state.clear();
            state = target.getRobotState();
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
        }
        return true;
    }

    public BackCommand (String argument) {
        super("back", argument);
    }
}
