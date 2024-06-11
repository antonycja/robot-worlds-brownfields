package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.ServerProtocol;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(Robot target) {
        String message = "I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move back by specified number of steps, e.g. 'BACK 10'\n" +
                "LEFT - turn left by 90 degrees\n"+
                "RIGHT - turn right by 90 degrees\n" +
                "OFF  - Shut down robot";
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        Map<String, Object> state = target.getRobotState();
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));

        return true;
    }
}
