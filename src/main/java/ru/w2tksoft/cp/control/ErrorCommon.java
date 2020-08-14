package ru.w2tksoft.cp.control;

/**
 * Рядовая ошибка. Некоторые классы пакета control используют их для описания рядовых ошибок.
 * Сообщения представляют собой цепочку сообщений, разделённые ": ", и готовы для показа конечному пользователю
 */
public class ErrorCommon {
    private String what;

    /**
     * Создаёт рядовую ошибку
     * @param what сообщение
     */
    public ErrorCommon(String what) {
        this.what = what;
    }

    /**
     * Получение сообщения
     * @return сообщение
     */
    public String getMessage() {
        return what;
    }
}
