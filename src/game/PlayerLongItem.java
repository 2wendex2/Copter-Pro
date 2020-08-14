package game;

public interface PlayerLongItem extends PlayerDynamicItem, Updatable {
    void collisionWall(Wall wall);
}
