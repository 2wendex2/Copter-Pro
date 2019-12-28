package game;

import control.Drawable;

public interface Item extends GameObject, Drawable {
    boolean isForEnemy();
    boolean isDestroyable();
}