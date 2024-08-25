package robot_worlds_13.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import org.jline.reader.EndOfFileException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import robot_worlds_13.server.robot.Command;
import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.robot.maze.SimpleMaze;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.IWorld;
import robot_worlds_13.server.robot.world.Obstacle;
import robot_worlds_13.server.robot.world.TextWorld;
 // TODO: check this out for request building
/**
 * Responsible for handling communication with a client connected to the server.
 */
public class ClientHandler implements Runnable {
    // This class by implementing the runnable interface, make each robot in the world
    // it is responsible for giving the robot properties already defined by the server's configuration

    // socket variables
    final Socket clientSocket;
    BufferedReader in;
    PrintStream out;
    String clientIdentifier;
    Scanner commandLine;
    ServerProtocol serverProtocol;
    private static Gson gson = new Gson();
    private Timer statusCheckTimer;
    Robot robot;

    // loaded variables
    public AbstractWorld world;
    public Server connectedServer;

    // robot variables
    String name;

    /**
     * Constructs a ClientHandler instance with the given client socket.
     * @param clientSocket The socket connected to the client.
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.world = new TextWorld(new SimpleMaze());
    }

    /**
     * Constructs a ClientHandler instance with the given client socket, world, and connected server.
     * @param clientSocket The socket connected to the client.
     * @param worldChosen The world associated with the client.
     * @param currentConnectedServer The server to which the client is connected.
     */
    public ClientHandler(Socket clientSocket, AbstractWorld worldChosen, Server currentConnectedServer) {
        this.clientSocket = clientSocket;
        this.world = worldChosen;
        this.connectedServer = currentConnectedServer;

    }

