package robot_worlds_13.server.robot;

import robot_worlds_13.server.robot.world.IWorld.UpdateResponse;

public class BackCommand extends robot_worlds_13.server.robot.Command {

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());

        // -nrsteps since the update in abstract world has no front vs back
        if (target.worldData.updatePosition(-nrSteps) == UpdateResponse.FAILED_OBSTRUCTED) {
            // obstacle
            target.setStatus("Sorry, there is an obstacle in the way.");
            return true;
        }

        else if (target.worldData.updatePosition(-nrSteps) == UpdateResponse.FAILED_OUTSIDE_WORLD) {
            // outside world
            target.setStatus("Sorry, I cannot go outside my safe zone.");
            return true;
        }

        // where towards is either "front" or "back"
        if (target.updatePosition(nrSteps, "back")){
            target.setStatus("Moved back by "+nrSteps+" steps.");
        }
        return true;
    }

    public BackCommand (String argument) {
        super("back", argument);
    }
}
