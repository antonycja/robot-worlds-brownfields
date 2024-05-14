package server;

import server.maze.SimpleMaze;
import server.world.AbstractWorld;
import server.world.TextWorld;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(2201)) {
            System.out.println("Server started. Listening for incoming connections...");

            try (Socket socket = serverSocket.accept()) {
                System.out.println("Incoming connection accepted");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //creates new buffered reader object, reader.. reads input from client
                     PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                    AbstractWorld world = new TextWorld(new SimpleMaze());  //"word" represents the game world
                    Robot robot = new Robot("Robot", world);

                    while (true) {  //processes incoming commands from the client
                        String instruction = reader.readLine();
                        try {
                            Command command = Command.create(instruction);
                            boolean shouldContinue = robot.handleCommand(command);
                            writer.println(robot.toString());
                        } catch (IllegalArgumentException e) {
                            writer.println("Sorry, I did not understand '" + instruction + "'.");
                        }
                    }
                }
            }
        }
    }
}