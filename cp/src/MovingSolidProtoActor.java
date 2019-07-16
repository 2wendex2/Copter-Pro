import java.util.concurrent.ThreadLocalRandom;

public class MovingSolidProtoActor {
    private int x, y, w, h, xr, yr, xspeed, yspeed;
    private byte flags, dflags;
    private Sprite sprite;

    public MovingSolidProtoActor(int x, int y, int w, int h, byte flags, Sprite sprite, byte dflags, int xr, int yr, int xspeed, int yspeed) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.flags = flags;
        this.sprite = sprite;
        this.dflags = dflags;
        this.xr = xr;
        this.yr = yr;
        this.xspeed = xspeed;
        this.yspeed = yspeed;
    }

    private static final int HVAR = 1 << 1;
    private static final int VVAR = 1 << 0;

    public MovingSolidActor generate() {
        if ((dflags & HVAR) != 0 && ThreadLocalRandom.current().nextInt(0, 2) != 0)
            xspeed = -xspeed;

        if ((dflags & VVAR) != 0 && ThreadLocalRandom.current().nextInt(0, 2) != 0)
            yspeed = -yspeed;

        return new MovingSolidActor(ThreadLocalRandom.current().nextInt(x, xr + 1),
                ThreadLocalRandom.current().nextInt(y, yr + 1), w, h, flags, sprite,
                xspeed, yspeed);
    }
}
