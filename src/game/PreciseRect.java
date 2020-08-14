package game;

public class PreciseRect extends Collidable.Rect {
    protected int xOffset, yOffset;

    public PreciseRect(int x, int y, int w, int h) {
        super(x, y, w, h);
        xOffset = x*32;
        yOffset = y*32;
    }

    public void precise() {
        x = xOffset / 32;
        y = yOffset / 32;
    }
}
