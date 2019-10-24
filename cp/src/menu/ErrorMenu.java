package menu;

import control.Control;
import control.ControlException;
import control.ControlState;

import javax.swing.*;

public class ErrorMenu extends Menu {
    private String str;
    private ControlState nextState;

    public void onExit() {}

    public void init() throws ControlException {
        JOptionPane.showMessageDialog(null,
                str, "Error", JOptionPane.ERROR_MESSAGE);
        Control.getInstance().changeStateNative(nextState);
    }

    public ErrorMenu(String str, ControlState nextState) {
        this.str = str;
        this.nextState = nextState;
    }
}
