package ru.w2tksoft.cp.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import control.FileSprite;
import control.RectSprite;
import control.Sprite;

public class SpritePool {
    static private HashMap<String, Sprite> pool = new HashMap<>();
    static private Stack<String> errs = new Stack<>();

    public static Sprite get(String s) {
        try {
            Sprite r = pool.get(s);
            if (r == null) {
                r = new FileSprite(s);
            }
            return r;
        } catch (IOException e) {
            Log.printThrowable("Sprite loading", e);
            errs.push(s + ": " + e.getMessage());
            return new RectSprite(252.f / 255.0f, 15.f / 255.0f, 192.f / 255.0f, 64, 32);
        }
    }

    static public String getErrsMessage() {
        StringBuilder sb = new StringBuilder();
        while (!errs.empty()) {
            sb.append('\n');
            sb.append(errs.pop());
        }
        return sb.toString();
    }



    public static boolean isErrs() {
        return !errs.empty();
    }
}