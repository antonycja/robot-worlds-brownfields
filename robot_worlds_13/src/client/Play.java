package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import server.*;
import server.maze.SimpleMaze;
import server.world.AbstractWorld;
import server.world.TextWorld;

public class Play {
    static Scanner scanner;

    //
    static List<String> commandList = new ArrayList<>();

    public static void main(String[] args) {

        // get world type
        for (String arg : args) {
            if (arg.toLowerCase() == "text") {
                // do nothing for now
            }
        }

        scanner = new Scanner(System.in);

        String name = getInput("What do you want to name your robot?");
        System.out.println("Hello Kiddo!");

        // make world, set name
        AbstractWorld world = new TextWorld(new SimpleMaze());
        Robot robot = new Robot(name, world);

        //set and print obstacles
        world.showObstacles();

        // print first status
        System.out.println(robot.toString());

        server.Command command;
        boolean shouldContinue = true;
        do {
            String instruction = getInput(robot.getName() + "> What must I do next?").strip().toLowerCase();
            try {

                command = server.Command.create(instruction);
                shouldContinue = robot.handleCommand(command);

            } catch (IllegalArgumentException e) {
                robot.setStatus("Sorry, I did not understand '" + instruction + "'.");
            }
            System.out.println(robot);
        } while (shouldContinue);

    }

    private static String getInput(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();

        while (input.isBlank()) {
            System.out.println(prompt);
            input = scanner.nextLine();
        }
        return input;
    }
}
