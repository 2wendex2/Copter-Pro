package ru.wendex.cp.game;

//Общий интерфейс для стен

import ru.wendex.cp.control.Drawable;

public interface Wall extends GameObject, Drawable, Collidable.RectI {
    boolean isPlayerKiller();
    boolean isEnemyKiller();
}