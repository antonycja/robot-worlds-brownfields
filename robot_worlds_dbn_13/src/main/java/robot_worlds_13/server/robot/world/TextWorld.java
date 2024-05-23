package robot_worlds_13.server.robot.world;

import java.util.List;

import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.Server;
import robot_worlds_13.server.robot.Position;

public class TextWorld extends AbstractWorld {
    
    public TextWorld(Maze maze) {
        super(maze);
    }

    public TextWorld(Maze simpleMaze, Server serverObject) {
        super(simpleMaze, serverObject);
    }
    
}
