package server.maze;

import java.util.ArrayList;
import java.util.List;

import server.*;
import server.world.*;

public class SimpleMaze extends AbstractMaze {

    private List<Obstacle> obstacles;

    public SimpleMaze (){
        this.obstacles = new ArrayList<>();
        obstacles.add(new SquareObstacle(1,1));
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
