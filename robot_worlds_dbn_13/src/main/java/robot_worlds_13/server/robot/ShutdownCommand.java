package robot_worlds_13.server.robot;

public class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("off");
    }

    @Override
    public boolean execute(Robot target) {
        target.setResponseToRobot("Shutting down...");
        return false;
    }
}
