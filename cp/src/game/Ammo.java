package game;

import control.Graphics;

public class Ammo extends MovingSolidActor{
    int bulletCount;

    public Ammo(int x, int y, int b) {
        super(x, y, 16, 16, (byte)0, -10*32, 0);
        bulletCount = b;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    @Override
    public void draw() {
        Graphics.drawColorRect(x, y, w, h, 0.5f, 0.5f, 0.5f);
    }
}
