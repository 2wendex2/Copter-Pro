package config;

import control.ControlException;
import control.Main;

import java.io.File;

//если вернул true завершить игру
//если вернул false продолжить

public class Config {
    public static final String version = "0.1.3";
    private static String configDir;
    private static String configName;
    private static String savesDir;

    public static String getConfigDir() {
        return configDir;
    }

    public static String getConfigName() {
        return configName;
    }

    public static String getSavesDir() {
        return savesDir;
    }

    static void setConfigDir(String configDir) {
        Config.configDir = configDir;
    }

    static void setConfigName(String configName) {
        Config.configName = configName;
    }

    static void setSavesDir(String savesDir) {
        Config.savesDir = savesDir;
    }

    public static void defaultNull() throws ControlException {
        if (configDir == null)
            configDir = ".";

        if (configName == null)
            configName = "config.dat";

        if (savesDir == null)
            savesDir = configDir + File.separatorChar + "saves";
    }

    private static String programPath() throws ControlException {
        try {
            return (new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI())).getParentFile().toString();
        } catch (Exception e) {
            throw new ControlException("Unable to get default program path, try specifying this path explicitly" +
                    " by entering -config-dir <path> key in command line arguments", e);
        }
    }
}