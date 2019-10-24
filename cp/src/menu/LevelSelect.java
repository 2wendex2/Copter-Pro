package menu;

import config.MusicPool;
import config.PlayerSave;
import control.*;
import game.Game;
import game.Level;

import java.io.IOException;
import java.util.ArrayList;

public class LevelSelect extends Menu {
    private int episode, level;
    private int width;
    private int height;
    private int x;
    private int y;
    private int compl;
    private Sprite[] levelSprite ;
    private Sprite notCompleted;
    private int notCompletedX, notCompletedY;

    public LevelSelect(int episode, int level) {
        this.episode = episode;
        this.level = level;
        compl = PlayerSave.getCurSave().getCompletedLevelCount(episode) + 1;
        levelSprite = new Sprite[compl > 18 ? 18 : compl];
    }

    @Override
    public void onExit() throws ControlException {
        if (PlayerSave.getCurSave().getCompletedLevelCount(1) < 18)
            Control.getInstance().changeStateNative(new MainMenu());
        else
            Control.getInstance().changeStateNative(new EpisodeSelect());
    }

    @Override
    public void init() throws ControlException {
        Graphics.changeView(0, 0);

        width = levelSprite.length < 9 ? (levelSprite.length) * 80 + 16 : 9 * 80 + 16;
        height = levelSprite.length < 10 ? 64 * 3 : 64 * 7;

        x = (Control.WINDOW_WIDTH - width) / 2;
        y = (Control.WINDOW_HEIGHT - height) / 2;

        Sprite selected;
        try {
            for (int i = 0; i < levelSprite.length; i++)
                levelSprite[i] = new FileSprite("level" + (i + 1));
            selected = new FileSprite("selectlevel");
            notCompleted = new FileSprite("notcompletedlevel");

        ArrayList<Button> selectables = new ArrayList<>(levelSprite.length);
        for (int i = 0; i < levelSprite.length && i < 9; i++)
            selectables.add(i, new LevelButton(levelSprite[i], selected,
                    x + 16 + i * 80, y + 64, i+1));

        if (levelSprite.length > 9)
            for (int i = 9; i < levelSprite.length; i++)
                selectables.add(i, new LevelButton(levelSprite[i], selected,
                        x + 16 + (i - 9) * 80, y + 64 * 5, i+1));

        for (int i = 0; i < levelSprite.length; i++) {
            Selectable up, down, left, right;
            if (i > 8)
                up = selectables.get(i - 9);
            else if (i <= 8 && levelSprite.length > 9) {
                if (levelSprite.length > i + 9)
                    up = selectables.get(i + 9);
                else
                    up = selectables.get(levelSprite.length - 1);
            }
            else
                up = selectables.get(i);

            down = up;

            if (i == 0) {
                if (levelSprite.length >= 9)
                    left = selectables.get(8);
                else
                    left = selectables.get(levelSprite.length - 1);
            } else if (i < 9 || i > 9)
                left = selectables.get(i - 1);
            else
                left = selectables.get(levelSprite.length - 1);


            if (i == levelSprite.length - 1) {
                if (levelSprite.length <= 9)
                    right = selectables.get(0);
                else
                    right = selectables.get(9);
            } else if (i == 8)
                right = selectables.get(0);
            else
                right = selectables.get(i + 1);

            selectables.get(i).setMarkUp(left, right, up, down);
        }

        notCompletedX = selectables.get(levelSprite.length - 1).getX() - 4;
        notCompletedY = selectables.get(levelSprite.length - 1).getY() - 4;

        if (level == 0)
            level = compl > 18 ? 1 : compl;

        setSelectables(selectables, selectables.get(level - 1));
        Graphics.setBackgroundColor(1.f, 1.f, 0.f, 1.f);

        MusicPool.getInstance().toLevelSelect(episode, level);
        } catch (IOException e) {
            Control.getInstance().changeStateNative(new ErrorMenu("Level select init error: " + e.getMessage(),
                    new MainMenu()));
        }
    }

    @Override
    public void draw() {
        Graphics.drawColorRect(x, y, width, height, 0.f, 0.f, 0.f);
        Graphics.setColor(1.f, 1.f, 1.f);
        if (compl < 19)
            notCompleted.draw(notCompletedX, notCompletedY);
        super.draw();
    }

    private static class LevelButton extends Button {
        int levelNum;

        public LevelButton(Sprite sprite, Sprite selectedSprite, int x, int y, int levelNum) {
            super(sprite, selectedSprite, x, y, x - 8, y - 8);
            this.levelNum = levelNum;
        }

        @Override
        public void onPress() throws ControlException {
            Control.getInstance().changeStateNative(new Game(new Level(levelNum)));
        }
    }
}
