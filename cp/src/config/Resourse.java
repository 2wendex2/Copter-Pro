package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//Корневая папка

public class Resourse {
    private File root;
    private File data;
    private File settings;
    private Class jarClass;
    static private final String dataFolder = "DATA";
    static private final String settingsFolder = "SETTINGS";
    static private final String jarDataPath = "/" + dataFolder + "/";

    public Resourse(File root, Class jarClass) {
        this.root = root;
        this.data = new File(root, dataFolder);
        this.settings = new File(root, settingsFolder);
        this.jarClass = jarClass;
    }

    public InputStream getInputDataResourse(String type, String name) throws IOException {
        StringBuilder path = new StringBuilder(30);
        path.append(jarDataPath);
        path.append(type);
        path.append("/");
        path.append(name);
        path.append(".dat");
        if (jarClass.getResourceAsStream(path.toString()) != null)
            return jarClass.getResourceAsStream(path.toString());
        File typeFile = new File(data, type);
        File nameFile = new File(typeFile, name);
        return new FileInputStream(nameFile);
    }
}
