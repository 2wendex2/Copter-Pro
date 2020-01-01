package menu;

import config.*;
import control.*;
import game.Game;
import game.Level;
import game.Player;

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
    private int stars;

    public LevelSelect(int episode, int level) {
        this.episode = episode;
        this.level = level;
    }

    public LevelSelect(int levelId) {
        if (levelId % 19 == 0) {
            episode = 7;
            level = levelId / 19;
        } else {
            episode = levelId / 19 + 1;
            if (episode == 7) {
                level = levelId - 19 * 6 - 1 + 7;
            } else {
                level = levelId % 19;
            }
        }
    }

    @Override
    public void onExit() throws ControlException {
        if (PlayerSave.getCurSave().getOpenedEpisodesCount() < 2 &&
                !PlayerSave.getCurSave().getSuckingOpened())
            Control.getInstance().changeStateNative(new MainMenu());
        else
            Control.getInstance().changeStateNative(new EpisodeSelect());
    }

    private void initRegular() {
        compl = PlayerSave.getCurSave().getCompletedLevelCount(episode) + 1;
        stars = PlayerSave.getCurSave().getOpenedStarsCount(episode);

        levelSprite = new Sprite[compl > 18 ? 18 : compl];

        width = levelSprite.length < 9 ? (levelSprite.length) * 80 + 16 : 9 * 80 + 16;
        height = levelSprite.length < 10 ? 64 * 3 : 64 * 7;

        x = (Control.WINDOW_WIDTH - width) / 2;
        y = (Control.WINDOW_HEIGHT - height) / 2;

        Sprite selected;
        for (int i = 0; i < levelSprite.length; i++)
            levelSprite[i] = SpritePool.get("level" + (i + 1));
        selected = SpritePool.get("selectlevel");
        notCompleted = SpritePool.get("notcompletedlevel");

        ArrayList<Button> selectables = new ArrayList<>(levelSprite.length);


        for (int i = 0; i < levelSprite.length && i < 9; i++)
            selectables.add(i, new LevelButton(levelSprite[i], selected,
                    x + 16 + i * 80, y + 64, i + 1 + (19 * (episode - 1))));

        if (levelSprite.length > 9)
            for (int i = 9; i < levelSprite.length; i++)
                selectables.add(i, new LevelButton(levelSprite[i], selected,
                        x + 16 + (i - 9) * 80, y + 64 * 5, i + 1 + (19 * (episode - 1))));

        for (int i = 0; i < levelSprite.length; i++) {
            Selectable up, down, left, right;
            if (i > 8)
                up = selectables.get(i - 9);
            else if (i <= 8 && levelSprite.length > 9) {
                if (levelSprite.length > i + 9)
                    up = selectables.get(i + 9);
                else
                    up = selectables.get(levelSprite.length - 1);
            } else
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
    }

    private void init7() {
        //в notCompletedX захуярим открытые уровни
        //в notCompletedY пройденные
        notCompletedX = 0;
        notCompletedY = 0;

        compl = PlayerSave.getCurSave().getCompletedLevelCount(7);
        int opnd = 0;

        for (int i = 0; i < 6; i++) {
            if (PlayerSave.getCurSave().getCompletedLevelCount(i + 1) > 17){
                notCompletedX |= (1 << i);
                opnd++;
                if (PlayerSave.getCurSave().getCompletedLevelCount(i + 1) > 18)
                    notCompletedY |= (1 << i);
            }
        }

        if (PlayerSave.getCurSave().getOpenedEpisodesCount() == 7) {
            notCompletedX |= (1 << 6);
            opnd++;
        }

        switch (compl) {
            case 1:
                notCompletedY |= (1 << 6);
                break;
            case 2:
                notCompletedY |= (1 << 7);
        }

        if ((notCompletedY & 0x7F) == 0x7F) {
            notCompletedX |= (1 << 7);
            opnd++;
        }

        levelSprite = new Sprite[8];

        width = (opnd) * 80 + 16;
        height = 64 * 3;

        x = (Control.WINDOW_WIDTH - width) / 2;
        y = (Control.WINDOW_HEIGHT - height) / 2;

        Sprite selected;
        for (int i = 0; i < 8; i++)
            if ((notCompletedX & (1 << i)) != 0)
                levelSprite[i] = SpritePool.get("level" + (i + 1));
        selected = SpritePool.get("selectlevel");

        if (notCompletedX != notCompletedY)
            notCompleted = SpritePool.get("notcompletedlevel");

        ArrayList<Button> selectables = new ArrayList<>(opnd);

        int a = 0;
        for (int i = 0; i < 8; i++)
            if ((notCompletedX & (1 << i)) != 0) {
                selectables.add(new LevelButton(levelSprite[i], selected,
                        x + 16 + a * 80, y + 64, i < 6 ? (i + 1) * 19 : 19 * 6 + 1 + (i - 6)));
                a++;
            }

        for (int i = 0; i < opnd; i++)
            selectables.get(i).setMarkUp(
                    i > 0 ? selectables.get(i - 1) : selectables.get(opnd - 1),
                    i < opnd - 1 ? selectables.get(i + 1) : selectables.get(0),
                    selectables.get(i), selectables.get(i));

        if (level == 0)
            level = 1;

        setSelectables(selectables, selectables.get(0));
    }

    @Override
    public boolean init() throws ControlException {
        if (levelSprite == null) {
            ErrorManager.getInstance().clear(LevelSelect.this);

            if (episode != 7)
                initRegular();
            else
                init7();

            try {
                MusicPool.getInstance().toLevelSelect(episode, level);
            } catch (IOException e) {
                ErrorManager.getInstance().addWarning("Level select init error: " + e.getMessage());
                Log.printThrowable("LevelSelect.init MusicPool", e);
            }

            if (SpritePool.isErrs()) {
                String s = SpritePool.getErrsMessage();
                ErrorManager.getInstance().addWarning("LevelSelect init error: Sprite loading error" + s);
                Log.println("ELevelSelect.init SpritePool\n" + s);
            }

            if (ErrorManager.getInstance().dispatchNative())
                return false;
        }



        switch (episode) {
            case 1:
                Graphics.setBackgroundColor(1.f, 1.f, 0.f, 1.f);
                break;
            case 2:
                Graphics.setBackgroundColor(1.f, 0.f, 0.f, 1.f);
                break;
            case 3:
                Graphics.setBackgroundColor(0.f, 1.f, 1.f, 1.f);
                break;
            case 4:
                Graphics.setBackgroundColor(0.f, 1.f, 0.f, 1.f);
                break;
            case 5:
                Graphics.setBackgroundColor(1.f, 0.f, 1.f, 1.f);
                break;
            case 6:
                Graphics.setBackgroundColor(1.f, 1.f, 1.f, 1.f);
                break;
            case 7:
                Graphics.setBackgroundColor(0.f, 0.f, 1.f, 1.f);
        }
        MusicPool.getInstance().inLevelSelect(episode, level);

        return true;
    }

    @Override
    public void draw() {
        Graphics.drawColorRect(x, y, width, height, 0.f, 0.f, 0.f);
        Graphics.setColor(1.f, 1.f, 1.f);
        if (episode != 7) {
            if (compl < 19)
                notCompleted.draw(notCompletedX, notCompletedY);
            switch (stars) {
                case 0:
                    Graphics.drawColorRect(16, 568, 768, 16, 0.f, 0x64 / 255.f, 0.f);
                    break;
                case 1:
                    Graphics.drawColorRect(16, 568, 384, 16, 0.f, 0x64 / 255.f, 0.f);
                    break;
                case 2:
                    Graphics.drawColorRect(16, 568, 192, 16, 0.f, 0x64 / 255.f, 0.f);
            }
        } else {
            int a = 0;
            for (int i = 0; i < 8; i++)
                if ((notCompletedX & (1 << i)) != 0) {
                    if ((notCompletedY & (1 << i)) == 0) {
                        notCompleted.draw(x + 12 + a * 80, y + 60);
                    }
                    a++;
                }
        }
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
            Control.getInstance().changeStateNative(new Game(levelNum));
        }
    }
}
