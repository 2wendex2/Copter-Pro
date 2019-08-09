import game.Level;

public class PlayerWrapping extends Player {
    private Player wrapping;
    private int wrape;

    //wrapMode: 1 вверх-вниз, 2 вправо-влево
    public PlayerWrapping(int x, int y, int state, int bulletCount, int bombCount, int w, int h, int wrapMode) {
        super(x, y, state, bulletCount, bombCount);
        wrapping = new Player(x, y - h, state, bulletCount, bombCount);
        wrape = h;
    }

    public void restart() {
        super.restart();
        wrapping.restart();
    }

    public void update() {
        super.update();
        wrapping.update();
    }

    private void shrinkToWrape() {
        if (y < h) {
            y += wrape;
            yGrav += wrape * 32;
            wrapping.y += wrape;
            wrapping.yGrav += wrape * 32;
        } else if (wrapping.y > wrape - h) {
            y -= wrape;
            yGrav -= wrape * 32;
            wrapping.y -= wrape;
            wrapping.yGrav -= wrape * 32;
        }
    }

    public void updateRelease() {
        super.updateRelease();
        wrapping.updateRelease();
        shrinkToWrape();
    }

    public void updatePress() {
        super.updatePress();
        wrapping.updatePress();
        shrinkToWrape();
    }

    public void draw() {
        super.draw();
        wrapping.draw();
    }

    public void onCollisionSolidNK(SolidActor other) {
        super.onCollisionSolidNK(other);
        wrapping.onCollisionSolidNK(other);
        shrinkToWrape();
    }

    public void shoot(Level level) {
        super.shoot(level);
        wrapping.shoot(level);
    }

    public void addBullet(int c) {
        super.addBullet(c);
        wrapping.addBullet(c);
    }

    @Override
    public boolean testCollision(CollidableActor other) {
        return super.testCollision(other) || wrapping.testCollision(other);
    }
}
