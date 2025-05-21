package game;

public class Constants {
    // Screen width and height
    public static final int SCREEN_W = 1920;
    public static final int SCREEN_H = 1080;
    // factors of 1920: 1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 16, 20, 24, 30, 32, 40, 48, 60, 64, 80, 96, 120, 128, 160, 192, 240, 320, 384, 480, 640, 960, 1920
    public static final int W_FRAC = 60; // screen will be divided up into W_FRAC segments by width
    // factors of 1080: 1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 18, 20, 24, 27, 30, 36, 40, 45, 54, 60, 72, 90, 108, 120, 135, 180, 216, 270, 360, 540, 1080
    public static final int H_FRAC = 5; // screen will be divided up into H_FRAC segments by height
    // Calculated values by W_FRAC and H_FRAC;
    public static final int U_WIDTH = SCREEN_W / W_FRAC;
    public static final int U_HEIGHT = SCREEN_H / H_FRAC;

    // values for hitting physics
    public static final double RANDOM_EFFECT = 5;

    // values for bouncing balls
    public static final double RANDOM_SPEED = 5;

    // only set to true for debugging purposes
    public static final boolean RENDER_EXTRA_BALLS = false; // to reduce visual clutter

    public static final int PADDLE_BOOST_FRAMES = 20; // number of frames the paddle will move faster for
}
