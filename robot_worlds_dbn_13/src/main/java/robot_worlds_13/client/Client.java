package robot_worlds_13.client;

/*
 * This class acts as the user's interface to the program
 * it will employ charecteristics from play
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
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

            while (true) {
                // get server messages
                String response = din.readUTF();
                
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
                    String command = line.nextLine();
                    dout.writeUTF(command);
                    dout.flush();
                }
            }

            // close this client
            dout.flush();
            dout.close();
        }
        catch (Exception e) {
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
}
