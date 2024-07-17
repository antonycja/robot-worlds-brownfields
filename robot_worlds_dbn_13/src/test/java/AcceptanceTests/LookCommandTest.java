package AcceptanceTests;


import AcceptanceTests.RobotWorldClient.RobotWorldClient;
import AcceptanceTests.RobotWorldClient.RobotWorldJsonClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import robot_worlds_13.server.robot.LookCommand;
import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.robot.world.AbstractWorld;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.when;

public class LookCommandTest {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }
    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }
    @Mock
    private Robot mockRobot;
    @Mock
    private AbstractWorld mockWorldData;
    private LookCommand lookCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lookCommand = new LookCommand();

        try {

            Field worldDataField = Robot.class.getDeclaredField("worldData");
            worldDataField.setAccessible(true);
            worldDataField.set(mockRobot, mockWorldData);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up mock world data", e);
        }
    }

    @Test
    public void testExecute_Success() {

        ArrayList<Object> mockObstructions = new ArrayList<>();
        when(mockRobot.worldData.lookAround()).thenReturn(mockObstructions);
        Map<String, Object> mockState = new HashMap<>();
        when(mockRobot.getRobotState()).thenReturn(mockState);
        Map<String, Object> mockGUISate = new HashMap<>();
        when(mockRobot.getGUIRobotState()).thenReturn(mockGUISate);


        boolean result = lookCommand.execute(mockRobot);


    }
}
