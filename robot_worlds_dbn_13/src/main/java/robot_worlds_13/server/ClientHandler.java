package robot_worlds_13.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import robot_worlds_13.server.robot.*;
import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.robot.world.*;

/*
 * responsable for each thread
 */
public class ClientHandler implements Runnable {
    // This class by implementing the runnable interface, make each robot in the world
    // it is responsible for giving the robot properties alread defined by the server's configuration

    // socket variables
    final Socket clientSocket;
    DataInputStream dis;
    DataOutputStream dos;
    String clientIdentifier;
    Scanner commandLine;
    ServerProtocol serverProtocol;
    private static Gson gson = new Gson();

    // loaded variables
    AbstractWorld world;

    // robot variables
    String name;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.world = new TextWorld(new SimpleMaze());
    }

    public ClientHandler(Socket clientSocket, AbstractWorld worldChosen) {
        this.clientSocket = clientSocket;
        this.world = worldChosen;
    }

    @Override
    public void run() {
        try {
            this.dis = new DataInputStream(clientSocket.getInputStream());
            this.dos = new DataOutputStream(clientSocket.getOutputStream());
            this.clientIdentifier = getClientIdentifier(clientSocket);
            this.commandLine = new Scanner(System.in);
            
            // sendMessage("Connected");
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> state = new HashMap<>();
            data.clear();
            data.put("message", "\nConnected successfully to server you can launch a robot!\n");
            state.clear();
            sendMessage(ServerProtocol.buildResponse("DISPLAY", data));
            
            // luanch validation
            String potentialRobotName;
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
                    continue;
                }
                String requestedCommand = (String) request.get("command");
                ArrayList arguments = (ArrayList) request.get("arguments");
                

                if (!requestedCommand.equalsIgnoreCase("launch")) {
                    // "Unsupported command"
                    data = new HashMap<>();
                    data.put("message", "Unsupported command");
                    state = new HashMap<>();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    continue;
                }

                if (!NameChecker.isValidName(arguments)) {
                    // "Unsuppotted name"
                    data.clear();
                    data.put("message", "Could not parse arguments");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    continue;
                }
                
                potentialRobotName = (String) arguments.get(0);

                // if name already exists in world
                if (world.serverObject.robotNames.contains(potentialRobotName)) {
                    data.clear();
                    data.put("message", "Too many of you in this world");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    continue;
                }

                // if number of robots is too large
                if (world.serverObject.robotNames.size() >= 5) {
                    data.clear();
                    data.put("message", "No more space in this world");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    continue;
                }
                



                if (requestedCommand.equalsIgnoreCase("launch") && !world.serverObject.robotNames.contains(potentialRobotName)) {
                    data.clear();
                    data.put("message", "Successfully launched\n");
                    state.clear();
                    sendMessage(ServerProtocol.buildResponse("DISPLAY", data));
                    break;
                }
            }



            // checker if position is available on the world
            Position start = world.generatePosition();

            // mocking starting position of robot
            // Position startPosition =  new Position(0, 0);
            // if (!serverObject.robotNames.isEmpty()) {
            //     startPosition =  new Position(0, 10);
            // }

            // load robots bulletDistance, shields, shots

            // make the robot itself
            this.name = potentialRobotName;
            Robot robot = new Robot(this.name, this.world, start);
            world.serverObject.robotNames.add(name);
            ArrayList<Object> currentRobotState = new ArrayList<>();
            currentRobotState.add(robot.getCurrentPosition());
            currentRobotState.add(robot.getCurrentDirection());
            currentRobotState.add(robot);
            world.serverObject.nameRobotMap.put(name, currentRobotState);

            // send hello message
            data.clear();
            data.put("message", "Hello Kiddo!\n");
            state.clear();
            sendMessage(ServerProtocol.buildResponse("DISPLAY", data));

            // Obstacles
            // world.showObstacles();  // will need to now return obstacles, and flush them to user
            List<String> obstaclesData = world.getObstaclesAsString();
            String obstacleMessage = "";
            if (obstaclesData.isEmpty()) {
                obstacleMessage += "There are no obstacles in the world";
                
            } else {
                obstacleMessage += "Obstacles in world: \n";
                for (String obstacle: obstaclesData) {
                    obstacleMessage += "    " + obstacle;
                }
            }
            
            data.clear();
            data.put("message", obstacleMessage);
            state.clear();
            sendMessage(ServerProtocol.buildResponse("DISPLAY", data));

            List<Obstacle> obstacles = world.getObstacles();
            data.clear();
            data.put("message", "OBSTACLES");
            state.clear();
            state.put("obstacles", obstacles);
            System.out.println(obstacles);
            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));

            // starting position
            data.clear();
            data.put("position", new int[] {robot.getPosition().getX(), robot.getPosition().getY()});
            data.put("visibility",  world.visibility);
            data.put("reload", world.ammoReloadTime);
            data.put("repair", world.shieldRepairTime);
            data.put("shields", robot.shields);
            state = robot.getRobotState();
            sendMessage(ServerProtocol.buildResponse("OK", data, state));

            data.clear();
            data.put("message", "LAUNCH");
            state.clear();
            state = robot.getGUIRobotState();
            Server.broadcastMessage(ServerProtocol.buildResponse("GUI", data, state));

            
            Command command;
            boolean shouldContinue = true;
            String instruction;

            while (true) {
                // sending prompt to client
                data.clear();
                data.put("message", "What must I do next?");
                sendMessage(ServerProtocol.buildResponse("DISPLAY", data));
                
                // get command as json string
                instruction = getCommand();
                
                // unpack command
                Map<String, Object> request = gson.fromJson(instruction, new TypeToken<Map<String, Object>>(){}.getType());
                // String robot = (String) request.get("robot");
                String requestedCommand = (String) request.get("command");
                ArrayList arguments = (ArrayList) request.get("arguments");
                
                if (requestedCommand.matches("ClientQuit")) {
                    break;
                }
                
                // create the command, and execute it on the robot
                try {
                    command = Command.create(requestedCommand, arguments);
                    shouldContinue = robot.handleCommand(command);
                } catch (IllegalArgumentException e) {
                    data.clear();
                    data.put("message", "Unsupported command '" + requestedCommand + "'.");
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    continue;
                    
                } catch (IndexOutOfBoundsException e) {
                    data.clear();
                    data.put("message", "Could not parse arguments '" + arguments + "'.");
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    continue;
                    
                }

                // print robot status after executing command
                sendMessage(robot.getResponseToRobot());
                Server.broadcastMessage(robot.getGUIResponseToRobot());
                
                if (shouldContinue) {
                    continue;
                } else {
                    break;
                }
            }

            world.serverObject.robotNames.remove(name);
            currentRobotState = new ArrayList<>();
            currentRobotState.add(robot.getCurrentPosition());
            currentRobotState.add(robot.getCurrentDirection());
            world.serverObject.nameRobotMap.remove(name, currentRobotState);
            System.out.println("Client " + clientIdentifier + " disconnected.");
            clientSocket.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    private String getClientIdentifier(Socket clientSocket) {
        // Here you can determine the identifier based on client's IP address or any other parameter
        return clientSocket.getInetAddress().getHostAddress();
    }

    public String getCommand(){
        String message = "";
        try {
            message = dis.readUTF();
            System.out.println("Client " + clientIdentifier + " says: " + message);

        } catch (Exception e) {
            return "ClientQuit";
        }
        return message;
    }

    public void sendMessage(String question) {
        try {
            dos.writeUTF(question);
            dos.flush();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public void sendMessage(Robot target) {
        String response = target.toString();
        try {
            dos.writeUTF(response);
            dos.flush();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }


}






