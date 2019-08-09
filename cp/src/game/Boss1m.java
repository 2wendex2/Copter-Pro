import game.Level;

import java.util.concurrent.ThreadLocalRandom;

public class Boss1m extends MovingSolidActor implements Boss{
    protected int hp = 15;
    protected int hpStart = 15;
    private int alarm = 100;
    private int xStart, yStart;

    private static int getYspeed() {
        int ys = 8*32;
        if (ThreadLocalRandom.current().nextInt(0, 2) != 0)
            ys = -ys;
        return ys;
    }

    public void restart() {
        x = xStart;
        y = yStart;
        hp = hpStart;
    }

    public Boss1m(int x, int y) {
        super(x, y, 150, 100, (byte)0, new Sprite(1.0f, 1.0f, 1.0f), 0, getYspeed());
        xStart = x;
        yStart = y;
    }

    public boolean isEnd() {
        return hp < 0;
    }

    public void bulletCollision(Level level, Bullet bullet) {
        hp--;
        level.addBulletCount(3);
    }

    public void update(Level level) {
        super.update();
        if (alarm == 0) {
            alarm = ThreadLocalRandom.current().nextInt(25, 60);
            level.addMovingSolid(new MovingSolidActor(x - 32, y + (h - 32) / 2, 32,
                    32, (byte)0, level.getSolidSprite(), -8*32, 0));
        }
        alarm--;
    }
}