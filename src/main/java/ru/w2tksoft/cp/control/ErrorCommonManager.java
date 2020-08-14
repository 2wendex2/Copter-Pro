package ru.w2tksoft.cp.control;

import java.util.ArrayDeque;

/**
 * Менеджер рядовых ошибок. Так как рядовые ошибки не обязаны обрабатываться немедленно, для них нужно место хранения
 * до обработки, и это место здесь
 */
public class ErrorCommonManager {
    private ArrayDeque<ErrorCommon> errors;
    private Log log;

    /**
     * Создаёт менеджер
     * @param log лог, в который будет записываться информация об ошибках
     */
    public ErrorCommonManager(Log log) {
        errors = new ArrayDeque<>();
        this.log = log;
    }

    /**
     * Помещает ошибку в менеджер
     * @param err ошибка
     */
    public void add(ErrorCommon err) {
        log.println("common: " + err.getMessage());
        errors.add(err);
    }

    /**
     * Удаляет ошибку из менеджера
     * @return удалённая ошибка
     */
    public ErrorCommon remove() {
        return errors.remove();
    }

    /**
     * Получает ошибку из менеджера
     * @return ошибка
     */
    public ErrorCommon element() {
        return errors.element();
    }

    /**
     * Проверяет, пустой ли менеджер
     * @return true, если менеджер пустой, false в противном случае
     */
    public boolean isEmpty() {
        return errors.isEmpty();
    }
}
