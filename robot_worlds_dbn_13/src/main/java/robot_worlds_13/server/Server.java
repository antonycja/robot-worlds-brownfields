package robot_worlds_13.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static List<ClientHandler> clients = new ArrayList<>();

    public static void broadcast(String message) {
        for (ClientHandler client : clients) {
            try {
                client.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Server serverObject = new Server();

        try (ServerSocket serverSocket = new ServerSocket(2204)) {
            System.out.println("Server started. Listening for incoming connections...");

            while (true) {
                // Accepting clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("Incoming connection accepted");

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler); // Add client handler to the list of clients
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (Exception e) {
            System.out.println("Server closed due to error: " + e.getMessage());
        }
    }
}
