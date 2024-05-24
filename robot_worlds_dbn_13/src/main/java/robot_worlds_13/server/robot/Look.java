package robot_worlds_13.server.robot;

public class Look extends Command{
    
    @Override
    public boolean execute(Robot target) {
        
        // look using abstract world
        target.worldData.giveCurrentRobotInfo(target);
        

        // return list with details

        // set status with look
        target.setStatus("Turned left.");
        return true;
    }

    public Look() {
        super("look");
    }
}
