package game;

import control.Graphics;

public class StaticWall extends Collidable.Rect implements Wall, Cloneable {
    public static final int PLAYER_NOT_KILLER = 1 << 0;
    public static final int ENEMY_KILLER = 1 << 1;
    private byte state;

    public StaticWall(int x, int y, int w, int h, byte state) {
        super(x, y, w, h);
        this.state = state;
    }

    public StaticWall clone() {
        return new StaticWall(x, y, w, h, state);
    }

    public boolean isPlayerKiller() {
        return (state & PLAYER_NOT_KILLER) == 0;
    }

    public boolean isEnemyKiller() {
        return (state & ENEMY_KILLER) != 0;
    }

    public void draw() {
        if (isPlayerKiller())
            Graphics.drawColorRect(x, y, w, h, 1.f, 0.f, 0.f);
        else
            Graphics.drawColorRect(x, y, w, h, 0.f, 1.f, 0.f);
    }
}
