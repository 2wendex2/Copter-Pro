public class SolidActor extends CollidableActor.Rect implements DrawableActor{
    private byte state;
    private Sprite sprite;

    public static final int PLAYER_NOT_KILLER = 1 << 0;
    public static final int ENEMY_KILLER = 1 << 1;
    public static final int BULLET_DESTROY = 1 << 2;
    public static final int BOMB_DESTROY = 1 << 3;

    public SolidActor(int x, int y, int w, int h, byte state, Sprite sprite) {
        super(x, y, w, h);
        this.state = state;
        this.sprite = sprite;
    }

    public boolean isPlayerKiller() {
        return (state & PLAYER_NOT_KILLER) == 0;
    }

    public boolean isEnemyKiller() {
        return (state | ENEMY_KILLER) != 0;
    }

    //force: true — бомба, false — пуля
    //возвращает true, если удалить, false, если не надо
    public boolean bulletActorCollision(boolean force) {
        return (state | BULLET_DESTROY) != 0 && !force || (state | BOMB_DESTROY) != 0 && force;
    }

    public void draw() {
        sprite.draw(x, y, w, h);
    }
}