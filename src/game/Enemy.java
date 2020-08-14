package game;

public interface Enemy extends GameObject, Updatable, Collidable.RectI {
    void hurt();
    boolean isDeath();
    void onCollisionSoftWall(Wall wall);
    void onCollisionHardWall(Wall wall);
}