package game;

import control.Graphics;
import game.Level;

import java.util.concurrent.ThreadLocalRandom;

public class Boss1m extends SimpleMoving implements Boss{
    protected int hp = 15;
    private int alarm = 100;

    private static int getYspeed() {
        int ys = 8*32;
        if (ThreadLocalRandom.current().nextInt(0, 2) != 0)
            ys = -ys;
        return ys;
    }

    public Boss1m(int x, int y) {
        super(x, y, 150, 100, 0, getYspeed());
    }

    public void onCollisionSoftWall(Wall other) {
        onCollisionWall(other);
    }

    public void onCollisionHardWall(Wall wall) {
        onCollisionWall(wall);
        hurt();
    }

    public boolean isDeath() {
        return hp < 0;
    }

    public void hurt() {
        hp--;
        if (hp < 0)
            Game.getInstance().levelComplete();
        Level.getInstance().getPlayer().addBullet(3);
    }

    public void update() {
        super.update();
        if (alarm == 0) {
            alarm = ThreadLocalRandom.current().nextInt(25, 60);
            WallManager.getWallManager().addDwall(new DynamicWall(x - 32, y + (h - 32) / 2, 32,
                    32, (byte)0, -8*32, 0));
        }
        alarm--;
    }

    public void draw() {
        Graphics.drawColorRect(x, y, w, h, 1.f, 1.f, 1.f);
    }
}