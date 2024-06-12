package servertests.robottests;

import org.junit.jupiter.api.Test;

import robot_worlds_13.server.*;
import robot_worlds_13.server.robot.maze.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleMazeTest {

    @Test
    void testSimpleMazeHasOne() {
        Maze maze = new SimpleMaze();
        assertEquals(2, maze.getObstacles().size());
    }

    @Test
    void testSimpleMazeInCenter() {
        Maze maze = new SimpleMaze();
        assertEquals(1, maze.getObstacles().get(0).getBottomLeftX());
        assertEquals(1, maze.getObstacles().get(0).getBottomLeftY());
    }
}