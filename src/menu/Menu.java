package menu;

import control.*;
import org.lwjgl.glfw.GLFW;

/*
Состояние меню, передаётся в Control.getInstance().changeState при необходимости сменить состояние на меню
Внешнее использование:
    - Создать объект, передав ему нужное окно
    (init вызовет главный цикл игры, ни в коем случае не вызывать самостоятельно! (у окна тоже))
    - Кинуть новосозданные объекты в Control.getInstance().changeState
Внутреннее испольщование:
    Данный объект используется внутри фу
*/

public abstract class Menu implements ControlState {
    private Iterable<? extends Selectable> selectables;
    private Selectable selected;

    public static Menu getInstance() {
        return (Menu)Control.getInstance().getState();
    }

    public void draw() {
        if (selected == null)
            return;
        selected.drawSelected();
        for (Selectable i : selectables)
            i.draw();
    }

    public abstract void onExit() throws ControlException;

    @Override
    public void inputCallback(int key, int scancode, int action, int mods) throws ControlException {
        if (action == GLFW.GLFW_PRESS) {
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
                    onExit();
            }
        }
    }

    protected void setSelectables(Iterable<? extends Selectable> selectables, Selectable selected) {
        this.selectables = selectables;
        this.selected = selected;
    }
}