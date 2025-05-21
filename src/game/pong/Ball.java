package game.pong;

import game.Constants;
import game.engine.PhysicsObject;
import game.util.Vector2d;

/**
 * A ball is a square with a side length being a sinle unit of W_FRAC
 */
public class Ball extends PhysicsObject {

    private Vector2d resetVel;

    public Ball() {
        super(new Vector2d(Constants.SCREEN_W / 2, Constants.SCREEN_H / 2), Constants.U_WIDTH, Constants.U_WIDTH);
    }

    public Vector2d getResetVel() { return resetVel; }
    public void setResetVel(Vector2d resetVel) { this.resetVel = resetVel; }

    public void reset() {
        setPos(new Vector2d(Constants.SCREEN_W / 2, Constants.SCREEN_H / 2));
        setVel(resetVel);
    }
}
