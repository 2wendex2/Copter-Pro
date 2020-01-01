package game;

import java.util.ArrayList;

public class EnemyManager {
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<ProtoEnemy> protoEnemies = new ArrayList<>();

    public EnemyManager() {}

    public void collisionItems(ItemManager im, Level level) {
        for (int i = 0; i < enemies.size(); i++) {
            im.collisionEnemy(enemies.get(i));
            if (enemies.get(i).isDeath() || enemies.get(i).outerRect(level.getW(), level.getH())) {
                enemies.set(i, enemies.get(enemies.size() - 1));
                enemies.remove(enemies.size() - 1);
            }
        }
    }

    public void collisionWalls(WallManager wm, Level level) {
        for (Enemy i : enemies)
            wm.collisionEnemy(i);
    }

    public void addProtoEnemy(ProtoEnemy pe) {
        protoEnemies.add(pe);
    }

    public void update() {
        for (Enemy i : enemies)
            i.update();
    }

    public void restart() {
        enemies.clear();
        for (ProtoEnemy i : protoEnemies)
            enemies.add(i.generate());
    }

    public void draw() {
        for (Enemy i : enemies)
            i.draw();
    }
}
