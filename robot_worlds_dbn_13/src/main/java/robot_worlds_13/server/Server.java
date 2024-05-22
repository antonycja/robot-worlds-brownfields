package robot_worlds_13.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        
        // this server
        Server serverObject = new Server();

        try (ServerSocket serverSocket = new ServerSocket(2201)) {
            System.out.println("Server started. Listening for incoming connections...");

            // thread for listening to terminal commands
            Thread terminalListenerThread = new Thread(new TerminalListener(serverSocket, serverObject));
            terminalListenerThread.start();;

            while (true) {
                // accepting clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("Incoming connection accepted");
                

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                serverObject.clients.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        }
        catch (Exception e) {
            System.out.println("Server closed");
        }
    }
}
