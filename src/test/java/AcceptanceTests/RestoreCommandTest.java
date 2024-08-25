package AcceptanceTests;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import database.SqlCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import robot_worlds_13.server.robot.RestoreCommand;
import robot_worlds_13.server.robot.world.AbstractWorld;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestoreCommandTest {

    private SqlCommands sqlCommandsMock;
    private AbstractWorld worldMock;
    private RestoreCommand restoreCommand;

    @BeforeEach
    public void setUp() {
        sqlCommandsMock = Mockito.mock(SqlCommands.class);
        worldMock = mock(AbstractWorld.class); // Use concrete implementation
        restoreCommand = new RestoreCommand("TestWorld");
    }

//    @Test
//    public void testRestoreWorldSuccessfully() {
//        // Setup
//        Map<String, Object> worldData = new HashMap<>();
//        worldData.put("name", "TestWorld");
//        worldData.put("width", 10);
//        worldData.put("height", 10);
//
//        List<Map<String, Object>> obstacles = new ArrayList<>();
//        // Create a simple obstacle data map
//        Map<String, Object> obstacleData = new HashMap<>();
//        obstacleData.put("size", 2);
//        obstacleData.put("x_position", 1);
//        obstacleData.put("y_position", 1);
//        obstacleData.put("type", "obstacle");
//        obstacles.add(obstacleData);
//
//        worldData.put("obstacles", obstacles);
//
//        Mockito.when(sqlCommandsMock.restoreWorldData(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(worldData);
//
//        // Redirect System.out to capture output
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        // Perform test
//        restoreCommand.restoreWorld(worldMock);
//
//        // Validate the output
//        String output = outContent.toString();
//        assertTrue(output.contains("World restored successfully with the name: TestWorld"));
//
//        // Validate world state
//        assertEquals(1, (worldMock).getObstacles().size());
//    }

//    @Test
//    public void testRestoreWorldWhenWorldDoesNotExist() {
//        // Setup
//        Map<String, Object> worldData = new HashMap<>();
//        worldData.put("name", "DifferentWorld");
//
//        Mockito.when(sqlCommandsMock.restoreWorldData(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(worldData);
//
//        // Redirect System.out to capture output
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        // Perform test
//        restoreCommand.restoreWorld(worldMock);
//
//        // Validate the output
//        String output = outContent.toString();
//        assertTrue(output.contains("The world 'TestWorld' does not exist!!"));
//        assertTrue(output.contains("Aborting Restore..."));
//        assertTrue(output.contains("Successfully Reverted back."));
//
//        // Restore original System.out
//        System.setOut(System.out);
//    }
}
