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
}