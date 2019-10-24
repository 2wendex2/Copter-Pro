package config;

import java.io.*;

public class PlayerSave {
    private String name;
    private static PlayerSave curSave;
    private byte episodes;
    private byte[] completed = new byte[7];

    //Читает сохранение name и записывает в соответствующий объект
    //Если файла нет, создаёт его
    public PlayerSave(String name) throws IOException {
        this.name = name;
        read();
    }

    //Для удобства всегда есть такое понятие как текущее сохранение
    public static PlayerSave getCurSave() {
        return curSave;
    }

    public static void setCurSave(PlayerSave curSave) {
        PlayerSave.curSave = curSave;
    }

    //Количество открытых эпизодов
    public int getOpenedEpisodesCount() {
        return episodes;
    }

    //ep — номер эпизода
    //ep0 — номер эпизода - 1 и индекс в массиве completed
    public int getCompletedLevelCount(int ep) {
        return completed[ep - 1] & 0x1F;
    }

    public boolean getSuckingOpened() {
        return episodes > 0 && completed[0] >= 18;
    }

    //Звёзды эпизода
    public int getOpenedStarsCount(int ep) {
        int count = 0;
        for (int i = 5; i < 8; i++)
            if ((completed[ep - 1] & (1 << i)) != 0)
                count++;
        return count;
    }

    public boolean getOpenedStar(int ep, int starNum) {
        return (completed[ep - 1] & (1 << (starNum + 4))) != 0;
    }

    public void openStar(int ep, int starNum) throws IOException {
        completed[ep - 1] |= (1 << (starNum + 4));
        write();
    }

    public void setLevelComplete(int id) throws IOException {
        int ep0 = id / 19;

        if (completed[ep0] < id % 19) {
            completed[ep0] = (byte)((id % 19) | (completed[ep0] & 0xE0));
            write();
        }
    }

    public void setEpisodeComplete(int ep) throws IOException {
        if (ep > episodes) {
            episodes = (byte)ep;
            write();
        }
    }




    private String getFilePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Config.getSavesDir());
        sb.append(File.separatorChar);
        sb.append(name);
        sb.append(".sav");
        return sb.toString();
    }

    public void read() throws IOException {
        if (!fileCheckSoft()) {
            write();
            return;
        }

        FileInputStream stream;
        try {
            stream = new FileInputStream(getFilePath());
        } catch (IOException e) {
            fileCheckHard(e);
            throw new IOException("Unable to read save file \"" + getFilePath() + '\"', e);
        }

        try {
            byte i = 0;
            for (; i < 7; i++) {
                int b = stream.read();
                if (b < 0)
                    break;
                completed[i] = (byte)b;
            }
            if (i == 7 && stream.read() >= 0)
                throw new IOException("Save file \"" + name + ".sav\" corrupted");

            episodes = i;

            if (!checkPossibility())
                throw new IOException("Save file \"" + name + ".sav\" corrupted");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                //Я просто хочу закрыть ёбаный поток, я что, многого прошу??
                throw new IOException("Error closing save file \"" + getFilePath() + '\"', e);
            }
        }
    }

    public void write() throws IOException{
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(getFilePath());
        } catch (IOException e) {
            fileCheckHard(e);
            throw new IOException("Unable to write save file \"" + getFilePath() + '\"', e);
        }

        try {
            for (int i = 0; i < episodes; i++)
                stream.write(completed[i]);
        } catch (IOException e) {
            throw new IOException("Error writing save file \"" + getFilePath() + '\"', e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new IOException("Error closing save file \"" + getFilePath() + '\"', e);
            }
        }
    }

    //лёгкая проверка, вдруг получится
    private boolean fileCheckSoft() {
        File file = new File(getFilePath());
        if (file.exists())
            return true;

        file = new File(Config.getSavesDir());
        if (file.exists())
            return false;

        file.mkdirs();
        return false;
    }

    private void fileCheckHard(Throwable cause) throws IOException {
        File savesDir = new File(Config.getSavesDir());
        if (!savesDir.exists())
            throw new IOException("Unable to create saves folder \"" + Config.getSavesDir() + '\"', cause);

        if (!savesDir.isDirectory())
            throw new IOException("Saves folder path \"" + Config.getSavesDir() + "\" is not folder", cause);

        File saveFile = new File(getFilePath());
        if (!saveFile.exists())
            throw new IOException("Unable to create file \"" + getFilePath() + '\"', cause);

        if (!saveFile.isFile())
            throw new IOException("Save file path \"" + getFilePath() + "\" is not file", cause);
    }

    private boolean checkPossibility() {
        //для каждого эпизода всё проверять отдельно
        //medals это stars
        //TODO добавить проверку звёзд
        if (episodes < 1) return true;
        int comp = completed[0] & 31;
        int medals = completed[0] & 224;
        if (comp < 0 || comp > 19 || episodes > 1 && medals != 224 ||
                comp < 19 && completed[6] == 2) return false;

        if (episodes < 2) return true;
        comp = completed[1] & 31;
        medals = completed[1] & 224;
        if (comp < 0 || comp > 19 || episodes > 2 && medals != 224 ||
                comp < 19 && completed[6] == 2) return false;

        if (episodes < 3) return true;
        comp = completed[2] & 31;
        medals = completed[2] & 224;
        if (comp < 0 || comp > 19 || episodes > 3 && medals != 224 ||
                comp < 19 && completed[6] == 2) return false;

        if (episodes < 4) return true;
        comp = completed[3] & 31;
        medals = completed[3] & 224;
        if (comp < 0 || comp > 19 || episodes > 4 && medals != 224 ||
                comp < 19 && completed[6] == 2) return false;

        if (episodes < 5) return true;
        comp = completed[4] & 31;
        medals = completed[4] & 224;
        if (comp < 0 || comp > 19 || episodes > 5 && medals != 224 ||
                comp < 19 && completed[6] == 2) return false;

        if (episodes < 6) return true;
        comp = completed[0] & 31;
        medals = completed[0] & 224;
        if (comp < 0 || comp > 19 || episodes > 6 && medals != 224 ||
                comp < 19 && completed[6] == 2) return false;

        if (episodes < 7) return true;
        comp = completed[6];
        if (comp < 0 || comp > 2) return false;

        return true;
    }
}
