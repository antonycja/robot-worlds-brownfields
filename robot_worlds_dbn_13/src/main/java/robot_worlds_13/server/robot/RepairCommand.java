package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class  RepairCommand extends Command {

    //githe function that makes repair method

    // will restore the robots default shields

    public RepairCommand(String name) {
        super(name);
    }


    @Override
    public boolean execute(Robot target) {
        if (target == null) {
            // Handle the null case, e.g., return false or throw an exception
            return false;
            }

            target.worldData.giveCurrentRobotInfo(target);

            // do repair
            int repairTime = target.worldData.shieldRepairTime;
            target.repairShields(repairTime);

            Map<String, Object> data = new HashMap<>();
            data.put("message", "Done");
            Map<String, Object> state = target.getRobotState();
            target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
            return true;
        }

}
