package ru.w2tksoft.cp.control;

import javax.swing.*;

//Класс-обработчик для фатальных ошибок

public class ErrorFatalControl {
    public static void dispatch(Throwable throwable) {
        JOptionPane.showMessageDialog(null,
                throwable instanceof ControlException ? throwable.getMessage() : "Unexpected error\n" +
                        throwable.toString() ,
                "Fatal error", JOptionPane.ERROR_MESSAGE);
    }
}
