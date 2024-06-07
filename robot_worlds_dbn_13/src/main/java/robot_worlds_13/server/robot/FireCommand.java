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
        data.put("message", "Hit");
        data.put("distance", 5);
        data.put("robot", affectedRobot.getName());
        data.put("state", affectedRobot.getRobotState());
        state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
    } else {
        // if its a miss then nothing
        data.put("message", "Miss");
        state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
    }

    int startX = target.position.getX();
    int startY = target.position.getY();

    // int endX = startX;
    // int endY = startY;
    // if (IWorld.Direction.NORTH.equals(target.getCurrentDirection())) {
    //     endY = startY + robotBulletDistance;
    // } else if (IWorld.Direction.SOUTH.equals(target.getCurrentDirection())) {
    //     endY = startY - robotBulletDistance;
    // } else if (IWorld.Direction.WEST.equals(target.getCurrentDirection())) {
    //     endX = startX - robotBulletDistance;
    // } else if (IWorld.Direction.EAST.equals(target.getCurrentDirection())) {
    //     endX = startX + robotBulletDistance;
    // }

    data.clear();
    data.put("message", "FIRE");
    state.clear();
    state.put("previousPosition", new int[] {startX, startY});
    state.put("position", new int[] {affectedRobot.getPosition().getX(), affectedRobot.getPosition().getX()});
    Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));

    return true;
    }

    public FireCommand() {
        super("fire");
    }
}
