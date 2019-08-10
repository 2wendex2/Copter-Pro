package game;

import control.Drawable;
import game.Level;

public interface Boss extends Collidable, Drawable {
    boolean isEnd();
    void update(Level level);
    void bulletCollision(Level level, Bullet bullet);
    void collisionSolidActor(SolidActor other);
    void restart();
}
