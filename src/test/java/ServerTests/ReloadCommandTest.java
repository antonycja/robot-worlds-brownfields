package ServerTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.ReloadCommand;
import robot_worlds_13.server.robot.Robot;

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
     * Tests the execute method of ReloadCommand when the robot is null.
     * Verifies that a NullPointerException is thrown.
     */

    @Test
    void testExecute_RobotIsNull() {
        assertThrows(NullPointerException.class, () -> reloadCommand.execute(null));
    }

    




}
