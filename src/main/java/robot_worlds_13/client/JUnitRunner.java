package robot_worlds_13.client;

import java.io.BufferedReader;
import  java.io.InputStreamReader;

public class JUnitRunner {
    public static void main(String[] args) {
        try{
            String[] testClasses = {
                    "za.co.robotworlds.server.acceptanceTests.ForwardCommandTests",
                    "za.co.robotworlds.server.acceptanceTests.LookCommandTest",
                    "za.co.robotworlds.server.acceptanceTests.StateTests",
                    "za.co.robotworlds.server.acceptanceTests.LaunchRobotTests"
            };
            for (String testClass : testClasses){
                String[] command = {
                        "mvn",
                        "test",
                        "-Dtest=" + testClass
                };

                // Create a ProcessBuilder
                ProcessBuilder processBuilder = new ProcessBuilder(command);

                // Start the process
                Process process = processBuilder.start();

                // Read the output from the process
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // Read the error output from the process
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }

                // Wait for the process to complete and get the exit value
                int exitCode = process.waitFor();
                System.out.println("JUnit test process for " + testClass + " exited with code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
