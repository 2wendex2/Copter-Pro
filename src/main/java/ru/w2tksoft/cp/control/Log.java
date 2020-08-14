package ru.w2tksoft.cp.control;

/**
 * Игровой лог
 */
public interface Log {
    /**
     * Записывает строку в лог
     * @param what строка
     */
    void println(String what);

    /**
     * Записывает исключение в лог
     * @param t исключение
     */
    void printThrowable(Throwable t);
}
