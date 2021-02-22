package game;

//Менеджер стен

import java.util.ArrayList;

public class WallManager {
    private ArrayList<StaticWall> pnenSwall = new ArrayList<>();
    private ArrayList<StaticWall> pkenSwall = new ArrayList<>();
    private ArrayList<StaticWall> pkekSwall = new ArrayList<>();
    private ArrayList<DynamicWall> pnenDwall = new ArrayList<>();
    private ArrayList<DynamicWall> pkenDwall = new ArrayList<>();
    private ArrayList<DynamicWall> pkekDwall = new ArrayList<>();

    private ArrayList<StaticWall> protoSwall = new ArrayList<>();
    private ArrayList<ProtoDynamicWall> protoDwall = new ArrayList<>();

    public WallManager() {}

    public static WallManager getWallManager() {
        return Level.getInstance().getWallManager();
    }

    public void addProtoSwall(StaticWall swall) {
        protoSwall.add(swall);
    }

    public void addProtoDwall(ProtoDynamicWall dwall) {
        protoDwall.add(dwall);
    }

    public void addDwall(DynamicWall dwall) {
        if (!dwall.isPlayerKiller()) {
            pnenDwall.add(dwall);
        } else if (dwall.isEnemyKiller()) {
            pkekDwall.add(dwall);
        } else {
            pkenDwall.add(dwall);
        }
    }

    private void collisionUpdateArrayList(Level level, ArrayList<DynamicWall> what,
                                          ArrayList<? extends Wall> where) {
        for (int i = 0; i < what.size(); i++) {
            DynamicWall dwall = what.get(i);
            if (dwall.outerRect(level.getW(), level.getH())) {
                what.set(i, what.get(what.size() - 1));
                what.remove(what.size() - 1);
                i--;
                continue;
            }

            for (Wall wall : where)
                if (wall.testCollision(dwall) && wall != dwall)
                    dwall.collisionWall(wall);
        }
    }

    public void update() {
        for (DynamicWall i : pkekDwall)
            i.update();
        for (DynamicWall i : pkenDwall)
            i.update();
        for (DynamicWall i : pnenDwall)
            i.update();
    }

    public void collisionUpdate(Level level) {
        collisionUpdateArrayList(level, pkekDwall, pkekSwall);
        collisionUpdateArrayList(level, pkekDwall, pkenSwall);
        collisionUpdateArrayList(level, pkekDwall, pnenSwall);
        collisionUpdateArrayList(level, pkenDwall, pkekSwall);
        collisionUpdateArrayList(level, pkenDwall, pkenSwall);
        collisionUpdateArrayList(level, pkenDwall, pnenSwall);
        collisionUpdateArrayList(level, pnenDwall, pkekSwall);
        collisionUpdateArrayList(level, pnenDwall, pkenSwall);
        collisionUpdateArrayList(level, pnenDwall, pnenSwall);

        collisionUpdateArrayList(level, pkekDwall, pkenDwall);
        collisionUpdateArrayList(level, pkekDwall, pnenDwall);
        collisionUpdateArrayList(level, pkenDwall, pkekDwall);
        collisionUpdateArrayList(level, pkenDwall, pnenDwall);
        collisionUpdateArrayList(level, pnenDwall, pkekDwall);
        collisionUpdateArrayList(level, pnenDwall, pkenDwall);

        //collisionUpdateArrayList(level, pnenDwall, pnenDwall);
        //collisionUpdateArrayList(level, pkenDwall, pkenDwall);
        //collisionUpdateArrayList(level, pkekDwall, pkekDwall);
    }

    public boolean collisionQuickItem(Item item) {
        for (StaticWall i : pkekSwall)
            if (item.testCollision(i))
                return true;
        for (StaticWall i : pkenSwall)
            if (item.testCollision(i))
                return true;
        for (StaticWall i : pnenSwall)
            if (item.testCollision(i))
                return true;
        for (DynamicWall i : pkekDwall)
            if (item.testCollision(i))
                return true;
        for (DynamicWall i : pkenDwall)
            if (item.testCollision(i))
                return true;
        for (DynamicWall i : pnenDwall)
            if (item.testCollision(i))
                return true;
        return false;
    }

