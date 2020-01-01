package game;

//Общий интерфейс для стен

import control.Drawable;

public interface Wall extends GameObject, Drawable, Collidable.RectI {
    boolean isPlayerKiller();
    boolean isEnemyKiller();
}