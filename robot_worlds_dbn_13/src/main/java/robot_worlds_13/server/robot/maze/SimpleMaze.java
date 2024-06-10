package robot_worlds_13.server.robot.maze;

import java.util.ArrayList;
import java.util.List;

import robot_worlds_13.server.*;
import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.world.*;

public class SimpleMaze extends AbstractMaze {

    private List<Obstacle> obstacles;

    public SimpleMaze (){
        this.obstacles = new ArrayList<>();
        for (int number=0; number!=10; number++)
        obstacles.add(new SquareObstacle(0,0));
        // obstacles.add(new SquareObstacle(20, 20));
        // obstacles.add(new SquareObstacle(40, 40));
        // obstacles.add(new SquareObstacle(-20, 40));
        // obstacles.add(new SquareObstacle(-20, -20));
        // obstacles.add(new SquareObstacle(-20, -40));
    }

    @Override
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        return false;
    }

    

    
    
}
