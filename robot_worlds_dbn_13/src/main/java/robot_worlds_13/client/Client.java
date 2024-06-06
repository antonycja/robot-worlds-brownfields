package robot_worlds_13.client;

/*
 * This class acts as the user's interface to the program
 * it will employ charecteristics from play
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Binding;
import org.jline.reader.Reference;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.keymap.KeyMap;

import java.util.regex.Matcher;

import com.google.gson.Gson;

public class Client {
    private static final String PORT_REGEX = "\\b([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])\\b";
    private static final String IP_REGEX = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
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

        // connecting to server
        if (args.length == 0) {
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
        } else if (args.length != 2) {
            System.out.println("\nIncorrect ip address or port entered");
            System.out.println("Correct format: 'Client.java ipAddress portNumber");
            System.exit(0);
        } else {
            if (!isValidIPAddress(args[0])) {
                if (!args[0].equalsIgnoreCase("localhost")) {
                    System.out.println("\nIncorrect format for ip address");
                    System.exit(0);
                }
                
            }
            
            if (!isValidPortNumber(args[1])) {
                System.out.println("\nIncorrect format for port address");
                System.exit(0);
            }
            int port = Integer.parseInt(args[1]);
            String address =args[0].toLowerCase();
            try {
                sThisClient = new Socket(address, port);
            } catch (Exception ex) {
                System.out.println("Failed to connect to the server using the specified IP address.");
                System.exit(0);
            }
        }
        

        try {
            dout = new DataOutputStream(sThisClient.getOutputStream());
            din = new DataInputStream(sThisClient.getInputStream());
            
            String response;
            String potentialRobotName = "";

            Main main = new Main();
            // main.showRobot(robotName); // Pass the robot name to the showRobot method
           

            while (true) {
                // try to launch robot
                response = ClientProtocol.jsonResponseUnpacker(din.readUTF());
                System.err.println(response);
                
                if (response.contains("Successfully launched")) {
                    robotName = potentialRobotName;
                    main.setVisible(true); // Show the GUI screen
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

            // Create a terminal
            Terminal terminal = TerminalBuilder.builder().build();

            // Create a LineReader
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

            while (true) {
                // get server messages
                // String robotPosition = 
                response = ClientProtocol.jsonResponseUnpacker(din.readUTF());

                if (response.contains("GUI")) {
                    if (response.contains("LAUNCH")) {
                        
                    }
                    continue;
                }
                
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

    public static boolean isValidPortNumber(String portNumber) {
        Pattern pattern = Pattern.compile(PORT_REGEX);
        Matcher matcher = pattern.matcher(portNumber);
        return matcher.matches();
    }

    public static boolean isValidIPAddress(String ipAddress) {
        Pattern pattern = Pattern.compile(IP_REGEX);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
}
