package game;

public abstract class SimpleMoving extends PreciseRect {
    protected int ySpeed, xSpeed; //*32

    public SimpleMoving(int x, int y, int w, int h, int xspeed, int yspeed) {
        super(x, y, w, h);
        xSpeed = xspeed;
        ySpeed = yspeed;
        xOffset = x*32;
        yOffset = y*32;
    }

    public void update() {
        xOffset += xSpeed;
        yOffset += ySpeed;
        precise();
    }

    protected void onCollisionWall(Wall other) {
        if (xSpeed > 0)
            x = 2 * other.getX() - 2 * w - x;
        else if (xSpeed < 0)
            x = 2 * other.getX() + 2 * other.getW() - x;

        if (ySpeed > 0)
            y = 2 * other.getY() - y - 2 * h;
        else if (ySpeed < 0)
            y = 2 * other.getY() + 2 * other.getH() - y;

        xSpeed = -xSpeed;
        ySpeed = -ySpeed;
        xOffset = x*32;
        yOffset = y*32;
    }
}