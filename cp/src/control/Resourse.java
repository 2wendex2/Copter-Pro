package control;

import java.io.InputStream;

public class Resourse {
    public static InputStream getResourseAsInputStream(String type, String name) {
        StringBuilder path = new StringBuilder(30);
        path.append("/DATA/");
        path.append(type);
        path.append("/");
        path.append(name);
        path.append(".dat");
        return Main.class.getResourceAsStream(path.toString());
    }
}