package game;

import game.Level;

public class PlayerWrapping extends Player {
    private Player wrapping;
    private int wrape;

    //wrapMode: 1 вверх-вниз, 2 вправо-влево, 0 и то, и то
    public PlayerWrapping(int x, int y, int state, int bulletCount, int bombCount, int w, int h, int wrapMode) {
        super(x, y, state, bulletCount, bombCount);
        wrapping = new Player(x, y - h, state, bulletCount, bombCount);
        wrape = h;
    }

    public void restart() {
        super.restart();
        wrapping.restart();
    }

    public void update(Input input) {
        super.update(input);
        wrapping.update(input);
        shrinkToWrape();
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

    public void draw() {
        super.draw();
        wrapping.draw();
    }

    @Override
    public void onCollisionSoftWall(Wall other) {
        super.onCollisionSoftWall(other);
        wrapping.onCollisionSoftWall(other);
        shrinkToWrape();
    }

    public void shoot() {
        super.shoot();
        wrapping.shoot();
    }

    public void addBullet(int c) {
        super.addBullet(c);
        wrapping.addBullet(c);
    }

    @Override
    public boolean testCollision(Collidable other) {
        return super.testCollision(other) || wrapping.testCollision(other);
    }
}
