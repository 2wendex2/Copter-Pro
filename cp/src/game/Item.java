package game;

import control.Drawable;
import control.Graphics;

public class Item extends Collidable.Rect implements Drawable {
    public Item(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void draw() {
        Graphics.drawColorRect(x, y, 32, 32, 1.f, 0.f, 1.f);
    }
}