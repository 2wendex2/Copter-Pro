package control;

public class ControlNativeException extends Exception {
    public ControlNativeException() {
        super();
    }

    public ControlNativeException(String message) {
        super(message);
    }

    public ControlNativeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControlNativeException(Throwable cause) {
        super(cause);
    }
}
