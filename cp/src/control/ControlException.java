package control;

import javax.swing.*;

public class ControlException extends Exception {
    public ControlException() {
        super();
    }

    public ControlException(String message) {
        super(message);
    }

    public ControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControlException(Throwable cause) {
        super(cause);
    }

    //вызывать только при падении программы, проглатывает все несистемные исключения
    static void trainFatalError(Throwable e) {
        try {
            if (Control.getInstance() != null)
                Control.getInstance().destroy();

            StringBuilder sb = new StringBuilder();
            sb.append(e.toString());
            for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
                sb.append("\n\tcaused by:\n");
                sb.append(t.toString());
            }

            JOptionPane.showMessageDialog(null, sb.toString(),
                    "Fatal error", JOptionPane.ERROR_MESSAGE);
        } catch (Throwable ee) {
            //глотаем несистемные исключения
        }
    }
}
