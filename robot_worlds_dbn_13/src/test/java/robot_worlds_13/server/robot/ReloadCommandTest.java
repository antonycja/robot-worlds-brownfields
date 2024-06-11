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



}
