package control;

import java.util.Stack;

public class ControlStateManager {
    private Control control;
    private Stack<ControlStateManageable> stack;

    public ControlStateManager(Control control) {
        this.control = control;
    }

    public void enter(ControlStateManageable controlState) {
        if (!stack.isEmpty())
            stack.peek().exit();
        stack.push(controlState);
        controlState.init();
        controlState.start();
        control.changeState(controlState);
    }

    public void leave(ControlStateManageable controlState) {
        ControlStateManageable prevState = stack.pop();
        if (prevState == controlState) {
            prevState.exit();
            prevState.destroy();
            if (!stack.isEmpty()) {
                ControlStateManageable newState = stack.peek();
                newState.start();
                control.changeState(newState);
            } else {
                control.changeState(new EmptyControlState());
            }
        }
    }
}
