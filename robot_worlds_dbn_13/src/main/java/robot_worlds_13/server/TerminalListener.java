package robot_worlds_13.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.robot.world.AbstractWorld;

public class TerminalListener implements Runnable {

    private final ServerSocket server;
    private final Server serverObject;
    private final AbstractWorld world;

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
                    break;
                } 
                
                else if ("dump".equalsIgnoreCase(input)) {
                    System.out.println("\nDump command received... ");
                    
                    getRobotsInWorld();
                    getObstaclesInWorld();

                } else if ("robots".equalsIgnoreCase(input)) {
                    System.out.println("\nRobots command received...");
                    
                    getRobotsInWorld();


                }
                
                else {
                    System.out.println("\nInvalid terminal command received: " +"'" + input + "'");
                    System.out.println("Hint use 'robots', 'quit' or 'dump'");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private void closeServer() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private void getObstaclesInWorld () {
        if (world.getObstaclesAsString().isEmpty()) {
            System.out.println("There are no obstacles in the world");
            return;
        }
        System.out.println("Obstacles in world: ");
        for (String obstacle: world.getObstaclesAsString()) {
            System.out.println("    " + obstacle);
        }
    }
}
