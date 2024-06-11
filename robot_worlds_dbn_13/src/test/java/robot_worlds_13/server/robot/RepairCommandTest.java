package robot_worlds_13.server.robot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import robot_worlds_13.server.robot.maze.SimpleMaze;
import robot_worlds_13.server.robot.world.AbstractWorld;
import static org.junit.Assert.*;

public class RepairCommandTest {

    @Test
    public void testExecute() {
        Robot robot = new Robot("TestRobot");
        AbstractWorld world = new AbstractWorld(new SimpleMaze());

        robot.maxShields = 5;

        RepairCommand repairCommand = new RepairCommand("repair");

        boolean result = repairCommand.execute(robot);

        assertTrue(result);

        assertEquals(5, robot.maxShields);
    }

    @Test
    public void testExecute_NullRobot() {
        RepairCommand repairCommand = new RepairCommand("repair");

        boolean result = repairCommand.execute(null);

        assertFalse(result);
    }
}