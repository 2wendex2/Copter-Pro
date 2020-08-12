package control;

/*
Любое игровое состояние должно реализовывать интерфес ControlState
*/

public interface ControlState {
    default void draw() {};
    default void update() {};
    default void keyboardKeyPress(int key) {};
    default void keyboardKeyRelease(int key) {};
}