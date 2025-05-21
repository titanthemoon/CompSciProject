package game.util;

public class Vector2d {
    // a two dimensional vector, stores 2 doubles and allows them to be changed
    private double x, y;
    public Vector2d() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public double getX() { return x; }

    public double getY() { return y; }
    // unused
    public double getMagnitude() {
        return Math.sqrt((x * x) + (y * y));
    }
    // works the same way as in normal math
    public static Vector2d add(Vector2d u, Vector2d v) {
        return new Vector2d(u.getX() + v.getX(), u.getY() + v.getY());
    }

    public static Vector2d subtract(Vector2d u, Vector2d v) {
        return new Vector2d(u.getX() - v.getX(), u.getY() - v.getY());
    }

    public static Vector2d scaleMult(Vector2d v, double t) {
        return new Vector2d(v.getX() * t, v.getY() * t);
    }

    public String toString() {
        return "2-Dim Vector, Double, x:" + x + " y: " + y;
    }

    public boolean equals(Vector2d other) {
        if (x == other.getX() && y == other.getY())
            return true;

        return false;
    }
}
