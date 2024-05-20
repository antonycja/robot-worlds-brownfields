package robot_worlds_13.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static String localAddress = "localhost";
    static String serverIpAddress = "20.20.15.94";
    static Socket sThisClient;
    static DataOutputStream dout;
    static DataInputStream din;
    static Scanner line = new Scanner(System.in);

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
        System.out.println("Here is a list of things I can do:\n"+
                "OFF  - Shut down robot\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move back by specified number of steps, e.g. 'BACK 10'\n" +
                "LEFT - turn left by 90 degrees\n"+
                "RIGHT - turn right by 90 degrees\n");


        try {
            sThisClient = new Socket(serverIpAddress, 2204);
        } catch (Exception ex) {
            System.out.println("Failed to connect to the server using the specified IP address.");
            System.out.println("Resorting to localhost...\n");
            try {
                sThisClient = new Socket(localAddress, 2204);
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

            while (true) {
                // Get server messages
                String response = din.readUTF();

                // Print message to this client
                System.out.println(response);

                if (response.contains("Shutting down")) {
                    break;
                } else if (response.contains("Server shutting down...")) {
                    break;
                }

                // If the message requires a response prompt current client for input
                // and send the command
                if (response.startsWith("What")) {
                    String command = line.nextLine();
                    dout.writeUTF(command);
                    dout.flush();
                }
            }

            // Close this client
            dout.flush();
            dout.close();
        }
        catch (Exception e) {
            try {
                dout.writeUTF("off");
                dout.flush();
                dout.close();
            } catch (Exception i) {
                System.out.println("Connection problem encountered: Server is down or you do not an active internet connection");
            }
        }
    }
}
