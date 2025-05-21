import game.display.DisplayFrame;
import game.Constants;

import javax.swing.*;
import java.awt.*;
/**
 * I was originally creating a simple game engine which pong was built on top of, but I switch to just developing pong part way through.
 * Because of this, the code is definently more messy than it needs to be
 * Even though this is technically the runner class, the actual class which does everything is game.display.DrawGame
 * all this class does is initialize the code, since putting the rest of the game inside main would be extremely messy
 */

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong"); // create the window
        Container pane = frame.getContentPane();

        DisplayFrame renderPanel = new DisplayFrame(); // create the game and add it to the JFrame
        pane.add(renderPanel); // add the game to the window
        frame.setSize(Constants.SCREEN_W, Constants.SCREEN_H); // set the window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ensure the code exists when window is closed
        frame.setVisible(true);

        pane.requestFocusInWindow();

        renderPanel.startGameLoop(); // start game
    }
}