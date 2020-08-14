package control;

public interface ControlStateManageable extends ControlState {
    void init();
    void destroy();
    void start();
    void exit();
}
