package control;

/*
Любое игровое состояние должно реализовывать интерфес ControlState
Принцип работы:
    Смена состояния происходит в начале итерации игрового цикла
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

public interface ControlState {
    default void input() throws ControlException {
        Control.getInstance().updateInput();
    }

    default void update() throws ControlException {}
    void draw() throws ControlException;

    //меняющая состояние функция должна знать, как закончилась инициализация, иначе она перейдёт
    //не на то состояние. Одно и то же состояние может использоваться несколько раз
    //Поэтому init должен правильно работать и в случае если состояние уде использовалось, и в случае,
    //если оно не инициализировано. Чаще всего просто делать проверку
    boolean init() throws ControlException;
    void inputCallback(int key, int scancode, int action, int mods) throws ControlException;
}