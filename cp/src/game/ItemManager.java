package game;

import java.util.ArrayList;

public class ItemManager {
    //4 главных типа итемов
    private ArrayList<PlayerStaticItem> plSitems = new ArrayList<>();
    private ArrayList<PlayerQuickItem> plQitems = new ArrayList<>();
    private ArrayList<PlayerLongItem> plLitems = new ArrayList<>();
    private ArrayList<EnemyItem> enItems = new ArrayList<>();

    private ArrayList<PlayerStaticItem> protoSitems = new ArrayList<>();
    private ArrayList<ProtoPlayerDynamicItem> protoDitems = new ArrayList<>();

    public ItemManager() {}

    public static ItemManager getItemManager() {
        return Level.getInstance().getItemManager();
    }

    public void addEnemyItem(EnemyItem item) {
        enItems.add(item);
    }

    public void addPlayerQitem(PlayerQuickItem qitem) {
        plQitems.add(qitem);
    }

    public void addPlayerLitem(PlayerLongItem litem) {
        plLitems.add(litem);
    }

    public void addProtoSitem(PlayerStaticItem sitem) {
        protoSitems.add(sitem);
    }

    public void addProtoDitem(ProtoPlayerDynamicItem ditem) {protoDitems.add(ditem);}

    public void restart() {
        plSitems.clear();
        plLitems.clear();
        plQitems.clear();
        enItems.clear();
        SuckingWinItem.protoReset();
        for (PlayerStaticItem i : protoSitems) {
            plSitems.add(i.clone());
        }

        for (ProtoPlayerDynamicItem i : protoDitems) {
            PlayerDynamicItem ditem = i.generate();
            if (ditem instanceof PlayerLongItem)
                plLitems.add((PlayerLongItem) ditem);
            else if (ditem instanceof PlayerQuickItem)
                plQitems.add((PlayerQuickItem) ditem);
        }
    }

    public void collisionPlayer(Player player) {
        for (int i = 0; i < plSitems.size(); i++) {
            PlayerStaticItem sitem = plSitems.get(i);
            if (sitem.testCollision(player)) {
                sitem.collisionPlayer(player);
                if (sitem.deathFromPlayer()) {
                    plSitems.set(i, plSitems.get(plSitems.size() - 1));
                    plSitems.remove(plSitems.size() - 1);
                }
            }
        }
        for (int i = 0; i < plQitems.size(); i++) {
            PlayerQuickItem qitem = plQitems.get(i);
            if (qitem.testCollision(player)) {
                qitem.collisionPlayer(player);
                if (qitem.deathFromPlayer()) {
                    plQitems.set(i, plQitems.get(plQitems.size() - 1));
                    plQitems.remove(plQitems.size() - 1);
                }
            }
        }
        for (int i = 0; i < plLitems.size(); i++) {
            PlayerLongItem litem = plLitems.get(i);
            if (litem.testCollision(player)) {
                litem.collisionPlayer(player);
                if (litem.deathFromPlayer()) {
                    plLitems.set(i, plLitems.get(plLitems.size() - 1));
                    plLitems.remove(plLitems.size() - 1);
                }
            }
        }
    }

    public void collisionWalls(WallManager wm) {
        for (int i = 0; i < enItems.size(); i++) {
            EnemyItem item = enItems.get(i);
            if (wm.collisionQuickItem(item)) {
                enItems.set(i, enItems.get(enItems.size() - 1));
                enItems.remove(enItems.size() - 1);
            }
        }

        /*for (int i = 0; i < plQitems.size(); i++) {
            PlayerQuickItem qitem = plQitems.get(i);
            if (wm.collisionQuickItem(qitem)) {
                plQitems.set(i, plQitems.get(plQitems.size() - 1));
                plQitems.remove(plQitems.size() - 1);
            }
        }*/
        //TODO сделать по-нормальному

        for (PlayerLongItem i : plLitems)
            wm.collisionLongItem(i);
    }

    public void update() {
        for (EnemyItem i : enItems)
            i.update();
        for (PlayerLongItem i : plLitems)
            i.update();
        for (PlayerQuickItem i : plQitems)
            i.update();
    }

    public void collisionEnemy(Enemy enemy) {
        for (int i = 0; i < enItems.size(); i++) {
            EnemyItem item = enItems.get(i);
            if (item.testCollision(enemy)) {
                item.collisionEnemy(enemy);
                enItems.set(i, enItems.get(enItems.size() - 1));
                enItems.remove(enItems.size() - 1);
            }
        }
    }

    public void draw() {
        for (PlayerStaticItem i : plSitems)
            i.draw();
        for (EnemyItem i : enItems)
            i.draw();
        for (PlayerLongItem i : plLitems)
            i.draw();
        for (PlayerQuickItem i : plQitems)
            i.draw();
    }
}
