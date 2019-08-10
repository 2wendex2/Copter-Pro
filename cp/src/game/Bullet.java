package game;

import control.Drawable;
import control.Graphics;

public class Bullet extends Collidable.Rect implements Drawable {
    //force: true — бомба, false — пуля
    private boolean force;

    public Bullet(int x, int y, int w, int h, boolean force) {
        super(x, y, w, h);
        this.force = force;
    }

    public void update() {
        x += 7;
    }

    public boolean getForce() {
        return force;
    }

    public void draw() {
        Graphics.drawColorRect(x, y, w, h, 0.f, 0.f, 1.f);
    }
}