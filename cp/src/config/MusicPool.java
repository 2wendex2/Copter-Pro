package config;

import control.Control;
import control.Sound;
import org.lwjgl.system.CallbackI;

import java.io.IOException;

//Объект контролирует запуск и удаление всей музыки на протяжении всей игры
//С приставками to вызывать при первой инициализации
//С in при последующих (они не бросают исключений, при этом до вызова должен быть вызван to)
//to загружают, in переключают (между ними не вызывать других функций (но вообще же они вызываются, если мы из
//ошибки возвращаемся))
//вся суть была в том, чтобы исправить эту ошибку. И теперь это превратилось в немодифицируемое говно.
//Но типа init в этой игре второй раз вызывается только после ошибки
//Хотелось бы чтобы было не так, но пока это так. Музыка может похериться с вызовом других.
//Вообще вызов init после ошибок говно
//Но я не буду это менять, я чтобы сделать так пиздец как охуел, а вы мне говорите меняй
//Сами меняйте


//Коротко
//Вся музыка идентефицируется строкой из двух символов
//Первый: номер эпизода, от 0 до 7
//Второй: идентификатор самой музыки, может быть l, m, h, b, e
//Означает соответственно: low, middle, high, boss, end

public class MusicPool {
    //instance — синглетоновый экземпляр
    //music — массив загруженных мелодий
    //curMusic — текущая песня (если нет, то null)

    private static MusicPool instance = new MusicPool();
    private Sound[] music;
    private Sound curMusic;
    private Sound successLoaded = null;
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

    public void inMainMenu() {
        try {
            toMainMenu();
        } catch (IOException e) {}
    }

    public void inBeginGame() {
        try {
            toBeginGame();
        } catch (IOException e) {}
    }

    public void inLevelSelect(int episode, int levelNum) {
        try {
            toLevelSelect(episode, levelNum);
        } catch (IOException e) {}
    }

    public void inEpisodeSelect() {
        try {
            toEpisodeSelect();
        } catch (IOException e) {}
    }

    public void inGame(int level) {
        try {
            toGame(level);
        } catch (IOException e) {}
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

    public void stopMusic() {
        Control.getInstance().stopBgm();
        curMusic = null;
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