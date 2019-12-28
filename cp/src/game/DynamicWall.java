package game;

import control.Graphics;

public class DynamicWall extends SimpleMoving implements Wall {
    public static final int PLAYER_NOT_KILLER = 1 << 0;
    public static final int ENEMY_KILLER = 1 << 1;
    private byte state;

    public DynamicWall(int x, int y, int w, int h, byte state, int xspeed, int yspeed) {
        super(x, y, w, h, xspeed, yspeed);
        this.state = state;
    }

    public void collisionWall(Wall other) {
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

    public boolean isPlayerKiller() {
        return (state & PLAYER_NOT_KILLER) == 0;
    }

    public boolean isEnemyKiller() {
        return (state | ENEMY_KILLER) != 0;
    }

    public void draw() {
        if (isPlayerKiller())
            Graphics.drawColorRect(x, y, w, h, 1.f, 0.f, 0.f);
        else
            Graphics.drawColorRect(x, y, w, h, 0.f, 1.f, 0.f);
    }
}
