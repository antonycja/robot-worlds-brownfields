package robot_worlds_13.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import robot_worlds_13.server.*;
import robot_worlds_13.server.robot.maze.SimpleMaze;
import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.TextWorld;

/**
 * The `Play` class is the main entry point for the robot worlds application. It
 * handles the user interaction, creates the robot and world, and manages the
 * game loop.
 *
 * The class provides the following functionality:
 * - Prompts the user to name the robot
 * - Creates a `TextWorld` with a `SimpleMaze` and a `Robot` instance
 * - Displays the initial obstacles and robot status
 * - Enters a game loop where the user can provide commands to the robot
 * - Handles the robot's commands and updates the robot's status accordingly
 * - Continues the game loop until the robot is unable to continue
 *
 * The `getInput()` method is a utility function that prompts the user for input
 * and ensures a non-blank response is provided.
 */
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

        // set and print obstacles
        world.showObstacles();

        // print first status
        System.out.println(robot.toString());

        robot_worlds_13.server.robot.Command command;
        boolean shouldContinue = true;
        do {
            String instruction = getInput(robot.getName() + "> What must I do next?").strip().toLowerCase();
            try {

                command = robot_worlds_13.server.robot.Command.create(instruction);
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
