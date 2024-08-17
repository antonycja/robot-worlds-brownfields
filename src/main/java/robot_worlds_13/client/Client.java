/**
 * This package contains the client-side implementation for the Robot Worlds application.
 * The main entry point is the `Client` class, which handles the client-server communication
 * and user interaction.
 */
package robot_worlds_13.client;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import java.util.regex.Matcher;

import com.google.gson.Gson;

import static robot_worlds_13.server.configuration.ServerConfiguration.getIntInput;

//import static robot_worlds_13.server.configuration.ServerConfiguration.getIntInput;

//import static robot_worlds_13.server.Server.port;

public class Client {
    /**
     * Colors for the terminal.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    
    private static final String PORT_REGEX = "\\b([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])\\b";
    private static final String IP_REGEX = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
    static String localAddress = "localhost";
    static String serverIpAddress = "20.20.15.94";
    public static Socket socket;
    public static PrintStream out;
    public static BufferedReader in;
    static Scanner line = new Scanner(System.in);
    public static String robotName;

    static private Gson gson = new Gson();

    /**
     * The main method is the entry point of the application.
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(ANSI_CYAN + "       " +
                "  __\n" +
                " _(\\    |@@|\n" +
                "(__/\\__ \\--/ __\n" +
                "   \\___|----|  |   __\n" +
                "       \\ }{ /\\ )_ / _\\\n" +
                "       /\\__/\\ \\__O (__      --WELCOME TO ROBOT WORLDS-- \n" +
                "      (--/\\--)    \\__/\n" +
                "      _)(  )(_\n" +
                "     `---''---`");


        int port = getIntInput(scanner, "Port Number of Server: ", 1024, 65535);
        System.out.println("Here is the port: " + port);
        // connecting to server
        String address = "";
        if (args.length == 0) {
            address = localAddress;
            try {
                socket = new Socket(address, port);
            } catch (Exception ex) {
                System.out.println("Failed to connect to the server using the specified IP address.");
                System.out.println("Resorting to localhost...\n");
                try {
                    socket = new Socket(address, port);
                } catch (Exception e) {
                    System.out.println("Failed to connect to localhost.");
                    System.out.println("None of the known servers are running");
                    System.out.println("Exiting program...\n");
                    System.exit(0);
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

            port = Integer.parseInt(args[1]);
            address =args[0].toLowerCase();
            try {
                socket = new Socket(address, port);

            } catch (Exception ex) {
                System.out.println(port);
                System.out.println("Failed to connect to the server using the specified IP address.");
                System.exit(0);
            }
        }
        

        try {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader( new InputStreamReader(socket.getInputStream()));

//            out = new DataOutputStream(socket.getOutputStream());
//            in = new DataInputStream(socket.getInputStream());
            
            String response;
            String potentialRobotName = "";

            
         // Pass the robot name to the showRobot method
            

            while (true) {
                // try to launch robot
                response = ClientProtocol.jsonResponseUnpacker(in.readLine());

                if (response.contains("Successfully launched")) {
                    robotName = potentialRobotName;
                    int width = Integer.parseInt(response.split(" ")[3]);
                    int height = Integer.parseInt(response.split(" ")[5]);
                    
                    System.out.println(ANSI_GREEN + "Launch GUI?");
                    String guiResponse = line.nextLine();
                    System.out.println("--------------------------------------------------------------\n");
                    System.out.println(ANSI_RESET);
                    
                    if (guiResponse.toLowerCase().equals("yes") || guiResponse.toLowerCase().equals("y")) {
                        Main main = new Main(width, height, address, port);
                        main.setVisible(true);
                    }
                    
                    break;
                }

                System.out.println("--------------------------------------------------------------");
                System.err.println(ANSI_GREEN + response );

                if (response.contains("Could not parse arguments") || response.contains("Unsupported command") || 
                response.contains("Connected successfully to") || response.contains("Too many of you in this world")) {
                    System.out.println(ANSI_CYAN + "Launch a robot!, Hint use 'launch make robot_name'");
                    System.out.println(ANSI_GREEN + "Please enter a valid make. Options are: Pistol, Riffle, Sniper");
                    // get input
                    String command = line.nextLine();

                    // save name
                    String[] potentialRobotNameArray = command.split(" ");
                    
                    if (potentialRobotNameArray.length > 2) {
                        potentialRobotName = potentialRobotNameArray[2];
                    }

                    Map<String, Object> formattedCommand;
                    if (!isValidLaunch(command)) {
                        formattedCommand = ClientProtocol.jsonRequestBuilder("launch");
                        sendJsonRequest(formattedCommand);
                        continue;
                    }

                    ArrayList<Object> attributes = getAttributes (potentialRobotNameArray[1]);

                    try {
                        formattedCommand = ClientProtocol.jsonRequestBuilder(potentialRobotName, potentialRobotNameArray[1], attributes);
                    } catch (Exception e) {
                        formattedCommand = ClientProtocol.jsonRequestBuilder("launch");
                        sendJsonRequest(formattedCommand);
                        System.out.println(ANSI_RESET);
                        continue;
                    }
                    // send to server as json
                    sendJsonRequest(formattedCommand);
                    System.out.println(ANSI_RESET);
                }
            }

            while (true) {
                // get server messages
                
                response = ClientProtocol.jsonResponseUnpacker(in.readLine());

                if (response.contains("GUI")) {
                    continue;
                }
                
                if (response.startsWith("What")) {
                    System.out.println("--------------------------------------------------------------");
                }

                if (response.contains("ERROR")) {
                    System.out.println(ANSI_RED + response);
                } else if (response.contains("What")) {
                    System.out.println(ANSI_GREEN + response);
                } else {
                    System.out.println(ANSI_CYAN + response);
                } 

                if (response.contains("Shutting down")) {
                    break;
                } else if (response.contains("Server shutting down...")) {
                    break;
                }
                
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
            System.out.println("Client closed connection");
            out.flush();
            out.close();
            System.exit(0);
        }
        catch (EOFException e) {
            System.out.println("Game over");
            System.exit(0);

        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                e.printStackTrace();
                out.println("off");
                out.flush();
                out.close();
                
            } catch (Exception i) {
                System.out.println("Connection problem encountered: Server is down or you do not an active internet connection");
                System.exit(0);
            }
        }

        
    }

    static public void sendJsonRequest(Map<String, Object> commandDetails) {
        if (commandDetails.get("robot") == null) {
            commandDetails.put("robot", robotName);  // Add robot name to the command details
        }
        
        String jsonRequest = gson.toJson(commandDetails);
        
        System.out.println("Sending command: " + jsonRequest);
        System.out.println("--------------------------------------------------------------");
        
        try {
            out.println(jsonRequest);
            out.flush();
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

    static public boolean isValidLaunch (String commandGiven) {
        try {
            String[] parts = commandGiven.trim().split("\\s+", 3);
            if (!parts[0].equalsIgnoreCase("launch")) {
                return false;
            } else if (!Arrays.asList("pistol", "riffle", "sniper").contains(parts[1].toLowerCase())) {
                return false;
            } else if (parts[2] == null || parts[2].isEmpty()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static public ArrayList<Object> getAttributes (String make) {
        ArrayList<Object> attributes = new ArrayList<>();
        switch (make.toLowerCase()) {
            case "pistol":
                attributes.add(50);
                attributes.add(50);
                break;
            case "riffle":
                attributes.add(200);
                attributes.add(200);
                break;
            case "sniper":
                attributes.add(3);
                attributes.add(5);
                break;
            default:
                break;
        }

        return attributes;
    }
}
