package game;

import config.PlayerSave;
import control.Control;
import control.ControlException;
import control.Graphics;
import menu.LevelSelect;
import menu.MainMenu;
import menu.Menu;
import menu.MenuException;
import org.lwjgl.glfw.*;
import java.util.Arrays;

public class Input {
    //Суть: keys — состояние клавиш для игры
    //pressedKeys и releasedKeys — для callback
    //Константы — номера клавиш
    public static final int KEY_UP = 0;
    public static final int KEY_SHOOT = 1;
    public static final int KEY_DIRECTION = 2;
    public static final int KEY_COUNT = 3;
    private boolean[] prevKeys = new boolean[KEY_COUNT];
    private boolean[] keys = new boolean[KEY_COUNT];
    private boolean[] pressedKeys = new boolean[KEY_COUNT];
    private boolean[] releasedKeys = new boolean[KEY_COUNT];

    public void inputCallback(int key, int scancode, int action, int mods) throws MenuException, ControlException {
        if (action == GLFW.GLFW_PRESS)
            switch(key) {
                case GLFW.GLFW_KEY_SPACE:
                    pressedKeys[KEY_UP] = true;
                    break;
                case GLFW.GLFW_KEY_LEFT_SHIFT:
                case GLFW.GLFW_KEY_RIGHT_SHIFT:
                    pressedKeys[KEY_SHOOT] = true;
                    break;
                case GLFW.GLFW_KEY_Z:
                    pressedKeys[KEY_DIRECTION] = true;
                    break;
                case GLFW.GLFW_KEY_ESCAPE:
                    Graphics.changeView(0, 0);
                    if (PlayerSave.getCurSave().getOpenedEpisodesCount() > 0)
                        Control.getInstance().changeStateNative(new LevelSelect(1,
                            ((Game)Control.getInstance().getState()).getLevelId()));
                    else
                        Control.getInstance().changeStateNative(new MainMenu());
            }
        else if (action == GLFW.GLFW_RELEASE) {
            switch (key) {
                case GLFW.GLFW_KEY_SPACE:
                    releasedKeys[KEY_UP] = false;
                    break;
                case GLFW.GLFW_KEY_LEFT_SHIFT:
                case GLFW.GLFW_KEY_RIGHT_SHIFT:
                    releasedKeys[KEY_SHOOT] = false;
                    break;
                case GLFW.GLFW_KEY_Z:
                    releasedKeys[KEY_DIRECTION] = false;
            }
        }
    }

    public void update() {
        GLFW.glfwPollEvents();
        for (int i = 0; i < KEY_COUNT; i++) {
            prevKeys[i] = keys[i];
            keys[i] = pressedKeys[i] || keys[i] && releasedKeys[i];
        }
        Arrays.fill(pressedKeys, false);
        Arrays.fill(releasedKeys, true);
    }

    public boolean getKey(int i){
        return keys[i];
    }

    public boolean getPrev(int i){
        return prevKeys[i];
    }
}
