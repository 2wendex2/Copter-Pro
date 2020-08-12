package control;

public class Main {
    public static void main(String[] args) throws ControlException {
        Log log = new Log();
        Control control = new Control();
        try {
            ErrorCommonManager errorCommonManager = new ErrorCommonManager(log);
            control.init(60, 800, 600, "control test", errorCommonManager);
            control.changeState(new TestControlState(control.getGraphics()));
            control.loop();
            control.destroy();
            throw new RuntimeException("Пинус");
        } catch (Throwable throwable) {
            log.println("FATAL ERROR");
            log.printThrowable(throwable);
            control.destroy();
            ErrorFatalControl.dispatch(throwable);
            throw throwable;
        }
    }
}