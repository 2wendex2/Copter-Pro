package game;

import config.PlayerSave;
import control.Resourse;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LevelLoader {
    DataInputStream data;
    private int w, h;
    private int id;
    private WallManager wm = new WallManager();
    private ItemManager im = new ItemManager();
    private EnemyManager em = new EnemyManager();
    private Player player;
    private LevelLoader(int id) throws IOException {
        InputStream stream = Resourse.getResourseAsInputStream("LVL", String.valueOf(id));
        if (stream == null) {
            throw new IOException("Level " + id + " loading error: unable to read resourse");
        }
        data = new DataInputStream(stream);
        this.id = id;
    }

    public static Level load(int id) throws IOException {
        LevelLoader ll = new LevelLoader(id);
        ll.loadLevel();
        return new Level(ll.w, ll.h, ll.wm, ll.im, ll.em, ll.player);
    }

    //игрок
    //классы объектов
    private void loadLevel() throws IOException {
        loadPlayer();
        loadObjectClasses();
        if (data.read() != -1)
            throw new IOException("Not eof in the end");
    }

    //4 инта -- координаты
    //инт зарезервировано
    //инт состояние игрока
    private void loadPlayer() throws IOException {
        int x = data.readInt();
        int y = data.readInt();
        w = data.readInt();
        h = data.readInt();
        int stateReserved = data.readInt();
        int state = data.readInt();
        int bulletCount = data.readInt();
        int bombCount = data.readInt();

        if ((state & Player.WRAPPING_V) != 0)
            player = new PlayerWrapping(x, y, state, bulletCount, bombCount, w, h, 1);
        else if ((state & Player.WRAP_NOT_WIN) != 0)
            player = new Player(x, y, state, bulletCount, bombCount);
        else
            player = new PlayerWrapWinner(x, y, state, bulletCount, bombCount);
    }

    private void loadObjectClasses() throws IOException {
        int count = data.readInt();
        for (int i = 0; i < count; i++) {
            int objType = data.readInt();
            switch (objType) {
                case 0:
                    loadSwall();
                    break;
                case 1:
                    loadDwall();
                    break;
                case 2:
                    loadEnemy();
                    break;
                case 3:
                    loadItem();
                    break;
                default:
                    throw new IOException("Unknown object class type");
            }
        }
    }

    private void loadEnemy() throws IOException {
        int n = data.readInt();
        for (int i = 0; i < n; i++) {
            int type = data.readInt();
            int x = data.readInt();
            int y = data.readInt();

            em.addProtoEnemy(new ProtoEnemy(x, y, type));
        }
    }

    private void loadSwall() throws IOException {
        int n = data.readInt();
        for (int i = 0; i < n; i++) {
            byte f;
            wm.addProtoSwall(new StaticWall(data.readInt(), data.readInt(),
                    data.readInt(), data.readInt(), f = data.readByte()));
            data.readInt();
        }
    }

    private void loadDwall() throws IOException {
        int n = data.readInt();
        for (int i = 0; i < n; i++) {
            int f;
            wm.addProtoDwall(new ProtoDynamicWall(data.readInt(), data.readInt(), data.readInt(),
                    data.readInt(), data.readByte() , (byte)(data.readByte()+ (byte)((f = data.readInt()) * 0)),
                    data.readInt(), data.readInt(), data.readInt(), data.readInt()));
        }
    }

    private void loadItem() throws IOException {
        int n = data.readInt();
        for (int i = 0; i < n; i++) {
            int type = data.readInt();
            switch (type) {
                case 0:
                    int x = data.readInt(), y = data.readInt();
                    if (PlayerSave.getCurSave().getOpenedStar(id))
                        break;
                    im.addProtoSitem(new StarItem(x, y));
                    break;
                case 1:
                    x = data.readInt();
                    y = data.readInt();
                    im.addProtoSitem(new SuckingWinItem(x, y));
                    SuckingWinItem.protoReset();
                    break;
                default:
                    throw new IOException("Unknown item type");
            }
        }
    }
}
