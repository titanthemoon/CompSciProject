package game.engine;

import game.util.Vector2d;

import game.Constants;

import java.awt.*;

/**
 * Each Physics Object is a rectangle which is exists inside of a domain or bounding box
 * The domain is an outer rectangle starting at (0,0) and ending at (maxR, maxB)
 * ColBehavior determines how a physics object interacts with its domain
 * There is a built in method for detecting collision between physics objects, but it is the responsibility of other code to deal with these collisions
 * The update method updates position and velocity, and handles collisions with the domain
 */

public class PhysicsObject {
    private Vector2d pos; // pixels
    private Vector2d vel; // pixels / tick
    private Vector2d acc; // pixels / tick^2
    private int colW, colH;
    private int maxR, maxB;
    private ColBehavior colBehav;
    private Color color;

    public PhysicsObject() {
        pos = new Vector2d();
        vel = new Vector2d();
        acc = new Vector2d();
        colW = 0; colH = 0;
        setBounds(Constants.SCREEN_W, Constants.SCREEN_H);
        colBehav = ColBehavior.REFLECT;
        color = Color.WHITE;
    }

    public PhysicsObject(Vector2d pos, int colW, int colH) {
        this.pos = pos;
        vel = new Vector2d();
        acc = new Vector2d();
        this.colW = colW; this.colH = colH;
        setBounds(Constants.SCREEN_W, Constants.SCREEN_H);
        colBehav = ColBehavior.REFLECT;
        color = Color.WHITE;
    }
    // getters and setters
    public void setBounds(int maxR, int maxB) {
        this.maxR = maxR;
        this.maxB = maxB;
    }

    public int getMaxR() { return maxR; }
    public int getMaxB() { return maxB; }

    public Vector2d getPos() { return pos; }
    public Vector2d getVel() { return vel; }
    public Vector2d getAcc() { return acc; }

    public void setPos(Vector2d pos) { this.pos = pos; }
    public void setVel(Vector2d vel) { this.vel = vel; }
    public void setAcc(Vector2d acc) { this.acc = acc; }

    public int getColW() { return colW; }
    public int getColH() { return colH; }

    public void setColW(int colW) { this.colW = colW; }
    public void setColH(int colH) { this.colH = colH; }

    public ColBehavior getColBehavior() { return colBehav; }
    public void setColBehavior(ColBehavior colBehav) { this.colBehav = colBehav; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public void invertVelX() { vel.setX(-1 * vel.getX()); }
    public void invertVelY() { vel.setY(-1 * vel.getY()); }

    public boolean isPointInside(double x, double y) {
        if (x <= pos.getX() + colW && x >= pos.getX())
            if (y <= pos.getY() + colH && y >= pos.getY())
                return true;

        return false;
    }

    public boolean checkCol(PhysicsObject other) {
        // checks if objects are colliding using an axis-aligned bounding box (checks if there's overlap between the two rectangles)
        return pos.getX() < other.getPos().getX() + other.getColW() &&
                pos.getX() + colW > other.getPos().getX() &&
                pos.getY() < other.getPos().getY() + other.getColH() &&
                pos.getY() + colH > other.getPos().getY();
    }
    public Direction update() { // will return direction of domain collisions
        // position and velocity
        pos = Vector2d.add(pos, vel);
        vel = Vector2d.add(vel, acc);

        // collisions (many if and switch case statements in order to handle every scenario)
        return checkWallCols();
    }
    // will return side of box that object hits
    public Direction checkWallCols() {
        // checks whether the object has left its bounding box
        if (pos.getX() < 0) {
            if (colBehav != ColBehavior.IGNORE) {
                pos.setX(0);
                switch (colBehav) {
                    case STOP:
                        vel.set(0, 0);
                        break;
                    case REFLECT:
                        invertVelX();
                        break;
                }
            }
            return Direction.LEFT;
        }
        if (pos.getY() < 0) {
            if (colBehav != ColBehavior.IGNORE) {
                pos.setY(0);
                switch (colBehav) {
                    case STOP:
                        vel.set(0, 0);
                        break;
                    case REFLECT:
                        invertVelY();
                        break;
                }
            }
            return Direction.UP;
        }
        if (pos.getX() + colW > maxR) {
            if (colBehav != ColBehavior.IGNORE) {
                pos.setX(maxR - colW);
                switch (colBehav) {
                    case STOP:
                        vel.set(0, 0);
                        break;
                    case REFLECT:
                        invertVelX();
                        break;
                }
            }
            return Direction.RIGHT;
        }
        if (pos.getY() + colH > maxB) {
            if (colBehav != ColBehavior.IGNORE) {
                pos.setY(maxB - colH);
                switch (colBehav) {
                    case STOP:
                        vel.set(0, 0);
                        break;
                    case REFLECT:
                        invertVelY();
                        break;
                }
            }
            return Direction.DOWN;
        }

        return Direction.NONE;
    }

    public void render(Graphics2D g2) {
        // draws the object to the screen
        g2.setColor(color);
        g2.fillRect((int) pos.getX(), (int) pos.getY(), colW, colH);
    }

    public String toString() {
        String out =  "Physics Object, " + color.toString() + ", " + colBehav + "\n";
        out += "Position: " + pos.toString() + "\n";
        out += "Velocity: " + vel.toString() + "\n";
        out += "Acceleration: " + acc.toString() + "\n";
        out += "Bounds: x: " + maxR + " y: " + maxB + "\n";
        out += "Size: Width:" + colW + " Height: " + colH;
        return out;
    }
    // ignores position, velocity, and acceleration but cares about everything else
    public boolean equals(PhysicsObject other) {
        if (color.equals(other.getColor()) && colBehav == other.colBehav)
            if (colW == other.getColW() && colH == other.getColH())
                if (maxR == other.getMaxR() && maxB == other.getMaxB())
                    return true;

        return false;
    }
}
