package servertests.robottests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.FireCommand;
import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.robot.world.IWorld;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

public class FireCommandTest {

    private Robot targetRobot;
    private Robot affectedRobot;
    private FireCommand fireCommand;
    private IWorld worldData;

    @BeforeEach
    public void setUp() {
        targetRobot = mock(Robot.class);
        affectedRobot = mock(Robot.class);
        fireCommand = new FireCommand();
        worldData = mock(IWorld.class);

        when(targetRobot.getWorldData()).thenReturn(worldData);
        when(targetRobot.getBulletDistance()).thenReturn(5);
        when(targetRobot.getPosition()).thenReturn(new Position(0, 0));
        when(targetRobot.getCurrentDirection()).thenReturn(IWorld.Direction.NORTH);
    }

    @Test
    public void testFireWithNoAmmo() {
        when(targetRobot.ammoAvailable()).thenReturn(0);
        when(targetRobot.getRobotState()).thenReturn(Map.of("stateKey", "stateValue"));

        boolean result = fireCommand.execute(targetRobot);

        assertTrue(result);
        verify(targetRobot).setResponseToRobot(argThat(response -> 
            response.equals(ServerProtocol.buildResponse("ERROR", Map.of("message", "No shots available"), Map.of("stateKey", "stateValue")))
        ));
    }
}