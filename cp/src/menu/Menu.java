package menu;

import java.util.ArrayList;

import control.ControlState;
import control.Control;
import org.lwjgl.glfw.GLFW;
import control.ControlException;

/*
Состояние меню, передаётся в Control.getInstance().changeState при необходимости сменить состояние на меню
Внешнее использование:
    - Создать объект, передав ему нужное окно
    (init вызовет главный цикл игры, ни в коем случае не вызывать самостоятельно! (у окна тоже))
    - Кинуть новосозданные объекты в Control.getInstance().changeState
Внутреннее испольщование:
    Данный объект используется внутри фу
*/

public class Menu implements ControlState {
    private Iterable<Selectable> selectables;
    private Selectable selected;
    private Window wnd;

    public static Menu getInstance() {
        return (Menu)Control.getInstance().getState();
    }

    public Menu(Window wnd) {
        this.wnd = wnd;
    }

    public void init() throws ControlException {
        wnd.init();
        Control.getInstance().changeInput((long window, int key, int scancode, int action, int mods) -> {
            if (action == GLFW.GLFW_PRESS)
                switch (key) {
                case GLFW.GLFW_KEY_LEFT:
                    selected = selected.getLeft();
                    break;
                case GLFW.GLFW_KEY_RIGHT:
                    selected = selected.getRight();
                    break;
                case GLFW.GLFW_KEY_UP:
                    selected = selected.getUp();
                    break;
                case GLFW.GLFW_KEY_DOWN:
                    selected = selected.getDown();
                    break;
                case GLFW.GLFW_KEY_ENTER:
                    selected.onPress();
                    break;
                case GLFW.GLFW_KEY_ESCAPE:
                    wnd.onExit();
                }
        });
    }

    public void input() {
        GLFW.glfwPollEvents();
    }

    public void update() {}

    public void draw() {
        wnd.draw();
        selected.drawSelected();
        for (Selectable i : selectables)
            i.draw();
    }

    public void destroy() {}

    void setSelectables(Iterable<Selectable> selectables, Selectable selected) {
        this.selectables = selectables;
        this.selected = selected;
    }
}
