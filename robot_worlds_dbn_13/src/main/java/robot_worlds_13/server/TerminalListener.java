package robot_worlds_13.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class TerminalListener implements Runnable {

    private final ServerSocket server;
    private final Server serverObject;

    public TerminalListener(ServerSocket serverSocket, Server serverObjectGiven) {
        this.server = serverSocket;
        this.serverObject = serverObjectGiven;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            while ((input = reader.readLine()) != null) {
                if ("quit".equalsIgnoreCase(input)) {
                    System.out.println("Received quit command. Closing all clients and the server...");
                    closeAllClients();
                    closeServer();
                    break;
                } else if ("robots".equalsIgnoreCase(input)) {
                    System.out.println("Robots command received \n");
                    
                    if (serverObject.nameRobotMap.isEmpty()){
                        System.out.println("No robots on world yet");
                    }

                    for (String name: serverObject.nameRobotMap.keySet()) {
                        ArrayList<Object> currentState = serverObject.nameRobotMap.get(name);
                        System.out.println("Robot name: " + name + "\n" + 
                                            "Current position: " + currentState.get(0).toString() + "\n" + 
                                            "Current direction: " + currentState.get(1).toString() + "\n");
                    }

                    System.out.println();

                } else {
                    System.out.println("Terminal command received: " + input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeAllClients() {
        List<ClientHandler> clients = serverObject.clients;
        for (ClientHandler client : clients) {
                client.sendMessage("Server shutting down...");
                try {
                    client.clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        clients.clear();
        System.out.println("All clients closed...");
    }

    private void closeServer() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
