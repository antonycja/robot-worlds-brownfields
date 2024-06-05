package robot_worlds_13.server.robot;

import robot_worlds_13.server.ServerProtocol;

public class OrientationCommand extends Command {
    
    public OrientationCommand() {
        super("orientation");
    }

    @Override
    public boolean execute(Robot target) {
        target.worldData.giveCurrentRobotInfo(target);
        target.setResponseToRobot(ServerProtocol.buildResponse("Orientation: " + String.valueOf(target.getCurrentDirection())));
        return true;
    }
}
