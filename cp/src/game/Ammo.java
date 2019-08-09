public class Ammo extends MovingSolidActor{
    int bulletCount;

    public Ammo(int x, int y, int b) {
        super(x, y, 16, 16, (byte)0, new Sprite(0.5f, 0.5f, 0.5f), -10*32, 0);
        bulletCount = b;
    }

    public int getBulletCount() {
        return bulletCount;
    }
}
