public class Bullet extends CollidableActor.Rect implements DrawableActor {
    private Sprite sprite;

    //force: true — бомба, false — пуля
    private boolean force;

    public Bullet(int x, int y, int w, int h, Sprite sprite, boolean force) {
        super(x, y, w, h);
        this.sprite = sprite;
        this.force = force;
    }

    public void update() {
        x += 7;
    }

    public boolean getForce() {
        return force;
    }

    public void draw() {
        sprite.draw(x, y, w, h);
    }
}