package menu;

import config.MusicPool;
import config.PlayerSave;
import config.SpritePool;
import control.*;

import java.io.IOException;
import java.util.ArrayList;

import static config.PlayerSave.getCurSave;

public class EpisodeSelect extends Menu {
    private Sprite title;

    private static class EpisodeButton extends Button {
        private int epNum;

        public EpisodeButton(Sprite spr, Sprite selSpr,
                             int x, int y, int selx, int sely, int ep) {
            super(spr, selSpr, x, y, selx, sely);
            epNum = ep;
        }

        @Override
        public void onPress() throws ControlException {
            Control.getInstance().changeStateNative(new LevelSelect(epNum + 1, 0));
        }
    }

    public EpisodeSelect() {}

    @Override
    public void init() throws ControlException
    {
        try {
            title = SpritePool.get("epselect");

            int commonEpisodes = PlayerSave.getCurSave().getOpenedEpisodesCount();
            Sprite suckingSprite;
            if (commonEpisodes == 7) {
                suckingSprite = SpritePool.get("ep7");
                commonEpisodes = 6;
            }
            else
                suckingSprite = SpritePool.get("epempty");



            Sprite episodeSelected = SpritePool.get("selectep");
            Sprite suckingSelected = SpritePool.get("selectempty");
            Sprite ep1s = SpritePool.get("ep1");

            int epw = ep1s.getW();
            int eph = ep1s.getH();
            int suckh = suckingSprite.getH();
            int titleh = title.getH();

            //рассчёт локации
            int[] xarr = new int[commonEpisodes];
            int[] yarr = new int[commonEpisodes];
            switch (commonEpisodes) {
                case 1:
                    xarr[0] = (Control.WINDOW_WIDTH - epw) / 2;
                    yarr[0] = (Control.WINDOW_HEIGHT - ep1s.getH()) / 2;
                    break;
                case 2:
                    xarr[0] = (Control.WINDOW_WIDTH - epw * 2) / 4;
                    xarr[1] = xarr[0] * 3 + epw;
                    yarr[0] = (Control.WINDOW_HEIGHT - ep1s.getH()) / 2;
                    yarr[1] = yarr[0];
                    break;
                case 3:
                    xarr[0] = (Control.WINDOW_WIDTH - epw * 3) / 6;
                    xarr[1] = xarr[0] * 3 + epw;
                    xarr[2] = xarr[0] * 5 + 2 * epw;
                    yarr[0] = (Control.WINDOW_HEIGHT - ep1s.getH()) / 2;
                    yarr[2] = yarr[0];
                    yarr[3] = yarr[0];
                    break;
                case 4:
                    xarr[0] = (Control.WINDOW_WIDTH - epw * 2) / 4;
                    xarr[1] = xarr[0] * 3 + epw;
                    xarr[2] = xarr[0];
                    xarr[3] = xarr[1];
                    yarr[0] = (Control.WINDOW_HEIGHT - (64 + titleh + suckh)) + 32 + titleh;
                    yarr[1] = yarr[0];
                    yarr[2] = (Control.WINDOW_HEIGHT - (64 + titleh + suckh)) - eph * 2 + yarr[0];
                    yarr[3] = yarr[2];
                    break;
                case 5:
                    xarr[0] = (Control.WINDOW_WIDTH - epw * 3) / 6;
                    xarr[1] = xarr[0];
                    xarr[2] = xarr[0] * 3 + epw;
                    xarr[3] = xarr[0] * 5 + 2 * epw;
                    xarr[4] = xarr[3];
                    yarr[0] = (Control.WINDOW_HEIGHT - (64 + titleh + suckh)) + 32 + titleh;
                    yarr[1] = yarr[0];
                    yarr[2] = (Control.WINDOW_HEIGHT - ep1s.getH()) / 2;
                    yarr[3] = (Control.WINDOW_HEIGHT - (64 + titleh + suckh)) - eph * 2 + yarr[0];
                    yarr[4] = yarr[2];
                    break;
                case 6:
                    xarr[0] = (Control.WINDOW_WIDTH - epw * 3) / 6;
                    xarr[1] = xarr[0] * 3 + epw;
                    xarr[2] = xarr[0] * 5 + 2 * epw;
                    xarr[3] = xarr[0];
                    xarr[4] = xarr[1];
                    xarr[5] = xarr[2];
                    yarr[0] = (Control.WINDOW_HEIGHT - (64 + titleh + suckh)) + 32 + titleh;
                    yarr[1] = yarr[0];
                    yarr[2] = yarr[0];
                    yarr[3] = (Control.WINDOW_HEIGHT - (64 + titleh + suckh)) - eph * 2 + yarr[0];
                    yarr[4] = yarr[3];
                    yarr[5] = yarr[3];
            }

            //Создаём
            ArrayList<Button> selectables = new ArrayList<>();
            for (int i = 0; i < commonEpisodes; i++) {
                Button epi = new EpisodeButton(SpritePool.get("ep" + (i + 1)), episodeSelected,
                        xarr[i],
                        yarr[i],
                        xarr[i] - 4,
                        yarr[i] - 4, i);
                selectables.add(epi);
            }

            Button suckingb = new Button(suckingSprite, suckingSelected,
                    (Control.WINDOW_WIDTH - suckingSprite.getW()) / 2,
                    Control.WINDOW_HEIGHT - 16 - suckingSprite.getH(),
                    (Control.WINDOW_WIDTH - suckingSprite.getW()) / 2 - 4,
                    Control.WINDOW_HEIGHT - 16 - suckingSprite.getH() - 4) {
                @Override
                public void onPress() throws ControlException {

                }
            };

            //устанавливаем selectable'сы
            switch (commonEpisodes) {
                case 1:
                    selectables.get(0).setMarkUp(selectables.get(0), selectables.get(0),
                            suckingb, suckingb);
                    suckingb.setMarkUp(suckingb, suckingb, selectables.get(0), selectables.get(0));
                    break;
                case 2:
                    selectables.get(0).setMarkUp(selectables.get(1), selectables.get(1),
                            suckingb, suckingb);
                    selectables.get(1).setMarkUp(selectables.get(0), selectables.get(0),
                            suckingb, suckingb);
                    suckingb.setMarkUp(suckingb, suckingb, selectables.get(1), selectables.get(1));
                    break;
                case 3:
                    selectables.get(0).setMarkUp(selectables.get(2), selectables.get(1),
                            suckingb, suckingb);
                    selectables.get(1).setMarkUp(selectables.get(0), selectables.get(2),
                            suckingb, suckingb);
                    selectables.get(2).setMarkUp(selectables.get(1), selectables.get(0),
                            suckingb, suckingb);
                    break;
                case 4:
                    selectables.get(0).setMarkUp(selectables.get(2), selectables.get(2),
                            suckingb, selectables.get(1));
                    selectables.get(1).setMarkUp(selectables.get(3), selectables.get(3),
                            selectables.get(0), suckingb);
                    selectables.get(2).setMarkUp(selectables.get(0), selectables.get(0),
                            suckingb, selectables.get(3));
                    selectables.get(3).setMarkUp(selectables.get(1), selectables.get(1),
                            selectables.get(2), suckingb);
                    break;
                case 5:
                    selectables.get(0).setMarkUp(selectables.get(3), selectables.get(2),
                            suckingb, selectables.get(1));
                    selectables.get(1).setMarkUp(selectables.get(4), selectables.get(2),
                            selectables.get(0), suckingb);
                    selectables.get(2).setMarkUp(selectables.get(0), selectables.get(0),
                            suckingb, selectables.get(3));
                    selectables.get(2).setMarkUp(selectables.get(0), selectables.get(0),
                            suckingb, selectables.get(3));
                    selectables.get(3).setMarkUp(selectables.get(1), selectables.get(1),
                            selectables.get(2), suckingb);
            }
            selectables.add(suckingb);

            setSelectables(selectables, selectables.get(0));
            MusicPool.getInstance().toEpisodeSelect();

            Graphics.setBackgroundColor(1.f, 165.f/255.f, 0.f, 1.f);
        } catch (IOException e) {
            Control.getInstance().changeStateNative(new ErrorMenu("EpisodeSelect Menu init error: " +
                    e.getMessage(), new MainMenu()));
        }
    }

    @Override
    public void onExit() throws ControlException {
        Control.getInstance().changeStateNative(new MainMenu());
    }

    @Override
    public void draw() {
        title.draw((Control.WINDOW_WIDTH - title.getW()) / 2, 16);
        super.draw();
    }
}
