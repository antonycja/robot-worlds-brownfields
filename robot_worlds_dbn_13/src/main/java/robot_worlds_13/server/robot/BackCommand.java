package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.IWorld;
import robot_worlds_13.server.robot.world.IWorld.UpdateResponse;

/**
 * Executes the "back" command for a robot, moving it backwards by the specified
 * number of steps.
 * If the robot is in the "REPAIR" or "RELOAD" status, movement is not allowed
 * and a message is returned.
 * If there is an obstacle, robot, or the robot would move outside the world, a
 * message is returned.
 * Otherwise, the robot is moved backwards and a "Done" message is returned.
 *
 * @param target the robot to execute the command on
 * @return true if the command was executed successfully, false otherwise
 */
public class BackCommand extends Command {

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());

        target.worldData.giveCurrentRobotInfo(target);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = target.getRobotState();

        if (target.getStatus() == "REPAIR") {
            // repair
            data.clear();
            data.put("message", "Movement not allowed whilst repairing robot");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

            target.previouPosition = target.position;
            data.clear();
            data.put("message", "BACK");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        if (target.getStatus() == "RELOAD") {
            // reload
            data.clear();
            data.put("message", "Movement not allowed whilst repairing robot");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            data.clear();
            data.put("message", "BACK");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        IWorld.UpdateResponse responseGiven = target.worldData.updatePosition(-nrSteps);

        // -nrsteps since the update in abstract world has no front vs back
        if (responseGiven == UpdateResponse.FAILED_OBSTRUCTED) {
            // obstacle
            data.clear();
            data.put("message", "Obstructed - There is an obstacle in the way");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            data.clear();
            data.put("message", "BACK");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_DEAD) {
            // if jumped into a pit
            data.clear();
            data.put("message", "Robot fell into a bottomless pit and died");
            target.setDeadStatus();
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            target.updatePosition(nrSteps, "front");
            data.clear();
            data.put("message", "FRONT");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return false;
        }

        else if (responseGiven == UpdateResponse.FAILED_OBSTRUCTED_BY_ROBOT) {
            // robot
            data.clear();
            data.put("message", "Obstructed - There is a robot in the way");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            data.clear();
            data.put("message", "BACK");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            // outside world
            data.clear();
            data.put("message", "Obstructed - Trying to move outside world");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            data.clear();
            data.put("message", "BACK");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        // where towards is either "front" or "back"
        if (target.updatePosition(nrSteps, "back")) {
            data.clear();
            data.put("message", "Done");
            state.clear();
            state = target.getRobotState();
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

            data.clear();
            data.put("message", "BACK");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
        }
        return true;
    }

    public BackCommand(String argument) {
        super("back", argument);
    }
}
