package ServerTests;

import org.junit.jupiter.api.Test;

import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.robot.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleMazeTest {

    @Test
    void testSimpleMazeHasOne() {
        SimpleMaze maze = new SimpleMaze();
        maze.setMinCoordinate(500 / 3);
        maze.setMaxCoordinate(500 / 3);
        maze.generateRandomObstacles();

        assertEquals(5, maze.getObstacles().size());
    }

    @Test
    void testSimpleMazeGetBottomLessPits() {
        SimpleMaze maze = new SimpleMaze();
        maze.setMinCoordinate(500 / 3);
        maze.setMaxCoordinate(500 / 3);
        maze.generateRandomObstacles();

        assertEquals(3, maze.getBottomLessPits().size());
    }

    @Test
    void testSimpleMazeGetLakes() {
        SimpleMaze maze = new SimpleMaze();
        maze.setMinCoordinate(500 / 3);
        maze.setMaxCoordinate(500 / 3);
        maze.generateRandomObstacles();

        assertEquals(2, maze.getLakes().size());
    }

    @Test
    void testSimpleMazeBlocksPath() {
        SimpleMaze maze = new SimpleMaze();
        maze.setMinCoordinate(500 / 3);
        maze.setMaxCoordinate(500 / 3);
        maze.generateRandomObstacles();

        Position a = new Position(0, 0);
        Position b = new Position(1, 1);
        assertFalse(maze.blocksPath(a, b)); // This test will fail until you implement the blocksPath method
    }
}