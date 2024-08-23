package AcceptanceTests;

import database.SqlCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robot_worlds_13.server.robot.SaveCommand;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class SaveCommandTest {

    private AbstractWorld worldMock;
    private SaveCommand saveCommand;

    @BeforeEach
    public void setUp() {
        worldMock = mock(AbstractWorld.class);
        saveCommand = new SaveCommand("TestWorld");
    }

    @Test
    public void testSaveWorldWithValidName() {
            // Setup
        when(worldMock.width).thenReturn(10);
        when(worldMock.height).thenReturn(10);
        when(worldMock.getObstacles()).thenReturn(List.of(new Obstacle(1, 1, 2, "obstacle")));
        when(worldMock.getLakes()).thenReturn(List.of(new Obstacle(2, 2, 3, "lake")));
        when(worldMock.getBottomLessPits()).thenReturn(List.of(new Obstacle(3, 3, 4, "pit")));

            // Perform test
        String result = saveCommand.saveWorld(worldMock);

            // Validate
        assertTrue(result.contains("World saved successfully with the name: TestWorld"));
    }

    @Test
    public void testSaveWorldWithEmptyName() {
            // Setup
        SaveCommand saveCommandWithEmptyName = new SaveCommand();
        String userInput = "NewWorldName\n";
        InputStream originalSystemIn = System.in;
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        when(worldMock.width).thenReturn(10);
        when(worldMock.height).thenReturn(10);

            // Perform test
        String result = saveCommandWithEmptyName.saveWorld(worldMock);

            // Restore original System.in
        System.setIn(originalSystemIn);

            // Validate
        assertTrue(result.contains("World saved successfully with the name: NewWorldName"));
    }

    @Test
    public void testSaveWorldWhenNameExists() {
            // Setup
        when(worldMock.width).thenReturn(10);
        when(worldMock.height).thenReturn(10);
            // Simulate an exception being thrown when inserting the world
        SqlCommands sqlCommandsMock = mock(SqlCommands.class);
        doThrow(new IllegalArgumentException()).when(sqlCommandsMock).insertWorld(anyString(), anyInt(), anyInt());

            // Create a new SaveCommand instance with the same world name to trigger the exception
        saveCommand = new SaveCommand("ExistingWorld");

            // Perform test
        String result = saveCommand.saveWorld(worldMock);

            // Validate
        assertTrue(result.contains("World with name 'ExistingWorld' already exists"));
    }
}

