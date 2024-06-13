package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import robot_worlds_13.server.ServerProtocol;

/**
 * Represents a command to repair a robot's shields.
 * When executed, this command restores the robot's shields to their default state.
 */
public class  RepairCommand extends Command {

    /**
     * Constructs a RepairCommand object with the specified name.
     * @param name The name of the repair command.
     */
    public RepairCommand(String name) {
        super(name);
    }

    /**
     * Executes the repair command for the given target robot.
     * This method repairs the shields of the robot by restoring them to their default state.
     * @param target The robot executing the repair command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Robot target) {
        if (target == null) {
            // Handle the null case, e.g., return false or throw an exception
            return false;
            }

        // Retrieve current robot information from the world data
        target.worldData.giveCurrentRobotInfo(target);

        // Perform repair operation
        int repairTime = target.worldData.shieldRepairTime;
        target.repairShields(repairTime);

        // Construct response data and state
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Done");
        Map<String, Object> state = target.getRobotState();

        // Set response to the robot
        target.setResponseToRobot(ServerProtocol.buildResponse("OK", data, state));

        // Set GUI response
        data.clear();
        data.put("message", "NONE");
        state.clear();
        state = target.getGUIRobotState();
        target.setGUIResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
        return true;

        }

    }

