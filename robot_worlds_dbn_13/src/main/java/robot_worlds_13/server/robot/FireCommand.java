package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.Server;
import robot_worlds_13.server.ServerProtocol;

/**
 * Represents a command to fire a shot from a robot.
 * When executed, this command checks if the robot has sufficient ammo,
 * then calculates the bullet distance based on the robot's attributes.
 * It determines whether the shot hits another robot or not and updates their states accordingly.
 * Additionally, it generates responses for the robot and GUI based on the outcome of the shot.
 */
public class FireCommand extends Command {

    /**
     * Executes the fire command for the given target robot.
     * @param target The robot executing the fire command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {
        // Retrieve current robot information from the world data
    target.worldData.giveCurrentRobotInfo(target);
    Map<String, Object> data = new HashMap<>();
    Map<String, Object> state = target.getRobotState();

    // decrease the ammo of the current robot
    if (target.ammoAvailable() <= 0) {
        data.put("message", "No shots available");
        target.setResponseToRobot(ServerProtocol.buildResponse("ERROR", data, state));
        return true;
    }
    target.decreaseAmmo();


    // bullet distance
    int robotBulletDistance = target.getBulletDistance();

    // this check for a respone of either hit or miss
    Robot affectedRobot = target.worldData.isHit(robotBulletDistance);
    if (affectedRobot.getName() != "NonValid"){
        // if its a hit then decrese the shield of the affected robot
        int stepsAway = target.worldData.getStepsAway(target.getCurrentPosition(), affectedRobot.getCurrentPosition());
        data.put("message", "Hit");
        data.put("distance", stepsAway);
        data.put("robot", affectedRobot.getName());
        if (affectedRobot.shields < 0) {
            affectedRobot.setDeadStatus();
        }
        data.put("state", affectedRobot.getRobotState());
        state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
    } else {
        // if its a miss then nothing
        data.put("message", "Miss");
        state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
    }

    data.clear();
    data.put("message", "NONE");
    state.clear();
    state = target.getGUIRobotState();
    target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

    // Broadcast the fire action to the GUI
    data.clear();
    data.put("message", "FIRE");
    state.clear();
    state.put("name", "fireRobot");
    state.put("previousPosition", new int[] {target.getPosition().getX(), target.getPosition().getY()});
    state.put("position", new int[] {affectedRobot.getPosition().getX(), affectedRobot.getPosition().getY()});
    state.put("direction", target.getCurrentDirection());
    Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));
    

    return true;
    }

    /**
     * Constructs a FireCommand object.
     * Initializes the command name as "fire".
     */
    public FireCommand() {
        super("fire");
    }
}
