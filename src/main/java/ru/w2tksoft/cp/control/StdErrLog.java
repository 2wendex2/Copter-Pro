package ru.w2tksoft.cp.control;

/**
 * Лог, ведущий запись в stderr
 */
public class StdErrLog implements Log {
    /**
     * Создаёт объект
     */
    public StdErrLog() {}

    public void println(String what) {
        System.err.println(what);
    }

    public void printThrowable(Throwable t) {
        t.printStackTrace();
        System.err.println();
    }
}
