package config;

import control.Control;
import control.Sound;

//Объект контролирует запуск и удаление всей музыки на протяжении всей игры

//Коротко
//MainMenu сбрасывает только Game

public class MusicPool {
    private static MusicPool instance = new MusicPool();
    private final int MAIN_MENU_STATE = 1;
    private final int BEGIN_GAME_STATE = 2;
    private final int GAME_STATE = 3;
    private Sound mainMenu;
    private Sound ep1low;
    private Sound ep1mid;
    private Sound ep1high;
    private Sound ep1boss;
    private Sound curMusic;
    private int state;
    protected MusicPool() {}

    public static MusicPool getInstance() {return instance;}

    public void toMainMenu() {
        if (mainMenu == null)
            mainMenu = new Sound("0l");
        if (curMusic != mainMenu)
            Control.getInstance().changeBgm(mainMenu);
        curMusic = mainMenu;
    }

    public void toBeginGame() {
        if (ep1low == null)
            ep1low = new Sound("1l");
        if (curMusic != ep1low)
            Control.getInstance().changeBgm(ep1low);
        curMusic = ep1low;
    }

    public void toGame(int level) {
        if (level < 9) {
            if (ep1low == null)
                ep1low = new Sound("1l");
            if (curMusic != ep1low)
                Control.getInstance().changeBgm(ep1low);
            curMusic = ep1low;
        } else if (level == 9) {
            if (ep1mid == null)
                ep1mid = new Sound("1m");
            if (curMusic != ep1mid)
                Control.getInstance().changeBgm(ep1mid);
            curMusic = ep1mid;
        } else if (level > 9 && level < 17) {
            if (ep1high == null)
                ep1high = new Sound("1h");
            if (curMusic != ep1high)
                Control.getInstance().changeBgm(ep1high);
            curMusic = ep1high;
        } else if (level == 18) {
            if (ep1boss == null)
                ep1boss = new Sound("1b");
            if (curMusic != ep1boss)
                Control.getInstance().changeBgm(ep1boss);
            curMusic = ep1boss;
        }
    }
}