    public void collisionLongItem(PlayerLongItem item) {
        for (StaticWall i : pkekSwall)
            if (item.testCollision(i))
                item.collisionWall(i);
        for (StaticWall i : pkenSwall)
            if (item.testCollision(i))
                item.collisionWall(i);
        for (StaticWall i : pnenSwall)
            if (item.testCollision(i))
                item.collisionWall(i);
        for (DynamicWall i : pkekDwall)
            if (item.testCollision(i))
                item.collisionWall(i);
        for (DynamicWall i : pkenDwall)
            if (item.testCollision(i))
                item.collisionWall(i);
        for (DynamicWall i : pnenDwall)
            if (item.testCollision(i))
                item.collisionWall(i);
    }

    public void collisionPlayer(Player player) {
        for (StaticWall i : pnenSwall)
            if (player.testCollision(i))
                player.onCollisionSoftWall(i);
        for (DynamicWall i : pnenDwall)
            if (player.testCollision(i))
                player.onCollisionSoftWall(i);
        for (StaticWall i : pkekSwall)
            if (player.testCollision(i))
                player.onCollisionHardWall();
        for (StaticWall i : pkenSwall)
            if (player.testCollision(i))
                player.onCollisionHardWall();
        for (DynamicWall i : pkekDwall)
            if (player.testCollision(i))
                player.onCollisionHardWall();
        for (DynamicWall i : pkenDwall)
            if (player.testCollision(i))
                player.onCollisionHardWall();
    }

    public void collisionEnemy(Enemy enemy) {
        for (StaticWall i : pkenSwall)
            if (i.testCollision(enemy))
                enemy.onCollisionSoftWall(i);
        for (StaticWall i : pnenSwall)
            if (i.testCollision(enemy))
                enemy.onCollisionSoftWall(i);
        for (DynamicWall i : pkenDwall)
            if (i.testCollision(enemy))
                enemy.onCollisionSoftWall(i);
        for (DynamicWall i : pnenDwall)
            if (i.testCollision(enemy))
                enemy.onCollisionSoftWall(i);
        for (StaticWall i : pkekSwall)
            if (i.testCollision(enemy))
                enemy.onCollisionHardWall(i);
        for (DynamicWall i : pkekDwall)
            if (i.testCollision(enemy))
                enemy.onCollisionHardWall(i);
    }

    public void collisionPlayerDitem(PlayerDynamicItem ditem) {

    }

    public void restart() {
        pnenSwall.clear();
        pkenSwall.clear();
        pkekSwall.clear();
        pnenDwall.clear();
        pkenDwall.clear();
        pkekDwall.clear();

        for (StaticWall i : protoSwall) {
            if (!i.isPlayerKiller()) {
                pnenSwall.add(i.clone());
            } else if (i.isEnemyKiller()) {
                pkekSwall.add(i.clone());
            } else {
                pkenSwall.add(i.clone());
            }
        }

        for (ProtoDynamicWall i : protoDwall) {
            DynamicWall w = i.generate();
            if (!w.isPlayerKiller()) {
                pnenDwall.add(w);
            } else if (w.isEnemyKiller()) {
                pkekDwall.add(w);
            } else {
                pkenDwall.add(w);
            }
        }
    }

    public void draw() {
        for (StaticWall i : pkekSwall)
            i.draw();
        for (StaticWall i : pkenSwall)
            i.draw();
        for (StaticWall i : pnenSwall)
            i.draw();
        for (DynamicWall i : pkekDwall)
            i.draw();
        for (DynamicWall i : pkenDwall)
            i.draw();
        for (DynamicWall i : pnenDwall)
            i.draw();
    }
}
