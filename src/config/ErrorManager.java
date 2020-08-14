package ru.w2tksoft.cp.config;

import control.Control;
import control.ControlException;
import control.ControlState;

public class ErrorManager {
    static private ErrorManager instance = new ErrorManager();
    private ErrorState errorState = new ErrorState();

    static public ErrorManager getInstance() {
        return instance;
    }

    public void addWarning(String str) {
        errorState.add(str);
    }

    public void addError(String str, ControlState state) {
        errorState.addWithStateChange(str, state);
    }

    public boolean dispatchNative() throws ControlException {
        if (!errorState.isEmpty()) {
            Control.getInstance().changeStateNative(errorState);
            return true;
        }
        return false;
    }

    public void dispatch() {
        if (!errorState.isEmpty())
            Control.getInstance().changeState(errorState);
    }

    public void clear(ControlState state) {
        errorState.clear(state);
    }
}
