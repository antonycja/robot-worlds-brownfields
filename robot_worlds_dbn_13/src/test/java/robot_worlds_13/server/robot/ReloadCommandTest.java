package robot_worlds_13.server.robot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import robot_worlds_13.server.ServerProtocol;

class ReloadCommandTest {

    @Mock
    private Robot mockRobot;

    private ReloadCommand reloadCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reloadCommand = new ReloadCommand();
    }

    @Test
    public void testExecute_ReloadTimeIsZero() {
        int reloadTime = 0;
        when(mockRobot.getReloadTime()).thenReturn(reloadTime);

        ReloadCommand command = new ReloadCommand();
        boolean result = command.execute(mockRobot);

        verify(mockRobot).reload(reloadTime);

        assertTrue(result);

    }

    @Test
    void testExecute_ReloadTimeIsPositive() {
        int reloadTime = 5;
        when(mockRobot.getReloadTime()).thenReturn(reloadTime);

        boolean result = reloadCommand.execute(mockRobot);

        assertTrue(result);
        verify(mockRobot).reload(reloadTime);
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("message", "Done");
        verify(mockRobot).setResponseToRobot(ServerProtocol.buildResponse("OK", expectedData, mockRobot.getRobotState()));
    }


    @Test
    void testExecute_RobotIsNull() {
        assertThrows(NullPointerException.class, () -> reloadCommand.execute(null));
    }



}
