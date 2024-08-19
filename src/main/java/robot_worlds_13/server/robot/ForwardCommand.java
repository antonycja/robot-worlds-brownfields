package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.IWorld;
import robot_worlds_13.server.robot.world.IWorld.UpdateResponse;

/**
 * Represents a command to move a robot forward by a specified number of steps.
 * When executed, this command checks if the robot is in a valid state to perform the movement.
 * It updates the robot's position in the world and generates appropriate responses for the robot and GUI.
 */
public class ForwardCommand extends Command {

    /**
     * Executes the forward command for the given target robot.
     * @param target The robot executing the forward command.
     * @return true if the command was executed successfully, false otherwise.
     */
    
    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());

        target.worldData.giveCurrentRobotInfo(target);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = target.getRobotState();

        // Check if the robot is repairing or reloading
        if (target.getStatus() == "REPAIR") {
            // repair
            data.clear();
            data.put("message", "Movement not allowed whilst repairing robot");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

            target.previouPosition = target.position;
            data.clear();
            data.put("message", "FRONT");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        if (target.getStatus() == "RELOAD") {
            // reload
            data.clear();
            // Movement not allowed while reloading
            data.put("message", "Movement not allowed whilst repairing robot");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            data.clear();
            data.put("message", "FRONT");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        // Attempt to update the robot's position
        IWorld.UpdateResponse responseGiven = target.worldData.updatePosition(nrSteps);

        // Handle different update responses
        if (responseGiven == UpdateResponse.FAILED_OBSTRUCTED) {
            // Obstructed by an obstacle
            data.clear();

            data.put("message", "Obstructed - There is an obstacle in the way");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            data.clear();
            data.put("message", "FRONT");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_DEAD) {
            // Robot fell into a bottomless pit
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
            // Obstructed by another robot
            data.clear();
            data.put("message", "Obstructed - There is a robot in the way");
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            data.clear();
            data.put("message", "FRONT");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            // Movement attempted outside the world boundaries
            data.clear();
            data.put("message", "Obstructed - Trying to move outside world");
            data.put("position",  new int[] {target.getPosition().getX(), target.getPosition().getY()});
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            target.previouPosition = target.position;
            data.clear();
            data.put("message", "FRONT");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

            return true;
        }

        // If the update is successful, generate responses
        if (target.updatePosition(nrSteps, "front")){
            data.clear();
            data.put("message", "Done");
            state.clear();
            state = target.getRobotState();
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

            data.clear();
            data.put("message", "FRONT");
            state.clear();
            state = target.getGUIRobotState();
            target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
        }
        return true;
    }

    /**
     * Constructs a ForwardCommand object with the specified argument.
     * @param argument The number of steps to move forward.
     */
    public ForwardCommand(String argument) {
        super("forward", argument);
    }
}

