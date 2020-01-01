package game;

import control.Graphics;

public class DynamicAmmo extends SimpleMoving implements PlayerQuickItem{
    int bulletCount;

    public DynamicAmmo(int x, int y, int b) {
        super(x, y, 16, 16, -10*32, 0);
        bulletCount = b;
    }

    public boolean deathFromPlayer() {return true;}

    public void collisionPlayer(Player player) {
        player.addBullet(bulletCount);
    }

    @Override
    public void draw() {
        Graphics.drawColorRect(x, y, w, h, 0.5f, 0.5f, 0.5f);
    }
}
