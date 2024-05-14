package client;

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
    static String address = "localhost";
    static Socket sThisClient;
    static DataOutputStream dout;
    static DataInputStream din;
    static Scanner line = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            sThisClient = new Socket(address, 2201);
            dout = new DataOutputStream(sThisClient.getOutputStream());
            din = new DataInputStream(sThisClient.getInputStream());

            while (true) {
                // get server messages
                String response = din.readUTF();
                
                // print message to this client
                System.out.println(response);
                
                if (response.contains("Shutting down")) {
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
            
            dout.flush();
            dout.close();
            sThisClient.close();
        }
        catch (Exception e) {
            try {
                
                dout.writeUTF("off");
                dout.flush();
                dout.close();
                sThisClient.close();
            } catch (Exception i) {
                System.out.println("Connection problem to the server encountered");
            }
        }
    }
}