    /**
     * Handles the communication with the client.
     */
    @Override
    public void run() {
        try {
            // Initialization
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintStream(clientSocket.getOutputStream());
            this.clientIdentifier = getClientIdentifier(clientSocket);
            this.commandLine = new Scanner(System.in);


            // Inform client of successful connection
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> state = new HashMap<>();
//            data.clear();
//            data.put("message", "\nConnected successfully to server you can launch a robot!\n");
//            state.clear();
//            out.println("Connected successfully to server you can launch a robot");
//            sendMessage(ServerProtocol.buildResponse("OK",data));

//            Server.broadcastMessage(ServerProtocol.buildResponse("OK", data));

            // luanch validation
            String potentialRobotName = "";
            int maximumShieldStrength = 0;
            int maximumShots = 0;
            while (true) {
                String launchCommand = getCommand();

                Map<String, Object> request;
                try {
                    request = gson.fromJson(launchCommand, new TypeToken<Map<String, Object>>(){}.getType());
                } catch (Exception e) {
                    data.clear();
                    data.put("message", "Could not parse arguments");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

//                    continue;
                    break;
                }

                // Parsing command
                if (request.get("robot") == null || request.get("command") == null || request.get("arguments") == null) {
                    data.clear();
                    data.put("message", "Could not parse arguments");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

//                    continue;
                    break;
                }

                potentialRobotName = (String) request.get("robot");
                String requestedCommand = (String) request.get("command");
                ArrayList arguments = (ArrayList) request.get("arguments");
                
                String make;
                try {
                    make = (String) arguments.get(0);
                } catch (Exception e) {
                    data.clear();
                    data.put("message", "Could not parse arguments");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

                }

                
                try {
                    maximumShieldStrength = (int) Math.round((double) Integer.parseInt((String) arguments.get(1)));
                } catch (Exception e) {
                    data.clear();
                    data.put("message", "Could not parse arguments");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

                }

                
                try {
                    maximumShots = (int) Math.round((double) Integer.parseInt((String) arguments.get(2)));
                } catch (Exception e) {
                    data.clear();
                    data.put("message", "Could not parse arguments");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

                }

                // Validating command and arguments

                if (!requestedCommand.equalsIgnoreCase("launch")) {
                    // "Unsupported command"
                    data = new HashMap<>();
                    data.put("message", "Unsupported command");
                    state = new HashMap<>();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

//                    continue;
                    break;
                }

                if (!NameChecker.isValidName(potentialRobotName)) {
                    // "Unsuppotted name"
                    data.clear();
                    data.put("message", "Could not parse arguments");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

//                    continue;
                    break;
                }

                // if name already exists in world
                if (world.serverObject.robotNames.contains(potentialRobotName)) {
                    data.clear();
                    data.put("message", "Too many of you in this world");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

//                    continue;
                    break;
                }

                // if number of robots is too large
                if (world.serverObject.robotNames.size() >= 5) {
                    data.clear();
                    data.put("message", "No more space in this world");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

                    break;
                }

                // Launching the robot
                if (requestedCommand.equalsIgnoreCase("launch") && !world.serverObject.robotNames.contains(potentialRobotName)) {

//                    ArrayList<Object> objects = new ArrayList<>();
                    // data
//                    data.clear();
//                    data.put("visibility", world.visibility);
//                    data.put("position", new int[] {(int) world.getPosition().getX(), (int) world.getPosition().getY()});
//                    data.put("objects", objects.isEmpty() ?"[]":objects);
//                    data.put("message", "Successfully launched " + "width " + world.width + " height " + world.height);
//                    // state
//                    state.clear();
//                    state.put("shields", world.maximumShieldStrength);
//                    state.put("position", new int[] {world.getPosition().getX(), world.getPosition().getY()});
//                    state.put("shots", world.maximumShots);
//                    state.put("direction", world.getCurrentDirection());
//                    state.put("status", "NORMAL");
//
//                    sendMessage(ServerProtocol.buildResponse("OK", data, state));
//                    System.out.print(ServerProtocol.buildResponse("OK", data, state));
                    try {
//                        TODO: changed sleep time to 1/2 sec
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.out.println("Error encountered with sleep thread");
                    }
                    
                    break;
                }
            }
//            data.put("position", robot.position);
//            sendMessage(ServerProtocol.buildResponse("OK",data));



            // checker if position is available on the world
            Position start = world.generatePosition();

            // show other robots on gui
            Map <String, ArrayList<Object>> robotMap = connectedServer.nameAndPositionMap;
            Map <String, ArrayList<Object>> currentRobotMap = new HashMap<>();
            
            for (Map.Entry<String, ArrayList<Object>> entry: robotMap.entrySet()) {
                    String robotName = entry.getKey();
                    ArrayList<Object> list = entry.getValue();
                    ArrayList<Object> states = new ArrayList<>();
                    
                    Position thatRobotPosition = (Position) list.get(0);
                    IWorld.Direction thatRobotDirection = (IWorld.Direction) list.get(1);
                    states.add(thatRobotPosition);
                    states.add(thatRobotDirection);
                    currentRobotMap.put(robotName, states);
                }
            
            // System.out.println();
//            data.clear();
//            data.put("message", "OTHERCHARACTERS");
//            state.clear();
//            state.put("robots", currentRobotMap);
//            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));

            // load robots bulletDistance, shields, shots
            int maxShields;
            if (maximumShieldStrength >= world.maximumShieldStrength) {
                maxShields = world.maximumShieldStrength;
            } else {
                maxShields = maximumShieldStrength;
            }
            int maxShots;
            if (maximumShots >= world.maximumShieldStrength) {
                maxShots = world.maximumShots;
            } else {
                maxShots = maximumShots;
            }
            Map<String, Integer> attributes = new HashMap<>();
            attributes.put("shields", maxShields);
            attributes.put("shots", maxShots);
            attributes.put("visibility", world.visibility);
            attributes.put("bulletDistance", world.visibility);

            // make the robot itself
            this.name = potentialRobotName;
            this.robot = new Robot(this.name, this.world, start, attributes);
            world.serverObject.robotNames.add(name);
            ArrayList<Object> currentRobotState = new ArrayList<>();
            currentRobotState.add(robot.getCurrentPosition());
            currentRobotState.add(robot.getCurrentDirection());

            world.serverObject.nameAndPositionMap.put(name, currentRobotState);
            currentRobotState.add(robot);

            world.serverObject.nameRobotMap.put(name, currentRobotState);

            

            // send hello message
//            Robot rob = (Robot) currentRobotState.get(2);

            // data
//            data.clear();
//            data.put("visibility", robot.worldData.visibility);
//            data.put("position", new int[] {robot.getPosition().getX(), robot.getPosition().getY()});
//            data.put("objects", new ArrayList<>());
//
//            // state
//            state.clear();
//            state = robot.getRobotState();

//            sendMessage(ServerProtocol.buildResponse("OK", data, state));



//            data.clear();
//            data.put("message", "Hello Kiddo!\n");
//            int[] position = new int[] {
//                    ((Position) currentRobotState.get(0)).getX(),
//                    ((Position) currentRobotState.get(0)).getY()
//            };
//            data.put("position", position);
//            sendMessage(ServerProtocol.buildResponse("OK", data));

            

            // Obstacles
            // world.showObstacles();  // will need to now return obstacles, and flush them to user
            List<String> obstaclesData = world.getObstaclesAsString();
            StringBuilder obstacleMessage = new StringBuilder();
            if (obstaclesData.isEmpty()) {
                obstacleMessage.append("There are no obstacles in the world");
                
            } else {
                obstacleMessage.append("Obstacles in world: \n");
                for (String obstacle: obstaclesData) {
                    obstacleMessage.append("\t").append(obstacle);
                }
            }

//            data.clear();
//            data.put("message", obstacleMessage.toString());
//            state.clear();
//            sendMessage(ServerProtocol.buildResponse("DISPLAY", data, state));
//            sendMessage(ServerProtocol.buildResponse("OK", data, state));
//            TODO: TEST AND CHANGE THE CODE UP.

//            List<Obstacle> obstacles = world.getObstacles();
//            data.clear();
//            data.put("message", "OBSTACLES");
//            state.clear();
//            state.put("obstacles", obstacles);
//            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));


//            List<Obstacle> lakes = world.getLakes();
//            data.clear();
//            data.put("message", "LAKES");
//            state.clear();
//            state.put("obstacles", lakes);
//            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));

//            List<Obstacle> pits = world.getBottomLessPits();
//            data.clear();
//            data.put("message", "PITS");
//            state.clear();
//            state.put("obstacles", pits);
//            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));

            // starting position
            ArrayList<Object> objects = new ArrayList<>();

            data.clear();
            data.put("visibility", robot.worldData.visibility);
            data.put("position", new int[] {robot.getPosition().getX(), robot.getPosition().getY()});
            data.put("objects", objects.isEmpty() ?"[]":objects);

            // state
            state.clear();
            state = robot.getRobotState();
            sendMessage(ServerProtocol.buildResponse("OK", data, state));
            System.out.println("Response: " + ServerProtocol.buildResponse("OK", data, state));


//            data.clear();
//            data.put("message", "LAUNCH");
//            state.clear();
//            state = robot.getGUIRobotState();
//            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));
            Command command;
            boolean shouldContinue = true;
            String instruction;
            
            statusCheckTimer = new Timer();
            statusCheckTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (robot != null && ("DEAD".equals(robot.getStatus()))) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("message", "Your robot has died because it ran out of shields");
//                        sendMessage(ServerProtocol.buildResponse("DISPLAY", data));
                        
                        connectedServer.removeRobot(name);
                        world.serverObject.robotNames.remove(name);
                        world.serverObject.nameRobotMap.remove(name);
                        
//                        Map<String, Object> state = new HashMap<>();
//                        data.clear();
//                        data.put("message", "REMOVE");
//                        state.clear();
//                        state.put("robots", name);
//                        Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));

                        ArrayList<Object> currentRobotState = new ArrayList<>();
                        currentRobotState.add(robot.getCurrentPosition());
                        currentRobotState.add(robot.getCurrentDirection());
                        world.serverObject.nameRobotMap.remove(name, currentRobotState);
                        System.out.println("Client " + clientIdentifier + " disconnected.");
                        
                        this.cancel();
                        statusCheckTimer.cancel();
                        statusCheckTimer.purge();
                    }
                }
            }, 0, 1000);

            while (true) {
                // sending prompt to client
//                data.clear();
//                data.put("message", "What must I do next?");
//                out.println("What must I do next?");
                // TODO: FIX THIS TO ALWAYS DISPLAY ON CLIENT
//                sendMessage(ServerProtocol.buildResponse("DISPLAY", data));
//                System.out.println(ServerProtocol.buildResponse("DISPLAY", data));
                // get command as json string
                instruction = getCommand();
                
                if (instruction.equals("{}")) {
                    break;
                }
                
                // unpack command
                Map<String, Object> request = new HashMap<>();
                try {
                    request = gson.fromJson(instruction, new TypeToken<Map<String, Object>>(){}.getType());
                } catch (Exception e) {
                    System.out.println(request);
                    System.err.println("Failed to parse JSON: " + e.getMessage());
                    data.clear();
                    data.put("message", "Invalid command");
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println(ServerProtocol.buildResponse("ERROR", data));
                    e.printStackTrace();
                    break;
                }

                String name = (String) request.get("robot");
                if (!name.equalsIgnoreCase(potentialRobotName)){
                    data.clear();
                    data.put("message", "Robot does not exist");
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println(ServerProtocol.buildResponse("ERROR", data));
                    break;
                }

                // String robot = (String) request.get("robot");
                String requestedCommand = (String) request.get("command");
                ArrayList arguments = (ArrayList) request.get("arguments");
                
                if (requestedCommand.matches("ClientQuit")) {
                    break;
                }

                if (robot.shields < 0 || robot.getStatus() == "DEAD") {
                    data.clear();
                    data.put("message", "Your robot has died because it ran out of shields5");
                    // TODO: ALSO FIX THIS
//                    sendMessage(ServerProtocol.buildResponse("DISPLAY", data));
                    break;
                }
                
                // create the command, and execute it on the robot
                try {
                    robot.previouPosition = robot.getCurrentPosition();
                    command = Command.create(requestedCommand, arguments);
                    shouldContinue = robot.handleCommand(command);

                    data.clear();
                    data.put("message", "Command '" + requestedCommand + "' executed.");
                    data.put("visibility", robot.worldData.visibility);
                    data.put("position", new int[] {robot.getPosition().getX(), robot.getPosition().getY()});
                    data.put("objects", new ArrayList<>());

//                  state
                    state.clear();
                    state = robot.getRobotState();
//                    sendMessage(ServerProtocol.buildResponse("OK", data, state));
//                    System.out.println("Response: " + ServerProtocol.buildResponse("OK", data, state));

                } catch (IllegalArgumentException e) {
                    data.clear();
                    data.put("message", "Unsupported command");
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));
//                    continue;
                    break;
                    
                } catch (IndexOutOfBoundsException e) {
                    data.clear();
                    data.put("message", "Could not parse arguments '" + arguments + "'.");
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    System.out.println("Response: " + ServerProtocol.buildResponse("ERROR", data));

//                    continue;
                    break;
                }

                // print robot status after executing command
                System.out.println(ServerProtocol.buildResponse("OK", data, state));
                sendMessage(ServerProtocol.buildResponse("OK", data, state));
