package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Random;

import server.robot.*;
import server.robot.maze.*;
import server.robot.world.*;

/*
 * responsable for each thread
 */
public class ClientHandler implements Runnable {
    // This class by implementing the runnable interface, make each robot in the world
    // it is responsible for giving the robot properties alread defined by the server's configuration

    // socket variables
    private final Socket clientSocket;
    DataInputStream dis;
    DataOutputStream dos;
    String clientIdentifier;
    Scanner commandLine;

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
            
            sendMessage("Connected");

            sendMessage("What do you want to name your robot?");
            this.name = getCommand();

            sendMessage(name + " " + "has successfully launched");

            Robot robot = new Robot(name);

            sendMessage("Hello Kiddo!");

            world.showObstacles();  // will need to now return obstacles, and flush them to user

            sendMessage(robot.toString());
            
            Command command;
            boolean shouldContinue = true;
            String instruction;

            while (true) {
                // getting robot commands from the server
                sendMessage("What must I do next?");
                instruction = getCommand();
                
                if (instruction.matches("ClientQuit")) {
                    break;
                }
                
                // create the command, and execute it on the robot
                try {
                    command = Command.create(instruction);
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
            e.printStackTrace();
        }
    }

    private String getClientIdentifier(Socket clientSocket) {
        // Here you can determine the identifier based on client's IP address or any other parameter
        return clientSocket.getInetAddress().getHostAddress(); // Using client's IP address as identifier
    }

    public String getCommand(){
        String message = "";
        try {
            message = dis.readUTF();
            System.out.println("Client " + clientIdentifier + " says: " + message);

        } catch (IOException e) {
            return "ClientQuit";
        }
        return message;
    }

    public void sendMessage(String question) {
        String response = "";
        try {
            dos.writeUTF(response + question);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Robot target) {
        String response = target.toString();
        try {
            dos.writeUTF(response);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}






