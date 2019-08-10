package game;

import game.Level;

public class Boss1b extends Boss1m {
    public Boss1b(int x, int y) {
        super(x, y);
        hp = 30;
        hpStart = 30;
    }

    @Override
    public void bulletCollision(Level level, Bullet bullet) {
        level.addAmmo(new Ammo(bullet.getX() + 16, bullet.getY() + 8, 2));
        hp--;
    }
}
