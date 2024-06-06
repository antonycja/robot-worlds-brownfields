package robot_worlds_13.server.robot;

import robot_worlds_13.server.ServerProtocol;

public class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("off");
    }

    @Override
    public boolean execute(Robot target) {
        target.worldData.giveCurrentRobotInfo(target);
        target.setStatus("Shutting down...");
        target.setResponseToRobot(ServerProtocol.buildResponse("Shutting down..."));
        return false;
    }
    
}
