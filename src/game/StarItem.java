package game;

import control.Graphics;

public class StarItem extends Collidable.Rect implements PlayerStaticItem {
    public StarItem(int x, int y) {
        super(x, y, 32, 32);
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
    public StarItem clone() {
        return new StarItem(x, y);
    }

    @Override
    public void collisionPlayer(Player player) {
        Game.getInstance().starOpen();
    }
}
