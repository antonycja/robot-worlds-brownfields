package server;
public class RightCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        
        target.worldData.updateDirection(true);
        target.updateDirection("right");
        target.setStatus("Turned right.");
        return true;
    }

    public RightCommand() {
        super("right");
    }
}
