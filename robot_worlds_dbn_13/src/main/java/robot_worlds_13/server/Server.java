package robot_worlds_13.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import robot_worlds_13.server.robot.maze.SimpleMaze;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.TextWorld;

public class Server {

    // clients
    List<ClientHandler> clients = new ArrayList<>();
    private static List<Socket> clientConnections = new ArrayList<>();
    static DataOutputStream dos;
    static int port = 2201;

    // random
    Random rand = new Random();

    // keeping track of robots
    public ArrayList<String> robotNames = new ArrayList<>();
    public ArrayList<Object> states = new ArrayList<>();
    public HashMap<String, ArrayList<Object>> nameRobotMap = new HashMap<>();
    public HashMap<String, ArrayList<Object>> nameAndPositionMap = new HashMap<>();


    public static void main(String[] args) throws Exception {
        System.out.println("Starting server...\n");

        System.out.println("Server address: " + NetworkInfo.main(args));
        System.out.println("Port number: " + port + "\n");

        
        String path = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        String otherFilePath = "/../src/main/java/robot_worlds_13/server/configuration/file.txt";
        String directoryPath = new File(path).getParent() + otherFilePath;
        String filePath = directoryPath;

        Map<String, Integer> dataMap = new HashMap<>();
        try {
            dataMap = parseFileToMap(filePath);
            System.out.println("Loading server data...");
            displayServerConfiguration(dataMap);
            System.out.println();
        } catch (IOException e) {
            System.err.println("    Error reading file: " + e.getMessage());
        }

        // this server
        Server serverObject = new Server();
        
        // maze loaded
        AbstractWorld world = new TextWorld(new SimpleMaze(), serverObject, dataMap);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening for incoming connections...");

            // thread for listening to terminal commands
            Thread terminalListenerThread = new Thread(new TerminalListener(serverSocket, serverObject, world));
            terminalListenerThread.start();;

            while (true) {
                // accepting clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("Incoming connection accepted");

                // add client to connections
                clientConnections.add(clientSocket);

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, world, serverObject);
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
                    System.exit(0);
                }
            }
        }
        return result;
    }

    public static void broadcastMessage(String message) {
        for (Socket client : clientConnections) {
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF(message);
                dos.flush();

            } catch (IOException e) {
                continue;
            }
        }
    }

    public Map <String, ArrayList<Object>> getNamesAndPositionsOnly () {
        return nameAndPositionMap;
    }

    public void removeRobot (String name) {
        this.robotNames.remove(name);
        
        Iterator<Map.Entry<String, ArrayList<Object>>> iterator = nameRobotMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<Object>> entry = iterator.next();
            if (entry.getKey().equals(name)) {
                iterator.remove(); // Safe remove
            }
        }
        
        Iterator<Map.Entry<String, ArrayList<Object>>> iterator2 = nameAndPositionMap.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<String, ArrayList<Object>> entry = iterator2.next();
            if (entry.getKey().equals(name)) {
                iterator2.remove(); // Safe remove
            }
        }
    }

    static public void displayServerConfiguration(Map<String, Integer> dataMap) {
        for (String attribute: dataMap.keySet()) {
            switch (attribute) {
                case "repair":
                    System.out.println("    Repair: " + dataMap.get(attribute) + " seconds per robot");
                    break;
                case "shields":
                    System.out.println("    Shield: " + dataMap.get(attribute) + " shields maximum");
                    break;
                case "reload":
                    System.out.println("    Reload: " + dataMap.get(attribute) + " seconds per robot");
                    break;
                case "visibility":
                    System.out.println("    Visibility: " + dataMap.get(attribute) + " steps forward");
                    break;
                case "bulletDistance":
                    System.out.println("    Bullet Distance: " + dataMap.get(attribute) + " steps forward");
                    break;
                case "shots":
                    System.out.println("    Shots: " + dataMap.get(attribute) + " shots maximum");
                    break;
                case "width":
                    
                    System.out.println("    Width: " + dataMap.get(attribute) + " kliks");
                    if (dataMap.get(attribute) < 300) {
                        System.out.println("Too small of a world size");
                        System.exit(0);
                    }
                    break;
                case "height":
                    System.out.println("    Height: " + dataMap.get(attribute) + " kliks");
                    if (dataMap.get(attribute) < 300) {
                        System.out.println("Too small of a world size");
                        System.exit(0);
                    }
                    break;
                default:
                    break;
                
            }
            
        }
    }

}
