package config;

import control.Control;
import control.Sound;

import java.io.IOException;

//Объект контролирует запуск и удаление всей музыки на протяжении всей игры

//Коротко
//Вся музыка идентефицируется строкой из двух символов
//Первый: номер эпизода, от 0 до 7
//Второй: идентификатор самой музыки, может быть l, m, h, b, e
//Означает соответственно: low, middle, high, boss, end

public class MusicPool {
    private static MusicPool instance = new MusicPool();
    private Sound[] music;
    private Sound curMusic;
    protected MusicPool() {}

    public static MusicPool getInstance() {return instance;}

    public void initIfAudioInit() {
        music = new Sound[5*8];
    }

    private int musicNum(String s) {
        int r = Character.digit(s.charAt(0), 10) * 5;
        switch (s.charAt(1)) {
            case 'm':
                r += 1;
                break;
            case 'h':
                r += 2;
                break;
            case 'b':
                r += 3;
                break;
            case 'e':
                r += 4;
        }

        return r;
    }

    private void changeMusic(String newMusic) throws IOException {
        try {
            if (music != null) {
                int num = musicNum(newMusic);
                if (music[num] == null)
                    music[num] = new Sound(newMusic);
                if (curMusic != music[num])
                    Control.getInstance().changeBgm(music[num]);
                curMusic = music[num];
            }
        } catch (IOException e) {
            Control.getInstance().stopBgm();
            curMusic = null;
            throw e;
        }
    }

    public void toMainMenu() throws IOException {
        changeMusic("0l");
    }

    public void toBeginGame() throws IOException {
        changeMusic("1l");
    }

    public void toLevelSelect(int episode, int levelNum) throws IOException {
        if (episode < 7) {
            if (levelNum < 10)
                changeMusic(episode + "l");
            else
                changeMusic(episode + "h");
        } else {
            changeMusic("7m");
        }
    }

    public void toEpisodeSelect() throws IOException {
        changeMusic("0h");
    }

    public void toGame(int level) throws IOException {
        int episode = (level - 1) / 19 + 1;
        int levelInEpisode = (level - 1) % 19 + 1;
        if (episode < 7) {
            if (levelInEpisode < 9)
                changeMusic(episode + "l");
            else if (levelInEpisode == 9)
                changeMusic(episode + "m");
            else if (levelInEpisode == 18)
                changeMusic(episode + "b");
            else if (levelInEpisode == 19)
                changeMusic(episode + "e");
            else
                changeMusic(episode + "h");
        } else {
            if (levelInEpisode == 1)
                changeMusic(episode + "h");
            else
                changeMusic(episode + "b");
        }
    }
}