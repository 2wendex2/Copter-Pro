package menu;

import config.*;
import control.*;
import game.Game;
import game.Level;
import logo.Logo;

import java.io.IOException;
import java.util.ArrayList;

public class MainMenu extends Menu {
    Sprite cpSprite;

    public boolean init() throws ControlException{
        if (cpSprite == null) {
            ErrorManager.getInstance().clear(MainMenu.this);
            Sprite playSprite;
            Sprite exitSprite;
            Sprite selectedSprite;

            playSprite = SpritePool.get("play");
            exitSprite = SpritePool.get("exit");
            selectedSprite = SpritePool.get("selectmain");
            cpSprite = SpritePool.get("main");

            Button playButton = new Button(playSprite, selectedSprite,
                    (Control.WINDOW_WIDTH - playSprite.getW()) / 2,
                    (Control.WINDOW_HEIGHT - playSprite.getH()) / 2 - 20 + 146,
                    (Control.WINDOW_WIDTH - selectedSprite.getW()) / 2,
                    (Control.WINDOW_HEIGHT - selectedSprite.getH()) / 2 - 20 + 146) {
                public void onPress() throws ControlException {
                    try {
                        PlayerSave save = new PlayerSave("save");
                        PlayerSave.setCurSave(save);
                        int count = save.getOpenedEpisodesCount();
                        if (count == 0)
                            Control.getInstance().changeStateNative(new Logo());
                        else if (save.getCompletedLevelCount(1) == 0)
                            Control.getInstance().changeStateNative(new Game(new Level(1)));
                        else if (!save.getSuckingOpened() && count < 2)
                            Control.getInstance().changeStateNative(new LevelSelect(1, 0));
                        else
                            Control.getInstance().changeStateNative(new EpisodeSelect());
                    } catch (IOException e) {
                        ErrorManager.getInstance().clear(MainMenu.this);
                        ErrorManager.getInstance().addWarning("Save not loaded: " + e.getMessage());
                        ErrorManager.getInstance().dispatchNative();
                        Log.printThrowable("playButton onPress", e);
                    }
                }
            };

            Button exitButton = new Button(exitSprite, selectedSprite,
                    (Control.WINDOW_WIDTH - exitSprite.getW()) / 2,
                    (Control.WINDOW_HEIGHT - exitSprite.getH()) / 2 + 20 + 146,
                    (Control.WINDOW_WIDTH - selectedSprite.getW()) / 2,
                    (Control.WINDOW_HEIGHT - selectedSprite.getH()) / 2 + 20 + 146) {
                public void onPress() {
                    Control.getInstance().changeState(null);
                }
            };

            playButton.setMarkUp(playButton, playButton, exitButton, exitButton);
            exitButton.setMarkUp(exitButton, exitButton, playButton, playButton);

            ArrayList<Selectable> selectables = new ArrayList<>(2);
            selectables.add(0, playButton);
            selectables.add(1, exitButton);

            setSelectables(selectables, playButton);
            Graphics.setBackgroundColor(0.f, 0.f, 0.f, 0.f);

            try {
                MusicPool.getInstance().toMainMenu();
            } catch (IOException e) {
                ErrorManager.getInstance().addWarning("Main menu init error: " + e.getMessage());
                Log.printThrowable("MainMenu.init MusicPool", e);
            }

            if (SpritePool.isErrs()) {
                String s = SpritePool.getErrsMessage();
                ErrorManager.getInstance().addWarning("Main menu init error: Sprite loading error" + s);
                Log.println("MainMenu.init SpritePool\n" + s);
            }

            if (ErrorManager.getInstance().dispatchNative())
                return false;
        }

        Graphics.setColor(1.f, 1.f, 1.f);
        return true;
    }

    public void onExit() {
        Control.getInstance().changeState(null);
    }

    public void draw() {
        super.draw();
        cpSprite.draw((Control.WINDOW_WIDTH - cpSprite.getW()) / 2, 50);
    }
}
