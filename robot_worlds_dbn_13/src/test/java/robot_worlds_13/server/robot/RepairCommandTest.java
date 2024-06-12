package robot_worlds_13.server.robot;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import robot_worlds_13.server.robot.maze.SimpleMaze;
import robot_worlds_13.server.robot.world.AbstractWorld;

import java.util.HashMap;
import java.util.Map;


/**
 * Unit test class for testing the RepairCommand class.
 */
public class RepairCommandTest {

    /**
     * Tests the execute method of RepairCommand with a valid robot.
     * Verifies that the robot's shields are repaired to the maximum value.
     */
    @Test
    public void testExecute() {

        //Create a mock Robot object with configuration
       AbstractWorld worldObject = new AbstractWorld(new SimpleMaze());



        Map<String, Integer> robotConfig = new HashMap<>();
        //Set shields to non-maximum value for testing
        robotConfig.put("shields", 3);

        // Create a mock Robot object
        Robot robot = new Robot("TestRobot", worldObject, new Position(0,0),  robotConfig);


        RepairCommand repairCommand = new RepairCommand("repair");

        boolean result = repairCommand.execute(robot);
        //Check if the execution was successful
        assertTrue(result);

        //Check if the robot's shields are repaired to the maximum value
        assertEquals(5, robot.maxShields);
    }

    /**
     * Tests the execute method of RepairCommand when the robot is null.
     * Verifies that the command execution returns false.
     */
    @Test
    public void testExecute_NullRobot() {
        RepairCommand repairCommand = new RepairCommand("repair");

        boolean result = repairCommand.execute(null);

        assertFalse(result);
    }


}