package ru.w2tksoft.cp.control;

/**
* Интерфейс, от которого наследуются все игровые состояния
*/
public interface ControlState extends Drawable{
    /**
     * Отрисовка
     */
    default void draw() {};

    /**
     * Обновление
     */
    default void update() {};

    /**
     * Нажатие кнопки клавиатуры
     * @param key идентефикатор кнопки
     */
    default void keyboardKeyPress(int key) {};

    /**
     * Отпуск кнопки клавиатуры
     * @param key идентефикатор кнопки
     */
    default void keyboardKeyRelease(int key) {};
}