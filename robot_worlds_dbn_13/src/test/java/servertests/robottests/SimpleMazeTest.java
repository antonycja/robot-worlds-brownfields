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


    // @Test
    // void testSimpleMazeInCenter() {
    //     Maze maze = new SimpleMaze();
    //     assertEquals(200, maze.getObstacles().get(0).getBottomLeftX());
    //     assertEquals(0, maze.getObstacles().get(0).getBottomLeftY());
    // }


//     @Test
//     void testSimpleMazeObstacleWidth() {
//         Maze maze = new SimpleMaze();
//         assertEquals(1, maze.getObstacles().get(0).getWidth());
//     }

//     @Test
//     void testSimpleMazeObstacleHeight() {
//         Maze maze = new SimpleMaze();
//         assertEquals(1, maze.getObstacles().get(0).getHeight());
//     }

//     @Test
//     void testSimpleMazeGetRobotStartPosition() {
//         Maze maze = new SimpleMaze();
//         assertEquals(1, maze.getRobotStartPosition().getX());
//         assertEquals(1, maze.getRobotStartPosition().getY());
//     }

//     @Test
//     void testSimpleMazeGetGoalPosition() {
//         Maze maze = new SimpleMaze();
//         assertEquals(3, maze.getGoalPosition().getX());
//         assertEquals(3, maze.getGoalPosition().getY());
//     }

//     @Test
//     void testSimpleMazeGetMazeSize() {
//         Maze maze = new SimpleMaze();
//         assertEquals(5, maze.getMazeSize());
//     }

//     @Test
//     void testSimpleMazeGetMazeType() {
//         Maze maze = new SimpleMaze();
//         Object MazeType;
//         assertEquals(MazeType.SIMPLE, maze.getMazeType());
//     }

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