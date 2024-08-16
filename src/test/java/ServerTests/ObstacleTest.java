package ServerTests;

import org.junit.jupiter.api.Test;
import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.world.Obstacle;
import robot_worlds_13.server.robot.world.SquareObstacle;

import static org.junit.jupiter.api.Assertions.*;

public class ObstacleTest {
    private int size = 40;
    @Test
    void testObstacleDimensions() {
        Obstacle obstacle = new SquareObstacle(1,1, size);
        assertEquals(1, obstacle.getBottomLeftX());
        assertEquals(1, obstacle.getBottomLeftY());
        assertEquals(40, obstacle.getSize());
    }

    @Test
    void testBlockPosition(){
        Obstacle obstacle = new SquareObstacle(1,1, size);
        assertTrue(obstacle.blocksPosition(new Position(1,1)));
        assertTrue(obstacle.blocksPosition(new Position(5,1)));
        assertTrue(obstacle.blocksPosition(new Position(1,5)));
        assertFalse(obstacle.blocksPosition(new Position(0,5)));
        assertTrue(obstacle.blocksPosition(new Position(6,5)));
    }

    @Test
    void testBlockPath(){
        Obstacle obstacle = new SquareObstacle(1,1, size);
        assertTrue(obstacle.blocksPath(new Position(1,0), new Position(1,50)));
        assertTrue(obstacle.blocksPath(new Position(2,-10), new Position(2, 100)));
        assertTrue(obstacle.blocksPath(new Position(-10,5), new Position(20, 5)));
        assertFalse(obstacle.blocksPath(new Position(0,1), new Position(0, 100)));
        assertTrue(obstacle.blocksPath(new Position(1,6), new Position(1, 100)));

    }

}
