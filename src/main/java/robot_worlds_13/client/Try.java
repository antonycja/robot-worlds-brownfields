package robot_worlds_13.client;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel implementation that represents the game panel with a centered
 * play area and optional Cartesian coordinate axes.
 * The panel has a preferred size of 500x900 pixels, with a 50-pixel margin on
 * all sides.
 * The play area within the margins has dimensions of 400x800 pixels.
 * The panel is set to be double-buffered and focusable.
 */
class GamePanel1 extends JPanel {

    public GamePanel1() {
        // Add 100 to both width and height to account for margins on both sides
        this.setPreferredSize(new Dimension(500, 900));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the border and play area within the centered margins
        int xOffset = 50; // 50 pixels margin on the left and right
        int yOffset = 50; // 50 pixels margin on top and bottom

        // Play area dimensions and positions with offset
        int playAreaWidth = 400;
        int playAreaHeight = 800;

        // Setting color for the play area border (or could use a different color for
        // aesthetics)
        g.setColor(Color.WHITE);
        g.drawRect(xOffset, yOffset, playAreaWidth, playAreaHeight);

        // Optional: Draw Cartesian coordinates axes within the play area
        g.drawLine(xOffset + playAreaWidth / 2, yOffset, xOffset + playAreaWidth / 2, yOffset + playAreaHeight);
        g.drawLine(xOffset, yOffset + playAreaHeight / 2, xOffset + playAreaWidth, yOffset + playAreaHeight / 2);

        // Label the center if necessary (adjust position as needed)
        g.drawString("Center (0,0)", xOffset + playAreaWidth / 2 - 40, yOffset + playAreaHeight / 2 - 10);
    }
}

// Database.Main or test setup
public class Try {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game With Margins");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new GamePanel1());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
