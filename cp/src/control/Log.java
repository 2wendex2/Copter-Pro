package control;

public class Log {
    //private static String fileName;

    /*public static void init(String fileName) throws IOException {
        Log.fileName = fileName;
        FileWriter fw = new FileWriter(fileName);
        fw.append("Log start at ");
        fw.append((new Date().toString()));
        fw.append('\n');
        fw.close();
    }*/

    public Log() {}

    public void println(String what) {
        System.err.println(what);
    }

    public void printThrowable(Throwable t) {
        t.printStackTrace();
        System.err.println();
    }
}
