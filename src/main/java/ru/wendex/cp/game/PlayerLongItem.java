package ru.wendex.cp.game;

public interface PlayerLongItem extends PlayerDynamicItem, Updatable {
    void collisionWall(Wall wall);
}
