package menu;

import control.ControlException;

import javax.swing.*;

public class MenuException extends Exception {
    public MenuException(String message) {
        super(message);
    }

    public MenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void trainError(Throwable e) {
        JOptionPane.showMessageDialog(null,
                e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}