package logo;

import config.MusicPool;
import config.PlayerSave;
import control.*;
import game.Game;
import game.Level;
import menu.ErrorMenu;
import menu.MainMenu;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import java.io.IOException;

public class Logo implements ControlState {
    private Sprite beginLogo;
    private int timer;

    @Override
    public void init() throws ControlException {
        try {
            beginLogo = new FileSprite("begin");

        MusicPool.getInstance().toBeginGame();
        Graphics.setBackgroundColor(1.f, 1.f, 1.f, 1.f);
        } catch (IOException e) {
            Control.getInstance().changeStateNative(new ErrorMenu("Logo init error: " + e.getMessage(),
                    new MainMenu()));
        }
    }

    @Override
    public void draw() {
        beginLogo.draw(0, 0);
    }

    @Override
    public void inputCallback(int key, int scancode, int action, int mods) throws ControlException {
        if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_ESCAPE)
            Control.getInstance().changeStateNative(new MainMenu());
    }

    public void update() throws ControlException {
        try {
            timer++;
            if (timer > 340 && timer <= 380) {
                float turquoise = (timer - 340.f) / 40;
                Graphics.setBackgroundColor(1.f - turquoise, 1.f, 1.f, 1.f);
            } else if (timer > 430 && timer <= 470) {
                float blue = (timer - 430.f) / 40;
                Graphics.setBackgroundColor(0.f, 1.f - blue, 1.f, 1.f);
            } else if (timer > 520 && timer <= 560) {
                float pink = (timer - 520.f) / 40;
                Graphics.setBackgroundColor(pink, 0.f, 1.f, 1.f);
            } else if (timer > 610 && timer <= 650) {
                float red = (timer - 610.f) / 40;
                Graphics.setBackgroundColor(1.f, 0.f, 1.f - red, 1.f);
            } else if (timer > 690 && timer <= 730) {
                float yellow = (timer - 690.f) / 40;
                Graphics.setBackgroundColor(1.f, yellow, 0.f, 1.f);
            } else if (timer > 780 && timer <= 820) {
                float green = (timer - 780.f) / 40;
                Graphics.setBackgroundColor(1.f - green, 1.f, 0.f, 1.f);
            } else if (timer > 870 && timer <= 950) {
                float black = (timer - 870.f) / 80;
                Graphics.setBackgroundColor(0.f, 1.f - black, 0.f, 1.f);
            } else if (timer == 1055) {
                PlayerSave.getCurSave().setEpisodeComplete(0);
                Control.getInstance().changeState(new Game(new Level(1)));
            }
        } catch (IOException e) {
            Control.getInstance().changeStateNative(new ErrorMenu("Logo update error: " + e.getMessage(),
                    new MainMenu()));
        }
    }
}
