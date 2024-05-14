package server;

import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(2201)) {
            System.out.println("Server started. Listening for incoming connections...");

            while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Incoming connection accepted");

            // this server
            Server serverObject = new Server();

            // Create a new thread for each client
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            Thread clientThread = new Thread(clientHandler);
            clientThread.start();
            }
        }
    }
}
