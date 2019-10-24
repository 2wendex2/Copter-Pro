package config;

import java.io.IOException;
import java.util.HashMap;
import control.FileSprite;
import control.Sprite;

public class SpritePool {
    static private HashMap<String, Sprite> pool;

    public static void init() {
        pool = new HashMap<>();
    }

    public static Sprite get(String s) throws IOException {
        Sprite r = pool.get(s);
        if (r == null) {
            r = new FileSprite(s);
        }
        return r;
    }
}