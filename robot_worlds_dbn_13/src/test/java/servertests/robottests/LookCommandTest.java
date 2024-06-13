package servertests.robottests;//package robot_worlds_13.server.robot;
//
//import org.junit.jupiter.api.Test;
//import robot_worlds_13.server.robot.world.AbstractWorld;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class LookCommandTest {
//    @Test
//    public void testExecute() {
//        // Create a mock Robot object
//        Robot robot = new Robot("TestRobot", new World(10, 10), new Position(0, 0), new HashMap<>());
//
//        // Create a mock World object and set it for the robot
//        AbstractWorld world = new World(10, 10);
//
//
//        // Create a mock list of obstructions
//        ArrayList<Object> obstructions = new ArrayList<>();
//        obstructions.add(new Obstruction());
//
//        // Stub the lookAround method of World to return the mock list of obstructions
//        world.setLookAroundResult(obstructions);
//
//        // Create a mock response
//        Map<String, Object> expectedData = new HashMap<>();
//        expectedData.put("objects", obstructions);
//        Map<String, Object> expectedState = robot.getRobotState();
//        Response expectedResponse = ServerProtocol.buildResponse("OK", expectedData, expectedState);
//
//        // Create a LookCommand object
//        LookCommand lookCommand = new LookCommand();
//
//        // Execute the LookCommand
//        boolean result = lookCommand.execute(robot);
//
//        // Check if the execution was successful
//        assertTrue(result);
//
//        // Check if the response to the robot is set correctly
//        assertEquals(expectedResponse, robot.getResponseToRobot());
//
//        // Check if the GUI response to the robot is set correctly
//        Map<String, Object> expectedGUIData = new HashMap<>();
//        expectedGUIData.put("message", "NONE");
//        Map<String, Object> expectedGUIState = robot.getGUIRobotState();
//        Response expectedGUIResponse = ServerProtocol.buildResponse("GUI", expectedGUIData, expectedGUIState);
//        assertEquals(expectedGUIResponse, robot.getGUIResponseToRobot());
//    }
//
//
//
//
//
//
//
////    class lookCommandTest {
////        private Position Position;
////        private AbstractWorld World;
////
////
////        @Test
////        public void testLookCommandWithInvalidDirection() {
////            Robot robot = new Robot("TestRobot", World, new Position(0, 0), new HashMap<>());
////            LookCommand lookCommand = new LookCommand();
////            String result = lookCommand.execute(robot, "invalid");
////            assertEquals("Invalid direction", result);
////        }
////
////        @Test
////        public void testLookCommandWithNullDirection() {
////            Robot robot = new Robot(0, 0, Direction.NORTH, new position(10, 10));
////            LookCommand lookCommand = new LookCommand();
////            String result = lookCommand.execute(robot, null);
////            assertEquals("Invalid direction", result);
////        }
////
////        @Test
////        public void testLookCommandWithEmptyDirection() {
////            Robot robot = new Robot(0, 0, Direction.NORTH, new World(10, 10));
////            LookCommand lookCommand = new LookCommand();
////            String result = lookCommand.execute(robot, "");
////            assertEquals("Invalid direction", result);
////        }
////
////        @Test
////        public void testLookCommandAtWorldEdge() {
////            Robot robot = new Robot(0, 0, Direction.NORTH, new World(10, 10));
////            LookCommand lookCommand = new LookCommand();
////            String result = lookCommand.execute(robot, "north");
////            assertTrue(result.contains("Edge of the world"));
////        }
////
////        @Test
////        public void testLookCommandWithObstacle() {
////            World world = new World(10, 10);
////            world.addObstacle(new Obstacle(1, 0));
////            Robot robot = new Robot(0, 0, Direction.EAST, world);
////            LookCommand lookCommand = new LookCommand();
////            String result = lookCommand.execute(robot, "east");
////            assertTrue(result.contains("Obstacle"));
////        }
////    }
//
//}

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import robot_worlds_13.server.robot.Robot;

import java.util.ArrayList;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LookCommandTest {

    @Mock
    private Robot robot;

    @Mock
    private WorldData worldData;

//    @Test
//    public void testConstructor() {
//        LookCommand lookCommand = new LookCommand();
//        assertEquals("look", lookCommand.getName());
//    }
//
//    @Test
//    public void testExecute() {
//        // Arrange
//        when(robot.worldData).thenReturn(worldData);
//        when(worldData.lookAround()).thenReturn(new ArrayList<>());
//        when(robot.getRobotState()).thenReturn(new HashMap<>());
//        when(robot.getGUIRobotState()).thenReturn(new HashMap<>());
//
//        LookCommand lookCommand = new LookCommand();
//
//        // Act
//        boolean result = lookCommand.execute(robot);
//
//        // Assert
//        assertTrue(result);
//        verify(robot, times(1)).worldData();
//        verify(worldData, times(1)).lookAround();
//        verify(robot, times(1)).setResponseToRobot(any());
//        verify(robot, times(1)).setGUIResponseToRobot(any());
//    }
}

