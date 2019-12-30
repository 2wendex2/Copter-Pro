package game;

import game.Level;

public class Boss1b extends Boss1m {
    public Boss1b(int x, int y) {
        super(x, y);
        hp = 30;
    }

    public void hurt() {
        hp--;
        if (hp < 0)
            Game.getInstance().levelComplete();
    }
}
