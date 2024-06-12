package robot_worlds_13.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class NetworkInfo {

    public static String main(String[] args) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface iface : Collections.list(interfaces)) {
                // Filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // Checks for IPv4 addresses only
                    if (addr.getAddress().length == 4) {
                        // System.out.println(iface.getDisplayName() + " - " + addr.getHostAddress());
                        return addr.getHostAddress();
                    }
                }
            }

            return "";
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}