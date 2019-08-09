import game.Level;

public interface Boss extends CollidableActor, DrawableActor{
    boolean isEnd();
    void update(Level level);
    void bulletCollision(Level level, Bullet bullet);
    void collisionSolidActor(SolidActor other);
    void restart();
}
