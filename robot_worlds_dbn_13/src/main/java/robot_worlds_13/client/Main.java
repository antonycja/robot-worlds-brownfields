package robot_worlds_13.client;

import javax.swing.*;

public class Main extends JFrame {

    public Main(int width, int height, String address, int port) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Robot World");

        GamePanel gamePanel = new GamePanel(width, height, address, port);
        window.add(gamePanel);
        window.pack(); // Ensure the window is sized correctly based on its content

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }


    public void showRobot(String robotName) {
        // Update GUI to show selected robot
        System.out.println("Showing robot: " + robotName);
        // ... update GUI components ...
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    // public static void main(String[] args) {
    //     new Main();
    // }
}