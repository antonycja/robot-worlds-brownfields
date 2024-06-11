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

/**
 * Unit test class for testing the ReloadCommand class.
 */

class ReloadCommandTest {

    @Mock
    private Robot mockRobot;

    private ReloadCommand reloadCommand;

    /**
     * Sets up the test environment before each test case.
     * Initializes mocks and creates an instance of ReloadCommand.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reloadCommand = new ReloadCommand();
    }


    /**
     * Tests the execute method of ReloadCommand when reload time is zero.
     * Verifies that the reload method is called with the correct reload time.
     */
    @Test
    public void testExecute_ReloadTimeIsZero() {
        int reloadTime = 0;
        when(mockRobot.getReloadTime()).thenReturn(reloadTime);

        ReloadCommand command = new ReloadCommand();
        boolean result = command.execute(mockRobot);

        verify(mockRobot).reload(reloadTime);

        assertTrue(result);

    }

    /**
     * Tests the execute method of ReloadCommand when reload time is positive.
     * Verifies that the reload method is called with the correct reload time.
     * Verifies that the response to the robot is set correctly.
     */

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


    /**
     * Tests the execute method of ReloadCommand when the robot is null.
     * Verifies that a NullPointerException is thrown.
     */

    @Test
    void testExecute_RobotIsNull() {
        assertThrows(NullPointerException.class, () -> reloadCommand.execute(null));
    }



}
