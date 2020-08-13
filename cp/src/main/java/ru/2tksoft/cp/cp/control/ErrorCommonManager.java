package control;

import java.util.Stack;

public class ErrorCommonManager {
    private Stack<ErrorCommon> errors;
    private Log log;

    public ErrorCommonManager(Log log) {
        errors = new Stack<>();
        this.log = log;
    }

    public void push(ErrorCommon err) {
        log.println("common: " + err.getMessage());
        errors.push(err);
    }

    public ErrorCommon pop() {
        return errors.pop();
    }

    public ErrorCommon peek() {
        return errors.peek();
    }

    public boolean isEmpty() {
        return errors.isEmpty();
    }
}
