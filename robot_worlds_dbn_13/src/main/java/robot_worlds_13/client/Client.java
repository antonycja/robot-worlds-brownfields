package robot_worlds_13.client;

/*
 * This class acts as the user's interface to the program
 * it will employ charecteristics from play
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

public class Client {
    static String localAddress = "localhost";
    static String serverIpAddress = "20.20.15.94";
    protected static Socket sThisClient;
    protected static DataOutputStream dout;
    public static DataInputStream din;
    static Scanner line = new Scanner(System.in);
    protected static String robotName;

    static private Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("       " +
                "  __\n" +
                " _(\\    |@@|\n" +
                "(__/\\__ \\--/ __\n" +
                "   \\___|----|  |   __\n" +
                "       \\ }{ /\\ )_ / _\\\n" +
                "       /\\__/\\ \\__O (__      --WELCOME TO ROBOT WORLDS-- \n" +
                "      (--/\\--)    \\__/\n" +
                "      _)(  )(_\n" +
                "     `---''---`");

        try {
            sThisClient = new Socket(localAddress, 2201);
        } catch (Exception ex) {
            System.out.println("Failed to connect to the server using the specified IP address.");
            System.out.println("Resorting to localhost...\n");
            try {
                sThisClient = new Socket(localAddress, 2201);
            } catch (Exception e) {
                System.out.println("Failed to connect to localhost.");
                System.out.println("None of the known servers are running");
                System.out.println("Exiting program...\n");
                return;
            }
        }

        try {
            dout = new DataOutputStream(sThisClient.getOutputStream());
            din = new DataInputStream(sThisClient.getInputStream());
            
            String response;
            String potentialRobotName = "";

            while (true) {
                // try to launch robot
                response = ClientProtocol.jsonResponseUnpacker(din.readUTF());
                System.err.println(response);
                
                if (response.contains("Successfully launched")) {
                    robotName = potentialRobotName;
                    break;
                }

                if (response.contains("Could not parse arguments") || response.contains("Unsupported command") || 
                response.contains("Connected successfully to") || response.contains("Too many of you in this world")) {
                    System.out.println("Launch a robot!, Hint use 'launch robot_name'");
                    // get imput
                    String command = line.nextLine();

                    // save name
                    String[] potentialRobotNameArray = command.split(" ");
                    if (potentialRobotNameArray.length > 1) {
                        potentialRobotName = potentialRobotNameArray[1];
                    }

                    // format input
                    Map<String, Object> formattedCommand = ClientProtocol.jsonRequestBuilder(command);

                    // send to server as json
                    sendJsonRequest(formattedCommand);
                }
            }

            while (true) {
                // get server messages
                response = ClientProtocol.jsonResponseUnpacker(din.readUTF());
                
                // print message to this client
                System.out.println(response);
                
                if (response.contains("Shutting down")) {
                    break;
                } else if (response.contains("Server shutting down...")) {
                    break;
                }
                
                // if the message requires a response prompt current client for input
                // and send the command
                // (what do you want to name your robot / what do you want to do next)
                if (response.startsWith("What")) {
                    // get imput
                    String command = line.nextLine();

                    // format input
                    Map<String, Object> formattedCommand = ClientProtocol.jsonRequestBuilder(command);

                    // send to server as json
                    sendJsonRequest(formattedCommand);
                }
            }

            // close this client
            dout.flush();
            dout.close();
        }
        catch (Exception e) {
            // System.err.println(e);
            try {
                
                dout.writeUTF("off");
                dout.flush();
                dout.close();
                // sThisClient.close();
            } catch (Exception i) {
                System.out.println("Connection problem encountered: Server is down or you do not an active internet connection");
            }
        }

        
    }

    static public void sendJsonRequest(Map<String, Object> commandDetails) {
        commandDetails.put("robot", robotName);  // Add robot name to the command details
        String jsonRequest = gson.toJson(commandDetails);
        
        System.out.println("Sending command: " + jsonRequest + "\n");  // For debug purposes
        
        try {
            dout.writeUTF(jsonRequest);
            dout.flush();
        } catch (Exception i) {
            System.out.println("Could not send request");
        }

    }
}
