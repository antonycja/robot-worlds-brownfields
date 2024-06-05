package robot_worlds_13.server.robot.world;

import java.util.List;
import java.util.Map;

import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.Server;
import robot_worlds_13.server.robot.Position;

public class TextWorld extends AbstractWorld {
    
    public TextWorld(Maze maze) {
        super(maze);
    }

    // public TextWorld(Maze simpleMaze, Server serverObject) {
    //     super(simpleMaze, serverObject);
    // }

    public TextWorld(Maze simpleMaze, Server serverObject, Map<String, Integer> mapConfigurables) {
        super(simpleMaze, serverObject, mapConfigurables);
    }
    
}
