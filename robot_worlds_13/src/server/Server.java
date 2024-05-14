package server;

import server.robot.maze.SimpleMaze;
import server.robot.Command;
import server.robot.Position;
import server.robot.Robot;
import server.robot.world.AbstractWorld;
import server.robot.world.TextWorld;

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

            while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Incoming connection accepted");

            
            Server serverObject = new Server();

            // Create a new thread for each client
            ClientHandler clientHandler = new ClientHandler();
            Thread clientThread = new Thread(clientHandler);
            clientThread.start();
            }
        }
    }
}
