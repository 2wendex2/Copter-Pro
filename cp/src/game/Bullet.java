package game;

import control.Graphics;

public class Bullet extends SimpleMoving implements EnemyItem {
    public Bullet(int x, int y) {
        super(x, y, 32, 32, 7*32, 0);
    }

    public void collisionEnemy(Enemy enemy) {
        enemy.hurt();
        if (enemy instanceof Boss1b)
            Level.getInstance().getItemManager().addPlayerQitem(new DynamicAmmo(x + 16, y + 8, 2));
    }

    public void draw() {
        Graphics.drawColorRect(x, y, w, h, 0.f, 0.f, 1.f);
    }
}