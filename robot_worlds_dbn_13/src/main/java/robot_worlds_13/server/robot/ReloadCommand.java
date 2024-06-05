package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class ReloadCommand extends Command{
    public ReloadCommand(Robot robot) {
        super("reload"); // Call to Command constructor
    }

    public ReloadCommand() {
        super("reload");
    }

    @Override
    public boolean execute(Robot target) {
        // System.out.println("Reloading...");
        // simulate reload time
        
        int reloadTime = target.getReloadTime();

        target.reload(reloadTime);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Done");
        Map<String, Object> state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));
        
        return true;
    }
}