package control;

/*
Любое игровое состояние должно реализовывать интерфес ControlState
Принцип работы:
    Смена состояния происходит в начале итерации игрового цикла
    Сначала вызывается destroy предыдущего состояния
    Затем вызывается init текущего
    Для смены состояния вызвать Control.getInstance().changeState(новое состояние)
    Если состяние то же самое, поведение не определено
    Если разные, то смена состояния произойдёт только в начале следующей итерации
    Вызов init первого состояния происходит при вызове Control.getInstance().init()
    Вызов destroy последнего состояния при исключениях не происходит (пока)

    Вызов input происходит в начале каждой итерации
    Вызов update соответственно после input
    Вызов draw после исчерпания итерационной задержки
*/

import menu.MenuException;

public interface ControlState {
    default void input() throws MenuException, ControlException {
        Control.getInstance().updateInput();
    }

    default void update() throws MenuException, ControlException {}
    void draw() throws MenuException, ControlException;
    void init() throws MenuException, ControlException;
    void inputCallback(int key, int scancode, int action, int mods) throws MenuException, ControlException;
}