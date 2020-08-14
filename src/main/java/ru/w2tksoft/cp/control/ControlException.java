package ru.w2tksoft.cp.control;

/**
 * Исключения, бросаемые классами пакета control. Сообщения таких исключений представляют собой цепочку
 * сообщений, разделённые ": ", и готовы для показа конечному пользователю
 */
public class ControlException extends Exception {
    public ControlException(String message) {
        super(message);
    }

    public ControlException(String message, Throwable cause) {
        super(message, cause);
    }
}
