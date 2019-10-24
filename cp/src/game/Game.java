package game;

import config.MusicPool;
import config.PlayerSave;
import control.*;
import menu.ErrorMenu;
import menu.LevelSelect;
import menu.MainMenu;

import java.io.IOException;

public class Game implements ControlState {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 576;

    private Input input = new Input();
    private Level level;
    /*private int levelId = 1;
    private Audio audio;
    private Sound curMusic;
    private SoundSource curSource;
    static final private int TARGET_UPS = 60;*/

    public Game(Level level) {
        this.level = level;
    }

    @Override
    public void init() throws ControlException {
        level.init();
        Graphics.setBackgroundColor(1.f, 1.f, 0.f, 1.f);
        try {
            MusicPool.getInstance().toGame(getLevelId());
        } catch (IOException e) {
            Control.getInstance().changeStateNative(new ErrorMenu("Music change error: " + e.getMessage(),
                    new MainMenu()));
        }
    }

    public void input() {
        input.update();
    }

    @Override
    public void inputCallback(int key, int scancode, int action, int mods) throws ControlException {
        input.inputCallback(key, scancode, action, mods);
    }

    public void update() throws ControlException {
        level.update(input);
        if (level.isEnded()) {
            try {
                PlayerSave.getCurSave().setLevelComplete(level.getId());
            } catch (IOException e) {
                Control.getInstance().changeStateNative(new ErrorMenu("Save error: " + e.getMessage(),
                        new MainMenu()));
            }
            Graphics.changeView(0, 0);
            Control.getInstance().changeState(new LevelSelect(1, level.getId() < 18 ?
                    level.getId() + 1 : 18));
        }
    }

    public void draw(){
        Graphics.changeView(-level.updViewX() , -level.updViewY() + 24);
        level.draw();
    }

    public int getLevelId() {
        return level.getId();
    }
}
