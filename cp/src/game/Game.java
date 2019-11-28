package game;

import config.*;
import control.*;
import control.ErrorState;
import menu.EpisodeSelect;
import menu.LevelSelect;
import menu.MainMenu;

import java.io.IOException;

public class Game implements ControlState {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 576;

    private Input input;
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
    public boolean init() throws ControlException {
        if (input == null) {
            ErrorManager.getInstance().clear(Control.getInstance().getState());
            input = new Input();
            try {
                MusicPool.getInstance().toGame(getLevelId());
            } catch (IOException e) {
                ErrorManager.getInstance().addWarning("Game init error: " + e.getMessage());
                Log.printThrowable("Game.init MusicPool", e);
            }

            if (SpritePool.isErrs()) {
                String s = SpritePool.getErrsMessage();
                ErrorManager.getInstance().addWarning("LevelSelect init error: Sprite loading error" + s);
                Log.println("Game.init SpritePool\n" + s);
            }

            if (ErrorManager.getInstance().dispatchNative())
                return false;
        }

        level.init();
        Graphics.setBackgroundColor(1.f, 1.f, 0.f, 1.f);
        return true;
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
        try {
            if (level.isEnded()) {
                PlayerSave.getCurSave().setLevelComplete(level.getId());
                Control.getInstance().changeState(new LevelSelect(1, level.getId() < 18 ?
                        level.getId() + 1 : 18));
            } else if (level.isStarEnded()) {
                PlayerSave.getCurSave().openStar(getLevelId());
                if (PlayerSave.getCurSave().getOpenedStarsCount(getLevelId() / 19 + 1) == 3)
                    Control.getInstance().changeState(new EpisodeSelect());
                else
                    Control.getInstance().changeState(new LevelSelect(1, level.getId()));
            }
        } catch (IOException e) {
            ErrorManager.getInstance().clear(new MainMenu());
            ErrorManager.getInstance().addWarning("Save error: " + e.getMessage());
            ErrorManager.getInstance().dispatch();
            Log.printThrowable("Game.update save", e);
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
