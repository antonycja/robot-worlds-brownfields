package servertests.robottests;

import org.junit.jupiter.api.Test;

import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.robot.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleMazeTest {

    @Test
    void testSimpleMazeHasOne() {
        Maze maze = new SimpleMaze();
        assertEquals(5, maze.getObstacles().size());
    }

    @Test
    void testSimpleMazeGetBottomLessPits() {
        Maze maze = new SimpleMaze();
        assertEquals(3, maze.getBottomLessPits().size());
    }

    @Test
    void testSimpleMazeGetLakes() {
        Maze maze = new SimpleMaze();
        assertEquals(2, maze.getLakes().size());
    }

    @Test
    void testSimpleMazeBlocksPath() {
        Maze maze = new SimpleMaze();
        Position a = new Position(0, 0);
        Position b = new Position(1, 1);
        assertFalse(maze.blocksPath(a, b)); // This test will fail until you implement the blocksPath method
    }
}