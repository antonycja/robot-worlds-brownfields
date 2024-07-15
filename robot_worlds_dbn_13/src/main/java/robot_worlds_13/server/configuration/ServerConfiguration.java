package robot_worlds_13.server.configuration;
import java.util.Scanner;

public class ServerConfiguration {
    // ANSI escape codes for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static int portNum;

    public ServerConfiguration() {
        Scanner scanner = new Scanner(System.in);
        int portNum = getIntInput(scanner, ANSI_CYAN + "Enter the PORT number you want to host your server: ", 1024, 65535);

    }

    private static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.println(ANSI_RED + "Input must be between " + min + " and " + max + "." + ANSI_RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "Invalid input, enter a numeric value." + ANSI_RESET);
            }
        }
        return value;
    }

}
