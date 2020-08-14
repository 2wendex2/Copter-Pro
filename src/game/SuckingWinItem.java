package game;

import control.Graphics;

public class SuckingWinItem extends Collidable.Rect implements PlayerStaticItem {
    static private int count;

    public SuckingWinItem(int x, int y) {
        super(x, y, 32, 32);
        count++;
    }

    static public void protoReset() {
        count = 0;
    }

    @Override
    public boolean deathFromPlayer() {
        return true;
    }

    @Override
    public void draw() {
        Graphics.drawColorRect(x, y, w, h, 1.f, 0.f, 1.f);
    }

    @Override
    public SuckingWinItem clone() {
        return new SuckingWinItem(x, y);
    }

    @Override
    public void collisionPlayer(Player player) {
        count--;
        if (count == 0)
            Game.getInstance().levelComplete();
    }
}
