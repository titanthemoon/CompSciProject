package game.display;

import game.Constants;
import game.engine.Direction;
import game.engine.PhysicsObject;
import game.pong.Ball;
import game.pong.Paddle;
import game.util.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DisplayFrame extends JPanel {

    private Ball ball;
    private Paddle playerPaddle;
    private Paddle aiPaddle;
    private PhysicsObject[] physicsObjects; // 0-1 are nomral balls, 1-2 are bouncy balls. Hitting any ball will increase paddle speed
    private Vector2d resetVel;
    private int leftSpeed, rightSpeed;
    private boolean up1, down1;
    private boolean up2, down2;
    private boolean twoPlayer;
    private boolean running;
    private int leftScore, rightScore;
    private boolean leftWin;

    public DisplayFrame() {
        ball = new Ball();
        resetVel = new Vector2d(15, 15);

        // initialize physics object array
        physicsObjects = new PhysicsObject[4];

        // initialize the four speed-boosting balls
        physicsObjects[0] = new Ball(); physicsObjects[0].setColor(Color.BLUE);
        physicsObjects[1] = new Ball(); physicsObjects[1].setColor(Color.BLUE);
        physicsObjects[2] = new PhysicsObject(new Vector2d(Constants.SCREEN_W / 2, Constants.SCREEN_H / 2), Constants.U_WIDTH, Constants.U_WIDTH);
        physicsObjects[2].setColor(Color.RED);
        physicsObjects[3] = new PhysicsObject(new Vector2d(Constants.SCREEN_W / 2, Constants.SCREEN_H / 2), Constants.U_WIDTH, Constants.U_WIDTH);
        physicsObjects[3].setColor(Color.RED);

        // all four balls start off with random velocity in different directions
        physicsObjects[0].setVel(new Vector2d(Math.random() * Constants.RANDOM_SPEED, Math.random() * Constants.RANDOM_SPEED));
        physicsObjects[1].setVel(new Vector2d(Math.random() * Constants.RANDOM_SPEED * -1, Math.random() * Constants.RANDOM_SPEED * -1));
        physicsObjects[2].setVel(new Vector2d(Math.random() * Constants.RANDOM_SPEED, 0));
        physicsObjects[3].setVel(new Vector2d(Math.random() * Constants.RANDOM_SPEED * -1, 0));

        // the two bouncy balls experience gravity
        physicsObjects[2].setAcc(new Vector2d(0, 0.5));
        physicsObjects[3].setAcc(new Vector2d(0, 0.5));


        for (PhysicsObject o : physicsObjects) {
            System.out.println(o.toString());
        }

        ball.setVel(resetVel);
        aiPaddle = new Paddle(Constants.U_WIDTH, 15); // left side
        playerPaddle = new Paddle(Constants.SCREEN_W - 2 * Constants.U_WIDTH, 15); // right side

        leftSpeed = 0;
        rightSpeed = 0;

        up1 = false;
        down1 = false;
        up2 = false;
        down2 = false;
        twoPlayer = false;

        leftWin = false;

        running = true;
        leftScore = 0;
        rightScore = 0;

        setFocusable(true);

        setupInputMap();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // draw the black background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 48));
        if (running) {
            // render the game objects
            ball.render(g2);
            aiPaddle.render(g2);
            playerPaddle.render(g2);
            if (Constants.RENDER_EXTRA_BALLS) {
                for (PhysicsObject o : physicsObjects) {
                    o.render(g2);
                }
            }
            g2.setColor(Color.WHITE);
            // render the score
            g2.drawString(Integer.toString(leftScore), getWidth() / 2 - 58, 48);
            g2.drawString(Integer.toString(rightScore), getWidth() / 2 + 28, 48);
            g2.fillRect(getWidth() / 2 - 1, 0, 2, getHeight()); // center line
        } else {
            // end screen at the end that displays who won
            g2.setColor(Color.WHITE);
            if (leftWin) {
               g2.drawString("LEFT SIDE WINS", 0, 48);
            } else {
                g2.drawString("RIGHT SIDE WINS", 0, 48);
            }
        }
    }

    public void startGameLoop() {
        // create a single-use timer for a 3-second pause
        new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((Timer) actionEvent.getSource()).stop(); // stop the timer before it loops
                run(); // start the game loop
            }
        }).start();
    }

    private void run() {
        // ensure constant framerate using timers
        Timer gameTimer = new Timer(16, null); // 16ms = 60 fps/ups
        gameTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // sets player paddle direction depending on which keys are held down
                if (up1 && !down1) { playerPaddle.setDir(Direction.UP); }
                else if (!up1 && down1) { playerPaddle.setDir(Direction.DOWN); }
                else { playerPaddle.setDir(Direction.NONE); }

                if (twoPlayer) {
                    // same as right paddle controls
                    if (up2 && !down2) { aiPaddle.setDir(Direction.UP); }
                    else if (!up2 && down2) { aiPaddle.setDir(Direction.DOWN); }
                    else { aiPaddle.setDir(Direction.NONE); }
                } else {
                    // ai paddle always moves toward the ball
                    // its speed will be faster/slower depending on the score difference
                    if (rightScore - leftScore >= 4) { aiPaddle.setMaxVel(20); }
                    else if (leftScore - rightScore >= 4) { aiPaddle.setMaxVel(10); }
                    else { aiPaddle.setMaxVel(15); }

                    if (aiPaddle.getPos().getY() > ball.getPos().getY()) { aiPaddle.setDir(Direction.UP); }
                    else if (aiPaddle.getPos().getY() < ball.getPos().getY()) { aiPaddle.setDir(Direction.DOWN); }
                }

                // update paddle positions
                playerPaddle.update();
                aiPaddle.update();

                // speed-boosting ball stuff
                for (PhysicsObject o : physicsObjects) {
                    o.update();

                    if (playerPaddle.checkCol(o))
                        rightSpeed = Constants.PADDLE_BOOST_FRAMES;

                    if (aiPaddle.checkCol(o))
                        leftSpeed = Constants.PADDLE_BOOST_FRAMES;
                }

                // leftSpeed/rightSpeed slowly tick down each cycle until they hit 0
                // update the velocity of the paddles if nessecary during speed boost
                if (leftSpeed > 0) {
                    aiPaddle.setMaxVel(20);
                    leftSpeed--;
                } else {
                    aiPaddle.setMaxVel(15);
                }
                // same thing for other paddle
                if (rightSpeed > 0) {
                    playerPaddle.setMaxVel(20);
                    rightSpeed--;
                } else {
                    playerPaddle.setMaxVel(15);
                }

                // checks collisions between the ball and the paddles
                // sets the ball's position to directly in front of the paddle to prevent bugs
                // inverts the velocity, and also adds a random amount of velocity to the ball, determined by RANDOM_EFFECT
                if (ball.checkCol(playerPaddle)) {
                    ball.getPos().setX(Constants.SCREEN_W - 3 * Constants.U_WIDTH);
                    ball.setVel(new Vector2d(ball.getVel().getX() + (Constants.RANDOM_EFFECT * Math.random()), ball.getVel().getY() + (Constants.RANDOM_EFFECT * Math.random() * Math.signum(ball.getVel().getY()))));
                    ball.invertVelX();
                }

                if (ball.checkCol(aiPaddle)) {
                    ball.getPos().setX(2 * Constants.U_WIDTH);
                    ball.setVel(new Vector2d(ball.getVel().getX() - (Constants.RANDOM_EFFECT * Math.random()), ball.getVel().getY() + (Constants.RANDOM_EFFECT * Math.random() * Math.signum(ball.getVel().getY()))));
                    ball.invertVelX();
                }

                // the ball always resets moving in direction of the player who won the previous point
                // there is a 1 second pause after scoring, and paddles are reset along with the ball
                switch (ball.update()) {
                    case Direction.LEFT -> {
                        rightScore++;
                        ball.setResetVel(resetVel);
                        ball.reset();
                        aiPaddle.reset();
                        playerPaddle.reset();
                        repaint();
                        sleep(1000);
                    }
                    case Direction.RIGHT -> {
                        leftScore++;
                        ball.setResetVel(Vector2d.scaleMult(resetVel, -1));
                        ball.reset();
                        aiPaddle.reset();
                        playerPaddle.reset();
                        repaint();
                        sleep(1000);
                    }
                }

                // draw everything to the screen
                repaint();

                // check for wins
                if (leftScore >= 7 || rightScore >= 7) {
                    running = false;
                }

                if (!running) {
                    gameTimer.stop(); // stop loop at end of game
                    leftWin = leftScore > rightScore; // check the winning player
                    repaint(); // display the winning player to the screen
                }
            }
        });
        gameTimer.start(); // start game loop timer
    }

    // Used to pause when an imprecise amount of time is needed
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setupInputMap() {
        // All of this is to setup keys, most of it is juts repeated code
        // I know there's a simpler method of creating to input but it kept breaking
        // All it does is keep track of which keys are currently held down (With the exception of the final one which only detects when T is released)
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed UP"), "upPressed");
        getActionMap().put("upPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                up1 = true;
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released UP"), "upReleased");
        getActionMap().put("upReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                up1 = false;
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed DOWN"), "downPressed");
        getActionMap().put("downPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                down1 = true;
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released DOWN"), "downReleased");
        getActionMap().put("downReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                down1 = false;
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed S"), "sPressed");
        getActionMap().put("sPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                down2 = true;
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released S"), "sReleased");
        getActionMap().put("sReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                down2 = false;
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed W"), "wPressed");
        getActionMap().put("wPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                up2 = true;
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released W"), "wReleased");
        getActionMap().put("wReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                up2 = false;
            }
        });
        // 2 player mode
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released T"), "tReleased");
        getActionMap().put("tReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ensures both paddles are equal when entering/exiting two player mode so that game is fair
                if (!playerPaddle.equals(aiPaddle)) {
                    aiPaddle = new Paddle(Constants.U_WIDTH, 15);
                    System.out.println("Resetting Left Paddle");
                }

                twoPlayer = !twoPlayer;
            }
        });
    }
}
