import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.*;
import java.util.Iterator;


public class Level {
    private int w, h;
    private int x, y;
    private ArrayList<SolidActor> solid;
    private ArrayList<MovingSolidProtoActor> movingSolidProto;
    private ArrayList<MovingSolidActor> movingSolid;
    private ArrayList<Bullet> bullets;
    private ArrayList<Ammo> ammos;
    private Player player;
    private Boss boss;

    private Sprite solidSprite;
    private Sprite solidNotKillSprite;
    //private ArrayList<EnemyActor> enemies;
    //private ArrayList<ItemActor> items;
    //private ArrayList<BulletActor> bullet;

    //private ArrayList<RectModelObject> walls;

    //private ArrayList<DynamicRectObject> movingWalls;
    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public Level(int id){
        StringBuilder path = new StringBuilder(17);
        path.append("/DATA");
        //path.append(File.separatorChar);
        path.append("/LVL/");
        //path.append(File.separatorChar);
        path.append(id);
        path.append(".dat");
        //File file = new File(path.toString());

        try {
            //FileInputStream stream = new FileInputStream(file);
            InputStream stream = Main.class.getResourceAsStream(path.toString());
            DataInputStream data = new DataInputStream(stream);
            //terrain
            x = data.readInt();
            y = data.readInt();
            w = data.readInt();
            h = data.readInt();
            data.readInt();
            int state = data.readInt();
            int bulletCount = data.readInt(), bombCount = data.readInt();
            //количество классов объектов
            int classCount = data.readInt();
            solid = new ArrayList<>();
            movingSolidProto = new ArrayList<>();
            movingSolid = new ArrayList<>();
            int n;

            solidSprite = new Sprite(1.0f, 0.0f, 0.0f);
            solidNotKillSprite = new Sprite(0.0f, 1.0f, 0.0f);

            for (int j = 0; j < classCount; j++) {
                switch (data.readInt()) {
                    case 0:
                        n = data.readInt();
                        for (int i = 0; i < n; i++) {
                            byte f;
                            solid.add(new SolidActor(data.readInt(), data.readInt(),
                                    data.readInt(), data.readInt(), f = data.readByte(),
                                    (f & SolidActor.PLAYER_NOT_KILLER) != 0 ? solidNotKillSprite : solidSprite));
                            data.readInt();
                        }
                        break;
                    case 1:
                        n = data.readInt();
                        for (int i = 0; i < n; i++)
                            movingSolidProto.add(new MovingSolidProtoActor(data.readInt(), data.readInt(), data.readInt(),
                                    data.readInt(), data.readByte(), data.readInt() == 0 ? solidSprite : solidSprite, data.readByte(),
                                    data.readInt(), data.readInt(), data.readInt(), data.readInt()));
                        break;
                    case 2:
                        n = data.readInt();
                        for (int i = 0; i < n; i++) {
                            switch (data.readInt()) {
                            case 0:
                                boss = new Boss1m(data.readInt(), data.readInt());
                                break;
                            case 1:
                                boss = new Boss1b(data.readInt(), data.readInt());
                            }
                        }
                }
            }
            stream.close();
            if ((state & Player.WRAPPING_V) == 0)
                player = new Player(x, y, state, bulletCount, bombCount);
            else
                player = new PlayerWrapping(x, y, state, bulletCount, bombCount, w, h, 1);

            bullets = new ArrayList<>();
            ammos = new ArrayList<>();
            restart();
        } catch (Exception e) {
            throw new RuntimeException("LEVEL CORRUPTED", e);
        }
    }

    public void restart() {
        player.restart();
        movingSolid.clear();
        bullets.clear();
        ammos.clear();
        for (MovingSolidProtoActor i : movingSolidProto)
            movingSolid.add(i.generate());
        if (boss != null)
            boss.restart();
    }

