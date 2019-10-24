package config;

import org.lwjgl.system.CallbackI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {
    private static String fileName;

    /*public static void init(String fileName) throws IOException {
        Log.fileName = fileName;
        FileWriter fw = new FileWriter(fileName);
        fw.append("Log start at ");
        fw.append((new Date().toString()));
        fw.append('\n');
        fw.close();
    }*/



    public static void println(String what) {
        System.err.println(what);
        System.err.println();
    }

    public static void printThrowable(String what, Throwable t) {
        System.err.println(what);
        t.printStackTrace();
        System.err.println();
    }
}
