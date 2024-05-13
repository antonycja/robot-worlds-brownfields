package server;

public class LeftCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        
        target.worldData.updateDirection(false);
        target.updateDirection("left");
        target.setStatus("Turned left.");
        return true;
    }

    public LeftCommand() {
        super("left");
    }
}
