package menu;

import control.ControlException;
import control.Drawable;

public interface Window extends Drawable {
    void onExit();
    void init() throws ControlException;
}