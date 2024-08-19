package robot_worlds_13.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private static PrintStream out;
    public static int port;

    // random
    private final Random rand = new Random();

    // keeping track of robots
    ArrayList<String> robotNames = new ArrayList<>();
    ArrayList<Object> states = new ArrayList<>();
//    public final HashMap<String, ArrayList<Object>> nameRobotMap = new HashMap<>();
//    HashMap<String, ArrayList<Object>> nameAndPositionMap = new HashMap<>();

    public final ConcurrentHashMap<String, ArrayList<Object>> nameRobotMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, ArrayList<Object>> nameAndPositionMap = new ConcurrentHashMap<>();

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
        System.out.println("Starting server...");
        System.out.println("Server address: " + NetworkInfo.main(args));
        System.out.println("Port number: " + port + "\n");
        System.out.println("Size: " + config.getSize() + " kliks");
        System.out.println("Pit: " + (config.getPit() == 1 ? "Enabled" : "Disabled"));
        System.out.println("Obstacle: (" + (Objects.equals(config.getObstacle(), "(0,0)") ?"Unknown Position": config.getObstacle() + ")"));
        System.out.println("Lake: (" + (Objects.equals(config.getLake(), "(0,0)") ?"Unknown Position": config.getLake() + ")\n"));

        // Initialize the server object
        Server serverObject = new Server();

        // Path to configuration file
        Path jarPath = Paths.get(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        Path parentPath = jarPath.getParent();
        Path filePath = Paths.get("src/main/java/robot_worlds_13/server/configuration/file.txt").toAbsolutePath().normalize();


        // Load the configuration data from the file
        Map<String, Integer> dataMap;
        AbstractWorld world;
        try {
            // Always save the configuration to the file
            saveConfigurationToFile(filePath.toString(), config);

            dataMap = parseFileToMap(filePath.toString());
            System.out.println("Loading server data...");

            // Maze loaded
            SimpleMaze mazeGenerated = new SimpleMaze(config.getSize());
            mazeGenerated.setMinCoordinate(Math.min(dataMap.get("width"), dataMap.get("height")) / 3);
            mazeGenerated.setMaxCoordinate(Math.min(dataMap.get("width"), dataMap.get("height")) / 3);
            mazeGenerated.generateRandomObstacles();

            world = new TextWorld(mazeGenerated, serverObject, dataMap);

            config.configureWorld(world);


        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
            return;
        }

        // Always save the configuration to the file
        saveConfigurationToFile(filePath.toString(), config);

        displayServerConfiguration(dataMap);
        System.out.println("...\t\t...\t\t...\t\t...\t\t...\t\t...");
//        System.out.println("All Obstacles:\n\t" + ServerConfiguration.showAllObstacles(world) + "\n");

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

    public static void saveConfigurationToFile(String filePath, ServerConfiguration config) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("visibility " + config.getVisibility() + System.lineSeparator());
            writer.write("reload " + config.getReload() + System.lineSeparator());
            writer.write("repair " + config.getRepair() + System.lineSeparator());
            writer.write("shields " + config.getShields() + System.lineSeparator());
            writer.write("shots " + config.getShots() + System.lineSeparator());
            writer.write("width " + config.getSize() + System.lineSeparator());
            writer.write("height " + config.getSize() + System.lineSeparator());
            writer.write("visibility " + config.getVisibility() + System.lineSeparator());

        } catch (IOException e) {
            System.err.println("Error writing to configuration file: " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     * @param message The message to broadcast.
     */
    public static void broadcastMessage(String message) {
        Iterator<Socket> iterator = clientConnections.iterator();
        while (iterator.hasNext()) {
            Socket client = iterator.next();
            if (!client.isClosed()) {
                try {
                    out = new PrintStream(client.getOutputStream());
                    out.println(message);
                    out.flush();
                } catch (IOException e) {
                    System.err.println("Error broadcasting message to a client: " + e.getMessage());
                    iterator.remove(); // Remove the client from the list if there's an error
                }
            } else {
                iterator.remove(); // Remove the client if the socket is closed
            }
        }
    }



//    public static void broadcastMessage(String message) {
//        for (Socket client : clientConnections) {
//            try {
//                out = new PrintStream(client.getOutputStream());
//                out.println(message);
//                out.flush();
//            } catch (IOException e) {
//                System.err.println("Error broadcasting message to a client: " + e.getMessage());
//            }
//        }
//    }

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
//        this.robotNames.remove(name);
//
//        nameRobotMap.entrySet().removeIf(entry -> entry.getKey().equals(name));
//        nameAndPositionMap.entrySet().removeIf(entry -> entry.getKey().equals(name));

        this.robotNames.remove(name);
        nameRobotMap.remove(name);
        nameAndPositionMap.remove(name);

    }



    /**
     * Displays the server configuration.
     * @param dataMap The map containing the server configuration data.
     */
    public static void displayServerConfiguration(Map<String, Integer> dataMap) {
        for (String attribute : dataMap.keySet()) {
            switch (attribute) {
                case "repair":
                    System.out.println("\t-Repair: " + dataMap.get(attribute) + " seconds per robot");
                    break;
                case "shields":
                    System.out.println("\t-Shield: " + dataMap.get(attribute) + " shields maximum");
                    break;
                case "reload":
                    System.out.println("\t-Reload: " + dataMap.get(attribute) + " seconds for 1 rocket");
                    break;
                case "distance":
                    System.out.println("\t-Distance: " + dataMap.get(attribute) + " kliks");
                    break;
                case "visibility":
                    System.out.println("\t-Visibility: " + dataMap.get(attribute) + " kliks");
                    break;
                case "round":
                    System.out.println("\t-Round time: " + dataMap.get(attribute) + " seconds");
                    break;
                case "size":
                    System.out.println("    Size: " + dataMap.get(attribute) + " kliks");
                    break;
                case "pit":
                    System.out.println("    Pit: " + (dataMap.get(attribute) == 1 ? "Enabled" : "Disabled"));
                    break;
                case "obstacle_x":
                case "obstacle_y":
                    System.out.println("    Obstacle: (" + dataMap.get("obstacle_x") + "," + dataMap.get("obstacle_y") + ")");
                    break;
                case "lake_x":
                case "lake_y":
                    System.out.println("    Lake: (" + dataMap.get("lake_x") + "," + dataMap.get("lake_y") + ")");
                    break;
                default:
                    System.out.println("\t-" + attribute + ": " + dataMap.get(attribute));
                    break;
            }
        }
    }
}
