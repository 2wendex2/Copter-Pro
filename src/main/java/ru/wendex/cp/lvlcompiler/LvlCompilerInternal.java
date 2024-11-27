package ru.wendex.cp.lvlcompiler;

import java.util.logging.Logger;

class LvlCompilerInternal {
    private static Logger logger = Logger.getLogger("ru.wendex.cp.lvlcompiler");
    private static String version = "1.0";

    static Logger getLogger()
    {
        return logger;
    }

    static String getVersion()
    {
        return version;
    }
}
