package control;

public class ErrorCommon {
    private String what;

    public ErrorCommon(String what) {
        this.what = what;
    }

    public String getMessage() {
        return what;
    }
}
