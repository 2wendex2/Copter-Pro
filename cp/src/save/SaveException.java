package save;

import java.io.IOException;

public class SaveException extends IOException {
    public static final int UNKNOWN = 0;
    public static final int PATH_IS_NOT_FILE = 1;
    public static final int PATH_IS_NOT_DIRECTORY = 2;
    public static final int FILE_CORRUPTED = 3;

    private int code;

    public SaveException() {
        super();
    }

    public SaveException(String message) {
        super(message);
    }

    public SaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
