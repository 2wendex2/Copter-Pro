package menu;

import control.Control;
import control.ControlException;
import control.FileSprite;
import control.Sprite;
import save.PlayerSave;

import java.util.ArrayList;

public class MainMenu implements Window {
    FileSprite cpSprite;

    public void init() throws ControlException {
        FileSprite playSprite = new FileSprite("play");
        FileSprite exitSprite = new FileSprite("exit");
        FileSprite selectedSprite = new FileSprite("selectmain");
        cpSprite = new FileSprite("main");

        Button playButton = new Button(playSprite, selectedSprite,
                (Control.WINDOW_WIDTH - playSprite.getW()) / 2,
                (Control.WINDOW_HEIGHT - playSprite.getH()) / 2 - 20 + 146,
                (Control.WINDOW_WIDTH - selectedSprite.getW()) / 2,
                (Control.WINDOW_HEIGHT - selectedSprite.getH()) / 2 - 20 + 146) {
            public void onPress() {
                PlayerSave ps = new PlayerSave("save");
                System.out.println(ps.getGameStarted());
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

        Menu.getInstance().setSelectables(selectables, playButton);
        Control.getInstance().setBackgroundColor(0.f, 0.f, 0.f, 0.f);
    }

    public void onExit() {
        Control.getInstance().changeState(null);
    }

    public void draw() {
        cpSprite.draw((Control.WINDOW_WIDTH - cpSprite.getW()) / 2, 50);
    }
}
