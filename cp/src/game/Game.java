package game;

import config.*;
import control.*;
import menu.EpisodeSelect;
import menu.LevelSelect;
import menu.MainMenu;

import java.io.IOException;

public class Game implements ControlState {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 576;

    private Input input;
    private Level level;
    private int levelId;

    public Game(int levelId) {
        this.levelId = levelId;
    }

    public static Game getInstance() {
        return (Game)Control.getInstance().getState();
    }

    @Override
    public boolean init() throws ControlException {
        if (input == null) {
            ErrorManager.getInstance().clear(Control.getInstance().getState());
            input = new Input();
            try {
                level = LevelLoader.load(levelId);
            } catch (IOException e) {
                ErrorManager.getInstance().addWarning("Game init error: Level loader: " + e.getMessage());
                Log.printThrowable("Game.init LevelLoader", e);
            }

            try {
                MusicPool.getInstance().toGame(levelId);
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

        level.restart();
        MusicPool.getInstance().inGame(levelId);
        Graphics.setBackgroundColor(1.f, 1.f, 0.f, 1.f);
        return true;
    }

    public Level getLevel() {
        return level;
    }

    public void input() {
        input.update();
    }

    @Override
    public void inputCallback(int key, int scancode, int action, int mods) throws ControlException {
        input.inputCallback(key, scancode, action, mods);
    }

    public void levelComplete() {
        try {
            PlayerSave.getCurSave().setLevelComplete(levelId);
            Control.getInstance().changeState(new LevelSelect(levelId));
        } catch (IOException e) {
            ErrorManager.getInstance().clear(new MainMenu());
            ErrorManager.getInstance().addWarning("Save error: " + e.getMessage());
            ErrorManager.getInstance().dispatch();
            Log.printThrowable("Game.levelComplete save", e);
        }
    }

    public void starOpen() {
        try {
            PlayerSave.getCurSave().openStar(getLevelId());
            if (PlayerSave.getCurSave().getOpenedStarsCount(levelId / 19 + 1) == 3)
                Control.getInstance().changeState(new EpisodeSelect());
            else
                Control.getInstance().changeState(new LevelSelect(1, levelId));
        } catch (IOException e) {
            ErrorManager.getInstance().clear(new MainMenu());
            ErrorManager.getInstance().addWarning("Save error: " + e.getMessage());
            ErrorManager.getInstance().dispatch();
            Log.printThrowable("Game.starOpen save", e);
        }
    }

    public void update() {
        level.update(input);

    }

    public void draw(){
        Graphics.changeView(-level.updViewX() , -level.updViewY() + 24);
        level.draw();
    }

    public int getLevelId() {
        return levelId;
    }
}