//                sendMessage(robot.getResponseToRobot());
//                Server.broadcastMessage(robot.getGUIResponseToRobot());
                
                if (shouldContinue) {
                    continue;
                } else {
                    break;
                }
            }
//            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));
            // removing current robot from sever
            connectedServer.removeRobot(name);
            world.serverObject.robotNames.remove(name);
            world.serverObject.nameRobotMap.remove(name);
//            data.clear();
//            data.put("message", "REMOVE");
//            state.clear();
//            state.put("robots", name);
//            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));
            currentRobotState = new ArrayList<>();
            currentRobotState.add(robot.getCurrentPosition());
            currentRobotState.add(robot.getCurrentDirection());
            world.serverObject.nameRobotMap.remove(name, currentRobotState);
            System.out.println("Client " + clientIdentifier + " disconnected.");
            clientSocket.close();
            // Cleanup after client disconnects...
        } catch (IOException e) {
            System.out.println("Your robot has died");
            System.exit(0);
        } catch (EndOfFileException e) {
            System.out.println("Your robot has died because it ran out of shields");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getClientIdentifier(Socket clientSocket) {
        // Here you can determine the identifier based on client's IP address or any other parameter
        return clientSocket.getInetAddress().getHostAddress();
    }

    /**
     * Retrieves a command from the client.
     * @return The command received from the client.
     */
    public String getCommand(){
        String message = "";
        try {
            message = in.readLine();
            if (!isValidJson(message)) {
                System.out.println("Invalid JSON received: " + message);
                return "{}"; // Return empty JSON or handle appropriately
            }
            System.out.println("Client " + clientIdentifier + " says: " + message);
        } catch (Exception e) {
            return "{}"; // or handle error appropriately
        }
        return message;
    }

    /**
     * Checks if a given string is a valid JSON.
     * @param json The string to be checked.
     * @return True if the string is a valid JSON, otherwise false.
     */
    public boolean isValidJson(String json) {
        json = json.trim();
        return (json.startsWith("{") && json.endsWith("}")) || (json.startsWith("[") && json.endsWith("]"));
    }

    /**
     * Sends a message to the client.
     * @param question The message to be sent.
     */
    public void sendMessage(String question) {
        out.println(question);
        out.flush();
    }

    /**
     * Sends a message to the client.
     * @param target The robot to send the message to.
     */
    public void sendMessage(Robot target) {
        String response = target.toString();
        out.println(response);
        out.flush();
    }
}






