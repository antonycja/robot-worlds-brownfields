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
    Position start;


    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.world = new TextWorld(new SimpleMaze());
    }

    public ClientHandler(Socket clientSocket, AbstractWorld worldChosen) {
        this.clientSocket = clientSocket;
        this.world = worldChosen;
    }

    public ClientHandler(Socket clientSocket, AbstractWorld worldChosen, Position startingPosition) {
        this.clientSocket = clientSocket;
        this.world = worldChosen;
        this.start = startingPosition;
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
            data.put("message", "Connected");
            Map<String, Object> state = new HashMap<>();
            state.put("position", new int[] {0, 0});
            sendMessage(ServerProtocol.buildResponse("OK", data));

            data = new HashMap<>();
            data.put("message", "Connected successfully to server you can launch a robot!, Hint use 'launch robot_name'");
            state = new HashMap<>();
            sendMessage(ServerProtocol.buildResponse("OK", data));
            
            String potentialRobotName;
            while (true) {
                String launchCommand = getCommand();

                Map<String, Object> request = gson.fromJson(launchCommand, new TypeToken<Map<String, Object>>(){}.getType());
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
                    // "Unsupported command"
                    System.out.println("here");
                    data = new HashMap<>();
                    data.put("message", "Could not parse arguments");
                    state = new HashMap<>();
                    sendMessage(ServerProtocol.buildResponse("ERROR", data));
                    continue;
                }
                
                potentialRobotName = (String) arguments.get(0);

                if (requestedCommand.equalsIgnoreCase("launch") && !world.serverObject.robotNames.contains(potentialRobotName)) {
                    data = new HashMap<>();
                    data.put("message", "Successfully launched");
                    state = new HashMap<>();
                    sendMessage(ServerProtocol.buildResponse("OK", data));
                    break;
                }
            }

            this.name = potentialRobotName;
            Robot robot = new Robot(this.name, this.world, this.start);
            world.serverObject.robotNames.add(name);
            ArrayList<Object> currentRobotState = new ArrayList<>();
            currentRobotState.add(robot.getCurrentPosition());
            currentRobotState.add(robot.getCurrentDirection());
            world.serverObject.nameRobotMap.put(name, currentRobotState);

            sendMessage("Hello Kiddo!");

            world.showObstacles();  // will need to now return obstacles, and flush them to user
            ArrayList<String> obstaclesData = world.obstacleInStringFormat;

            sendMessage("Obstacles in the world: " + obstaclesData.get(0));

            sendMessage(robot.toString());
            
            Command command;
            boolean shouldContinue = true;
            String instruction;

            while (true) {
                // getting robot commands from the server
                sendMessage("What must I do next?");
                instruction = getCommand();
                
                //
                Map<String, Object> request = gson.fromJson(instruction, new TypeToken<Map<String, Object>>(){}.getType());
                // String robot = (String) request.get("robot");
                String requestedCommand = (String) request.get("command");
                ArrayList arguments = (ArrayList) request.get("arguments");
                
                if (instruction.matches("ClientQuit")) {
                    break;
                }
                
                // create the command, and execute it on the robot
                try {
                    command = Command.create(requestedCommand, arguments);
                    shouldContinue = robot.handleCommand(command);
                } catch (IllegalArgumentException e) {
                    robot.setStatus("Sorry, I did not understand '" + instruction + "'.");
                }

                // print robot status after executing command
                sendMessage(robot);
                
                if (shouldContinue) {
                    continue;
                } else {
                    break;
                }
            }

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






