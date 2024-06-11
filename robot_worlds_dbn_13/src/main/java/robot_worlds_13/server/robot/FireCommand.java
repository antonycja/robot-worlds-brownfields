package robot_worlds_13.server.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.Server;
import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.IWorld;

public class FireCommand extends Command {
    
    @Override
    public boolean execute(Object targetRobot) {
    targetRobot.worldData.giveCurrentRobotInfo(targetRobot);
    Map<String, Object> data = new HashMap<>();
    Map<String, Object> state = targetRobot.getRobotState();

    // decrease the ammo of the current robot
    if (targetRobot.ammoAvailable() <= 0) {
        data.put("message", "No shots available");
        targetRobot.setResponseToRobot(ServerProtocol.buildResponse("ERROR", data, state));
        return true;
    }
    targetRobot.decreaseAmmo();


    // bullet distance
    int robotBulletDistance = targetRobot.getBulletDistance();

    // this check for a respone of either hit or miss
    
    
    Robot affectedRobot = targetRobot.worldData.isHit(robotBulletDistance);
    if (affectedRobot.getName() != "NonValid"){
        // if its a hit then decrese the shield of the affected robot
        data.put("message", "Hit");
        data.put("distance", 5);
        data.put("robot", affectedRobot.getName());
        data.put("state", affectedRobot.getRobotState());
        state = targetRobot.getRobotState();
        targetRobot.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
    } else {
        // if its a miss then nothing
        data.put("message", "Miss");
        state = targetRobot.getRobotState();
        targetRobot.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
    }

    int startX = targetRobot.position.getX();
    int startY = targetRobot.position.getY();

    data.clear();
    data.put("message", "FIRE");
    state.clear();
    state.put("name", "fireRobot");
    state.put("previousPosition", new int[] {startX, startY});
    state.put("position", new int[] {affectedRobot.getPosition().getX(), affectedRobot.getPosition().getX()});
    Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));

    return true;
    }

    public FireCommand() {
        super("fire");
    }
}
