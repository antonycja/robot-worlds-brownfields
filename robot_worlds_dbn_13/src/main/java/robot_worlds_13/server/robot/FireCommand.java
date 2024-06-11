package robot_worlds_13.server.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.Server;
import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.IWorld;

public class FireCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
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

    public FireCommand() {
        super("fire");
    }
}
