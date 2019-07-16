public class MovingSolidActor extends SolidActor {
    private int ySpeed, xSpeed; //*32
    private int xOffset, yOffset;

    public MovingSolidActor(int x, int y, int w, int h, byte flags, Sprite sprite, int xspeed, int yspeed) {
        super(x, y, w, h, flags, sprite);
        xSpeed = xspeed;
        ySpeed = yspeed;
        xOffset = x*32;
        yOffset = y*32;
    }

    public void update() {
        xOffset += xSpeed;
        yOffset += ySpeed;
        x = xOffset / 32;
        y = yOffset / 32;
    }

    public void collisionSolidActor(SolidActor other) {
        if (xSpeed > 0)
            x = 2 * other.x - 2 * w - x;
        else if (xSpeed < 0)
            x = 2 * other.x + 2 * other.w - x;

        if (ySpeed > 0)
            y = 2 * other.y - y - 2 * h;
        else if (ySpeed < 0)
            y = 2 * other.y + 2 * other.h - y;

        xSpeed = -xSpeed;
        ySpeed = -ySpeed;
        xOffset = x*32;
        yOffset = y*32;
    }
}
