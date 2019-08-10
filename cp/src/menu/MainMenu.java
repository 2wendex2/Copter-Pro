package menu;

import config.MusicPool;
import control.*;
import config.PlayerSave;
import game.Game;
import game.Level;
import logo.Logo;

import java.io.IOException;
import java.util.ArrayList;

public class MainMenu extends Menu {
    FileSprite cpSprite;

    public void init() throws ControlException {
        Graphics.changeView(0, 0);
        Graphics.setColor(1.f, 1.f, 1.f);
        FileSprite playSprite;
        FileSprite exitSprite;
        FileSprite selectedSprite;

        try {
            playSprite = new FileSprite("play");
            exitSprite = new FileSprite("exit");
            selectedSprite = new FileSprite("selectmain");
            cpSprite = new FileSprite("main");
        } catch (IOException e) {
            throw new ControlException(e.getMessage(), e);
        }

        Button playButton = new Button(playSprite, selectedSprite,
                (Control.WINDOW_WIDTH - playSprite.getW()) / 2,
                (Control.WINDOW_HEIGHT - playSprite.getH()) / 2 - 20 + 146,
                (Control.WINDOW_WIDTH - selectedSprite.getW()) / 2,
                (Control.WINDOW_HEIGHT - selectedSprite.getH()) / 2 - 20 + 146) {
            public void onPress() throws MenuException, ControlException {
                PlayerSave save = new PlayerSave("save");
                PlayerSave.setCurSave(save);
                int count = save.getOpenedEpisodesCount();
                if (count == 0)
                    Control.getInstance().changeStateNative(new Logo());
                else if (count == 1) {
                    if (save.getEpisodeProgress(0) == 0)
                        Control.getInstance().changeStateNative(new Game(new Level(1)));
                    else
                        Control.getInstance().changeStateNative(new LevelSelect(1, 0));
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

        MusicPool.getInstance().toMainMenu();
    }

    public void onExit() {
        Control.getInstance().changeState(null);
    }

    public void draw() {
        super.draw();
        cpSprite.draw((Control.WINDOW_WIDTH - cpSprite.getW()) / 2, 50);
    }
}
