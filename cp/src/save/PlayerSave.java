package save;

import control.ControlException;

import java.io.*;

public class PlayerSave {
    private String name;
    private byte episodesMedals;
    private byte[] completed = new byte[6];
    //битмап

    /*
    Хранение сейвов:
    Прогресс хранится в episodesMedals
    Если игрок хоть раз запустил игру, она больше 0
    Затем определяет общее количество медалей, до 19 включительно
    Затем прогресс эпизода сосания
    20 - уровень с медалями пройден
    21 - игра пройдена
     */

    public PlayerSave(String name) {
        this.name = name;
        try {
            read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getGameStarted() {
        return episodesMedals > 0;
    }

    public int getOpenedEpisodesCount() {
        int rv = (episodesMedals - 1) / 3;
        if (rv > 6)
            rv  = 6;
        return rv;
    }

    public int getEpisodeProgress(int ep) {
        return completed[ep];
    }

    public boolean getGameEnded() {
        return episodesMedals == 21;
    }

    private String getFilePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(ProgramPath.get());
        sb.append(File.separatorChar);
        sb.append("saves");
        sb.append(File.separatorChar);
        sb.append(name);
        sb.append(".sav");
        return sb.toString();
    }

    private String getFolderPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(ProgramPath.get());
        sb.append(File.separatorChar);
        sb.append("saves");
        return sb.toString();
    }

    public void read() throws IOException {
        if (!fileCheckSoft()) {

            write();
            return;
        }

        System.out.println(getFilePath());
        FileInputStream stream = new FileInputStream(getFilePath());
        DataInputStream dataStream = new DataInputStream(stream);
        episodesMedals = dataStream.readByte();
        for (int i = 0; i < 6; i++)
            completed[i] = dataStream.readByte();
        dataStream.close();
        stream.close();
    }

    public void write() throws IOException{
        FileOutputStream stream = new FileOutputStream(getFilePath());
        DataOutputStream dataStream = new DataOutputStream(stream);
        dataStream.writeByte(episodesMedals);
        for (int i = 0; i < 6; i++)
            dataStream.writeByte(completed[i]);
        dataStream.close();
        stream.close();
    }

    private boolean fileCheckSoft() {
        File file = new File(getFilePath());
        if (file.exists())
            return true;

        file = new File(getFolderPath());
        if (file.exists())
            return false;

        file.mkdir();
        return false;
    }
}
