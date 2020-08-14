package ru.w2tksoft.cp.control;

/**
 * Спрайт, представляющий собой прямоугольник
 */
public interface RectSprite extends Sprite{
    /**
     * Получить ширину
     * @return ширина
     */
    int getW();

    /**
     * Получить высоту
     * @return высота
     */
    int getH();
}