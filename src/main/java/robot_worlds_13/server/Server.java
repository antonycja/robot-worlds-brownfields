package robot_worlds_13.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;

import robot_worlds_13.server.configuration.ServerConfiguration;
import robot_worlds_13.server.robot.maze.SimpleMaze;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.TextWorld;

/**
 * Class representing the server for the robot world.
 */
public class Server {

    // clients
    List<ClientHandler> clients = new ArrayList<>();
    private static final List<Socket> clientConnections = new ArrayList<>();
    private static DataOutputStream dos;
    public static int port;

    // random
    private final Random rand = new Random();

    // keeping track of robots
    ArrayList<String> robotNames = new ArrayList<>();
    ArrayList<Object> states = new ArrayList<>();
    public final HashMap<String, ArrayList<Object>> nameRobotMap = new HashMap<>();
    HashMap<String, ArrayList<Object>> nameAndPositionMap = new HashMap<>();

    /**
     * Main method to start the server.
     * @param args Command line arguments.
     * @throws Exception Throws an exception if an error occurs.
     */
    public static void main(String[] args) throws Exception {
        ServerConfiguration config = new ServerConfiguration();

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--size":
                case "-s":
                    if (i + 1 < args.length) {
                        config = new ServerConfiguration(config.getPortNum(), Integer.parseInt(args[++i]), config.getPit(), config.getObstacle(), config.getLake());
                    } else {
                        System.err.println("Missing value for --size or -s");
                        System.exit(1);
                    }
                    break;
                case "--pit":
                case "-p":
                    if (i + 1 < args.length) {
                        config = new ServerConfiguration(config.getPortNum(), config.getSize(), Integer.parseInt(args[++i]), config.getObstacle(), config.getLake());
                    } else {
                        System.err.println("Missing value for --pit or -p");
                        System.exit(1);
                    }
                    break;
                case "--obstacle":
                case "-o":
                    if (i + 1 < args.length) {
                        config = new ServerConfiguration(config.getPortNum(), config.getSize(), config.getPit(), args[++i], config.getLake());
                    } else {
                        System.err.println("Missing value for --obstacle or -o");
                        System.exit(1);
                    }
                    break;
                case "--lake":
                case "-l":
                    if (i + 1 < args.length) {
                        config = new ServerConfiguration(config.getPortNum(), config.getSize(), config.getPit(), config.getObstacle(), args[++i]);
                    } else {
                        System.err.println("Missing value for --lake or -l");
                        System.exit(1);
                    }
                    break;
                default:
                    System.err.println("Unknown argument: " + args[i]);
                    System.exit(1);
            }
        }

        port = config.getPortNum();
        System.out.println("Starting server...\n");
        System.out.println("Server address: " + NetworkInfo.main(args));
        System.out.println("Port number: " + port + "\n");
        System.out.println("Size: " + config.getSize() + " kliks");
        System.out.println("Pit: " + (config.getPit() == 1 ? "Enabled" : "Disabled") + "\n");


        // Path to configuration file
        Path jarPath = Paths.get(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        Path parentPath = jarPath.getParent();
        Path filePath = Paths.get("src/main/java/robot_worlds_13/server/configuration/file.txt").toAbsolutePath().normalize();

        // Load server configuration data from file
        Map<String, Integer> dataMap = new HashMap<>();
        if (Files.notExists(filePath)) {
            System.err.println("Configuration file not found: " + filePath);
            System.exit(1);
        }
        try {
            dataMap = parseFileToMap(filePath.toString());
            System.out.println("Loading server data...");
            displayServerConfiguration(dataMap);
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }

        // Initialize the server object
        Server serverObject = new Server();

        // Maze loaded
        SimpleMaze mazeGenerated = new SimpleMaze();
        mazeGenerated.setMinCoordinate(Math.min(dataMap.get("width"), dataMap.get("height")) / 3);
        mazeGenerated.setMaxCoordinate(Math.min(dataMap.get("width"), dataMap.get("height")) / 3);
        mazeGenerated.generateRandomObstacles();

        AbstractWorld world = new TextWorld(mazeGenerated, serverObject, dataMap);
        config.configureWorld(world);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening for incoming connections...");

            // Thread for listening to terminal commands
            Thread terminalListenerThread = new Thread(new TerminalListener(serverSocket, serverObject, world));
            terminalListenerThread.start();

            while (true) {
                // Accepting clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("Incoming connection accepted");

                // Add client to connections
                clientConnections.add(clientSocket);

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, world, serverObject);
                serverObject.clients.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (Exception e) {
            System.err.println("Server encountered an error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Parses a configuration file into a map.
     * @param filePath The path to the configuration file.
     * @return A map containing the configuration data.
     * @throws IOException Throws an exception if an error occurs during file parsing.
     */
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
        } catch (FileNotFoundException e) {
            System.err.println("Configuration file not found: " + filePath);
            throw e;
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            throw e;
        }
        return result;
    }

    /**
     * Broadcasts a message to all connected clients.
     * @param message The message to broadcast.
     */
    public static void broadcastMessage(String message) {
        for (Socket client : clientConnections) {
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF(message);
                dos.flush();
            } catch (IOException e) {
                System.err.println("Error broadcasting message to a client: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves names and positions only.
     * @return A map containing robot names and their positions.
     */
    public Map<String, ArrayList<Object>> getNamesAndPositionsOnly() {
        return nameAndPositionMap;
    }

    /**
     * Removes a robot from the server.
     * @param name The name of the robot to remove.
     */
    public void removeRobot(String name) {
        this.robotNames.remove(name);

        nameRobotMap.entrySet().removeIf(entry -> entry.getKey().equals(name));
        nameAndPositionMap.entrySet().removeIf(entry -> entry.getKey().equals(name));
    }

    /**
     * Displays the server configuration.
     * @param dataMap The map containing the server configuration data.
     */
    public static void displayServerConfiguration(Map<String, Integer> dataMap) {
        for (String attribute : dataMap.keySet()) {
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
                    if (dataMap.get(attribute) < 500) {
                        System.err.println("Too small of a world size");
                        System.exit(0);
                    }
                    break;
                case "height":
                    System.out.println("    Height: " + dataMap.get(attribute) + " kliks");
                    if (dataMap.get(attribute) < 500) {
                        System.err.println("Too small of a world size");
                        System.exit(0);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
