package ru.w2tksoft.cp.control;

/**
 * Спрайт - объект, для отрисовки которого нужно знать координаты на экране
 */
public interface Sprite {
    /**
     * Рисует объект
     * @param x координата x
     * @param y координата y
     */
    void draw(int x, int y);
}
