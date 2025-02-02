package robot_worlds_13.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import robot_worlds_13.server.robot.*;
import robot_worlds_13.server.robot.world.AbstractWorld;
import java.net.ServerSocket;
import java.util.List;

import static robot_worlds_13.server.configuration.ServerConfiguration.showAllObstacles;

/**
 * This class listens for terminal commands and executes them accordingly.
 */
public class TerminalListener implements Runnable {

    private final ServerSocket server;
    private final Server serverObject;
    private final AbstractWorld world;

    /**
     * Constructs a TerminalListener object with the given parameters.
     * @param serverSocket The server socket.
     * @param serverObjectGiven The server object.
     * @param worldGiven The world object.
     */
    public TerminalListener(ServerSocket serverSocket, Server serverObjectGiven, AbstractWorld worldGiven) {
        this.server = serverSocket;
        this.serverObject = serverObjectGiven;
        this.world = worldGiven;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            while ((input = reader.readLine()) != null) {
                if ("quit".equalsIgnoreCase(input)) {
                    System.out.println("\nReceived quit command. Closing all clients and the server...");
                    closeAllClients();
                    closeServer();
                    System.exit(0);
                } 
                
                else if ("dump".equalsIgnoreCase(input)) {
                    System.out.println("\nDump command received... ");
                    
                    getRobotsInWorld();
                    new Dump(world);

                }
                else if ("robots".equalsIgnoreCase(input)) {
                    System.out.println("\nRobots command received...");
                    
                    getRobotsInWorld();
                }
                else if (input.toLowerCase().contains("save")) {
                    SaveCommand save;
                    System.out.println("\nSaving the world...");
                    String[] newInput = input.split(" ");
                    if (newInput.length == 2){
                        save = new SaveCommand(newInput[1]);
                    } else {
                        save = new SaveCommand();
                    }
                    save.saveWorld(world);
                }
                else if (input.toLowerCase().contains("restore")) {
                    RestoreCommand restore;
                    System.out.println("\nRestoring world...");
                    String[] newInput = input.split(" ");
                    if (newInput.length == 2){
                        restore = new RestoreCommand(newInput[1]);
                    } else {
                        restore = new RestoreCommand();
                    }
                    restore.restoreWorld(world);

                }
                else if (input.toLowerCase().contains("delete")) {
                    DeleteCommand delete;
                    System.out.println("\nDeleting world...");
                    String[] newInput = input.split(" ");
                    if (newInput.length == 2){
                        delete = new DeleteCommand(newInput[1]);
                    } else {
                        delete = new DeleteCommand();
                    }
                    delete.deleteWorld(world);

                }
                else if (input.toLowerCase().contains("worlds")) {
                    WorldsCommand worldsCommand = new WorldsCommand();
                    worldsCommand.showWorlds();
                }
             else {
                    System.out.println("\nInvalid terminal command received: " +"'" + input + "'");
                    System.out.println("Hint use: 'robots', 'worlds', 'quit', 'dump', 'save <world name>'or 'restore <world name>'");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Closes all clients connected to the server.
     */
    private void closeAllClients() {
        List<ClientHandler> clients = serverObject.clients;
        for (ClientHandler client : clients) {
                client.sendMessage("Server shutting down...");
                try {
                    client.clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        clients.clear();
        System.out.println("All clients closed...");
    }

    /**
     * Closes the server.
     */
    private void closeServer() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints information about the robots in the world.
     */
    private void getRobotsInWorld () {
        if (serverObject.nameRobotMap.isEmpty()){
            System.out.println("No robots on world yet");
            return;
        }

        System.out.println("Robots: ");
        for (String name: serverObject.nameRobotMap.keySet()) {
            ArrayList<Object> currentState = serverObject.nameRobotMap.get(name);
            Robot thisRobot = (Robot) currentState.get(2);
            
            System.out.println("    Robot name: " + name + "\n" + 
                                "    Position: " + currentState.get(0).toString() + "\n" + 
                                "    Direction: " + currentState.get(1).toString() + "\n" +
                                "    Shields: " + thisRobot.shieldsAvailable() + "\n" +
                                "    Current shots: " + thisRobot.ammoAvailable() + "\n" + 
                                "    Current status: " + thisRobot.getStatus() + "\n"
            );
        }
    }

    /**
     * Prints information about the obstacles in the world.
     */

    private void getObstaclesInWorld () {
        if (world.getObstaclesAsString().isEmpty()) {
            System.out.println("There are no obstacles in the world");
            return;
        }
        System.out.println("All Obstacles:\n\t" + showAllObstacles(world));
    }
}
