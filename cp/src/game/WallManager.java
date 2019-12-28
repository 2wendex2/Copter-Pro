package game;

//Менеджер стен

import java.util.ArrayList;

public class WallManager {
    private ArrayList<StaticWall> pnenSwall;
    private ArrayList<StaticWall> pkenSwall;
    private ArrayList<StaticWall> pkekSwall;
    private ArrayList<DynamicWall> pnenDwall;
    private ArrayList<DynamicWall> pkenDwall;
    private ArrayList<DynamicWall> pkekDwall;

    private ArrayList<StaticWall> protoSwall;
    private ArrayList<ProtoDynamicWall> protoDwall;

    public WallManager() {}

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
                what.add(i, what.get(what.size() - 1));
                what.remove(what.size() - 1);
                i--;
                continue;
            }

            for (Wall wall : where)
                if (wall.testCollision(dwall))
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

        collisionUpdateArrayList(level, pkekDwall, pkekDwall);
        collisionUpdateArrayList(level, pkekDwall, pkenDwall);
        collisionUpdateArrayList(level, pkekDwall, pnenDwall);
        collisionUpdateArrayList(level, pkenDwall, pkekDwall);
        collisionUpdateArrayList(level, pkenDwall, pkenDwall);
        collisionUpdateArrayList(level, pkenDwall, pnenDwall);
        collisionUpdateArrayList(level, pnenDwall, pkekDwall);
        collisionUpdateArrayList(level, pnenDwall, pkenDwall);
        collisionUpdateArrayList(level, pnenDwall, pnenDwall);
    }

    public void reset() {
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
}
