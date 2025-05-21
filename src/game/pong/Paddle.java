package game.pong;

import game.Constants;
import game.engine.ColBehavior;
import game.engine.Direction;
import game.engine.PhysicsObject;
import game.util.Vector2d;

/**
 * Paddles only have a single velocity variable since they can only move up / down.
 * The player's paddle will always move at maxVel, the ai's paddle speed will vary
 * The paddle is a rectangle, whose widtrh width is a single unit dictated by W_FRAC and height is a single unit of H_FRAC
 */
public class Paddle extends PhysicsObject {
    private Direction dir;
    private double maxVel;

    public Paddle() {
        super(new Vector2d(0, Constants.SCREEN_H / 2), Constants.U_WIDTH, Constants.U_HEIGHT);
        setColBehavior(ColBehavior.STOP);
        dir = Direction.NONE;
        maxVel = 15;
    }

    public Paddle(int x, double maxVel) {
        super(new Vector2d(x, Constants.SCREEN_H / 2), Constants.U_WIDTH, Constants.U_HEIGHT);
        setColBehavior(ColBehavior.STOP);
        dir = Direction.NONE;
        this.maxVel = maxVel;
    }

    public Direction getDir() { return dir; }
    public void setDir(Direction dir) { this.dir = dir; }

    public double getMaxVel() { return maxVel; }
    public void setMaxVel(double maxVel) { this.maxVel = maxVel; }

    public Direction update() {
        switch (dir) {
            case Direction.UP:
                getPos().setY(getPos().getY() - (maxVel));
                break;
            case Direction.DOWN:
                getPos().setY(getPos().getY() + (maxVel));
                break;
            default: break;
        }

        return checkWallCols();
    }
    public void reset() {
        getPos().setY(Constants.SCREEN_H / 2);
        dir = Direction.NONE;
    }

    public String toString() {
        String out = super.toString() + "\n";
        out += "Paddle Components:\n";
        out += "Max Velocity: " + maxVel + "\n";
        out += "Direction: " + dir;
        return out;
    }

    public boolean equals(Paddle other) {
        if (super.equals(other) && maxVel == other.getMaxVel())
            return true;

        return false;
    }
}