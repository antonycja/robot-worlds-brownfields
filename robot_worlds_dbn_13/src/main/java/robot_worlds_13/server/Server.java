package robot_worlds_13.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.robot.maze.SimpleMaze;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.TextWorld;

public class Server {
    List<ClientHandler> clients = new ArrayList<>();

    // keeping track of robots
    public ArrayList<String> robotNames = new ArrayList<>();
    public ArrayList<Object> states = new ArrayList<>();
    public HashMap<String, ArrayList<Object>> nameRobotMap = new HashMap<>();

    // configured data
    

    public static void main(String[] args) throws Exception {
        
        // this server
        Server serverObject = new Server();
        
        //
        AbstractWorld world = new TextWorld(new SimpleMaze(), serverObject);

        
        

        try (ServerSocket serverSocket = new ServerSocket(2201)) {
            System.out.println("Server started. Listening for incoming connections...");

            // thread for listening to terminal commands
            Thread terminalListenerThread = new Thread(new TerminalListener(serverSocket, serverObject));
            terminalListenerThread.start();;

            while (true) {
                // accepting clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("Incoming connection accepted");
                
                // mocking starting position of robot
                Position startPosition =  new Position(0, 0);
                if (!serverObject.robotNames.isEmpty()) {
                    startPosition =  new Position(0, 10);
                }

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, world, startPosition);
                serverObject.clients.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        }
        catch (Exception e) {
            System.out.println("Server closed");
        }
    }
}
