package control;

import config.MusicPool;
import control.Control;
import control.ControlException;
import control.ControlState;
import menu.Menu;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import java.util.Stack;

//Суть
//Состояние содержит стек сообщений и указатель на следующее состояние
//Создавать можно как пустым, так и с начальным сообщением

public class ErrorState implements ControlState {
    private Stack<String> stack = new Stack<>();
    private ControlState nextState;

    public void add(String str) {
        stack.push(str);
    }

    public void addWithStateChange(String str, ControlState state) {
        stack.push(str);
        nextState = state;
    }

    @Override
    public void inputCallback(int key, int scancode, int action, int mods) throws ControlException {
        if (action == GLFW.GLFW_PRESS) {
            switch (key) {
                case GLFW.GLFW_KEY_ENTER:
                case GLFW.GLFW_KEY_ESCAPE:
                    Control.getInstance().changeStateNative(nextState);
            }
        }
    }

    public void clear(ControlState state) {
        nextState = state;
        stack.clear();
    }

    public  boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public void draw() {}

    public ErrorState(String str, ControlState state) {
        stack.push(str);
        nextState = state;
    }

    public ErrorState() {}

    public boolean init() {
        MusicPool.getInstance().stopMusic();
        if (nextState == null)
            nextState = Control.getInstance().getState();
        while (!stack.empty())
            JOptionPane.showMessageDialog(null,
                stack.pop(), "Error", JOptionPane.ERROR_MESSAGE);
        Graphics.setBackgroundColor(0.f, 0.f, 0.f, 1.f);
        return true;
    }
}