    public int updViewX() {
        if (player.getX() > Game.SCREEN_WIDTH/4 - player.getW()/2)
            if (player.getX() < w - Game.SCREEN_WIDTH * 3 / 4 - player.getW()/2)
                return player.getX() - Game.SCREEN_WIDTH/4 + player.getW()/2;
            else
                return w - Game.SCREEN_WIDTH;
        else
            return 0;
    }

    public int updViewY() {
        if (player.getY() > Game.SCREEN_HEIGHT/2 - player.getH()/2)
            if (player.getY() < h - Game.SCREEN_HEIGHT/2 - player.getH()/2)
                return -Game.SCREEN_HEIGHT/2 + player.getH()/2 + player.getY();
            else
                return -Game.SCREEN_HEIGHT + h;
        else
            return 0;
    }

    public void draw() {
        for (SolidActor i : solid)
            i.draw();
        for (MovingSolidActor i : movingSolid)
            i.draw();
        for (Bullet i : bullets)
            i.draw();
        for (Ammo i : ammos)
            i.draw();
        player.draw();
        if (boss != null)
            boss.draw();
    }

    public void update(Input input) {
        //обновление игрока
        player.update();
        if (input.getKey(Input.KEY_UP))
            player.updatePress();
        else
            player.updateRelease();
        if (input.getKey(Input.KEY_SHOOT) && !input.getPrev(Input.KEY_SHOOT))
            player.shoot(this);
        //движущиеся стены
        for (MovingSolidActor i : movingSolid)
            i.update();
        //пули
        for (Bullet i : bullets)
            i.update();
        //патроны
        for (Ammo i : ammos)
            i.update();
        //босс
        if (boss != null)
            boss.update(this);

        //столкновения движущихся стен со стенами
        for (Iterator<MovingSolidActor> it = movingSolid.iterator(); it.hasNext();) {
            MovingSolidActor i = it.next();
            if (i.outerRect(w, h)) {
                it.remove();
                continue;
            }

            for (SolidActor j : solid)
                if (j.testCollision(i))
                    i.collisionSolidActor(j);
        }

        //столкновение босса со стенами
        if (boss != null)
            for (SolidActor j : solid)
                if (j.testCollision(boss))
                    boss.collisionSolidActor(j);

        for (Iterator<Ammo> it = ammos.iterator(); it.hasNext();) {
            Ammo i = it.next();
            if (i.outerRect(w, h)) {
                it.remove();
                continue;
            }

            if (player.testCollision(i)) {
                player.addBullet(i.getBulletCount());
                it.remove();
            }
        }

        //столкновения пуль
        bulletLabel:
        for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();) {
            Bullet i = it.next();
            if (i.outerRect(w, h)) {
                it.remove();
                continue;
            }

            for (SolidActor j : solid)
                if (j.testCollision(i)) {
                    it.remove();
                    continue bulletLabel;
                }

            for (MovingSolidActor j : movingSolid)
                if (j.testCollision(i)) {
                    it.remove();
                    continue bulletLabel;
                }

            if (boss != null && i.testCollision(boss)) {
                boss.bulletCollision(this, i);
                it.remove();
                continue;
            }
        }

        for (SolidActor i : solid)
            if (player.testCollision(i))
                if (i.isPlayerKiller())
                    restart();
                else
                    player.onCollisionSolidNK(i);

        for (MovingSolidActor i : movingSolid)
            if (player.testCollision(i))
                if (i.isPlayerKiller()) {
                    restart();
                    return;
                }
                else
                    player.onCollisionSolidNK(i);
    }

    public boolean isEnded() {
        if ((player.getState() & Player.WRAP_NOT_WIN) == 0)
            return player.outerRect(w, h);
        else
            return boss.isEnd();
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void addBulletCount(int c) {
        player.addBullet(c);
    }

    public void addMovingSolid(MovingSolidActor ms) {
        movingSolid.add(ms);
    }

    public void addAmmo(Ammo ammo) {
        ammos.add(ammo);
    }

    public Sprite getSolidSprite() {
        return solidSprite;
    }
}
