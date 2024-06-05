package robot_worlds_13.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.robot.maze.SimpleMaze;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.TextWorld;

public class Server {

    // clients
    List<ClientHandler> clients = new ArrayList<>();

    // random
    Random rand = new Random();

    // keeping track of robots
    public ArrayList<String> robotNames = new ArrayList<>();
    public ArrayList<Object> states = new ArrayList<>();
    public HashMap<String, ArrayList<Object>> nameRobotMap = new HashMap<>();

    // configured data
    //shields etc
    

    public static void main(String[] args) throws Exception {
        //
        System.out.println("Starting server...\n");
        
        //
        String path = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        String otherFilePath = "/../src/main/java/robot_worlds_13/server/configuration/file.txt";
        String directoryPath = new File(path).getParent() + otherFilePath;
        // System.out.println(directoryPath);

        // load configured variables
        // String filePath = "configuration/file.txt";  // Update this path to where your file is located
        String filePath = directoryPath;

        Map<String, Integer> dataMap = new HashMap<>();
        try {
            dataMap = parseFileToMap(filePath);
            System.out.println("Loading server data...");
            dataMap.forEach((key, value) -> System.out.println("    " + key + ": " + value));
            System.out.println();
        } catch (IOException e) {
            System.err.println("    Error reading file: " + e.getMessage());
        }

        // this server
        Server serverObject = new Server();
        
        // maze loaded
        AbstractWorld world = new TextWorld(new SimpleMaze(), serverObject, dataMap);

        try (ServerSocket serverSocket = new ServerSocket(2201)) {
            System.out.println("Server started. Listening for incoming connections...");

            // thread for listening to terminal commands
            Thread terminalListenerThread = new Thread(new TerminalListener(serverSocket, serverObject, world));
            terminalListenerThread.start();;

            while (true) {
                // accepting clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("Incoming connection accepted");

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, world);
                serverObject.clients.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        }
        catch (Exception e) {
            System.out.println("Server closed");
        }
    }

    public static Map<String, Integer> parseFileToMap(String filePath) throws IOException {
        Map<String, Integer> result = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");  // Assumes that the key and value are separated by a space
                if (parts.length == 2) {
                    String key = parts[0];
                    Integer value = Integer.parseInt(parts[1]);  // Convert the second part to an integer
                    result.put(key, value);
                } else {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        }
        return result;
    }


}
