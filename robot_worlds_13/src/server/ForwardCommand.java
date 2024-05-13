package server;

import server.world.IWorld;
import server.world.IWorld.UpdateResponse;

public class ForwardCommand extends Command {
    
    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());

        // System.out.println("in forward");
        // where towards is either "front" or "back"

        IWorld.UpdateResponse responseGiven = target.worldData.updatePosition(nrSteps);
        
        if (responseGiven == UpdateResponse.FAILED_OBSTRUCTED) {
            // obstacle
            target.setStatus("Sorry, there is an obstacle in the way.");
            return true;
        }

        else if (responseGiven == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            // outside world
            target.setStatus("Sorry, I cannot go outside my safe zone.");
            return true;
        }

        if (target.updatePosition(nrSteps, "front")){
            target.setStatus("Moved forward by "+nrSteps+" steps.");
        }
        else {
            System.out.println("Error");
        }
        return true;
    }

    public ForwardCommand(String argument) {
        super("forward", argument);
    }
}

