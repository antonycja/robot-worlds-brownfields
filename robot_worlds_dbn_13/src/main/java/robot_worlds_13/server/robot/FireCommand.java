package robot_worlds_13.server.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class FireCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
    
    
    // decrease the ammo of the current robot
    target.decreaseAmmo();

    // bullet distance
    int robotBulletDistance = target.bullet_distance; // change later to private varable

    // this check for a respone of either hit or miss
    target.worldData.giveCurrentRobotInfo(target);

    // 
    if (target.worldData.isHit(10)){
        // if its a hit then decrese the shield of the affected robot
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hit");
        Map<String, Object> state = new HashMap<>();
        state.put("position", new int[] {target.getPosition().getX(), target.getPosition().getY()});
        state.put("direction", target.getCurrentDirection());
        target.setStatus(ServerProtocol.buildResponse("OK", data, state));
    } else {
        // if its a miss then nothing
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Miss");
        Map<String, Object> state = new HashMap<>();
        state.put("position", new int[] {target.getPosition().getX(), target.getPosition().getY()});
        state.put("direction", target.getCurrentDirection());
        target.setStatus(ServerProtocol.buildResponse("OK", data, state));
    }

    return true;
    }

    public FireCommand() {
        super("fire");
    }
}
