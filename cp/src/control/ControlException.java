package control;

import javax.swing.*;

public class ControlException extends Exception {
    public ControlException(String message) {
        super(message);
    }

    public ControlException(String message, Throwable cause) {
        super(message, cause);
    }

    //вызывать только при падении программы, проглатывает все несистемные исключения
    static void trainFatalError(Throwable e) {
        try {
            JOptionPane.showMessageDialog(null,
                    e instanceof ControlException ? e.getMessage() : "Unexpected error\n" + e.toString(),
                    "Fatal error", JOptionPane.ERROR_MESSAGE);
        } catch (Throwable ee) {
            //глотаем несистемные исключения
        }
    }
}
