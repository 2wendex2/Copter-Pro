package ru.w2tksoft.cp.control;

import org.junit.Test;

public class TestControl {
    @Test
    public void test() throws ControlException {
        Log log = new StdErrLog();
        Control control = new Control();
        try {
            ErrorCommonManager errorCommonManager = new ErrorCommonManager(log);
            control.init(60, 800, 600, "control test", errorCommonManager);
            control.changeState(new TestControlState(control.getGraphics()));
            control.loop();
            control.destroy();
        } catch (Throwable throwable) {
            log.println("FATAL ERROR");
            log.printThrowable(throwable);
            control.destroy();
            ErrorFatalControl.dispatch(throwable);
            throw throwable;
        }
    }
}