package game;

public class Level {
    private int w, h;
    private WallManager wm;
    private ItemManager im;
    private EnemyManager em;
    private Player player;
    private boolean isDeath = false;

    /*private int vx = 0;
    private int vvx = 0;
    private boolean redirectionX = false;
    private boolean vxdirection = false;*/

    public Level(int w, int h, WallManager wm, ItemManager im, EnemyManager em, Player player) {
        this.w = w;
        this.h = h;
        this.wm = wm;
        this.im = im;
        this.em = em;
        this.player = player;
    }

    public static Level getInstance() {
        return Game.getInstance().getLevel();
    }

    public void changeViewXDirection() {
        /*vx = 0;
        vvx = updViewX();
        redirectionX = true;*/
    }

    public WallManager getWallManager() {return wm;}



    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public ItemManager getItemManager() {return im;}

    public Player getPlayer() {return player;}

    void death() {
        isDeath = true;
    }

    public void restart() {
        player.restart();
        wm.restart();
        im.restart();
        em.restart();

        /*vx = 0;
        vvx = 0;
        redirectionX = false;
        vxdirection = false;*/
    }

    public int updViewX() {
        //if (!vxdirection) {
            if (player.getX() > Game.SCREEN_WIDTH / 4 - player.getW() / 2)
                if (player.getX() < w - Game.SCREEN_WIDTH * 3 / 4 - player.getW() / 2) {
                    return player.getX() - Game.SCREEN_WIDTH / 4 + player.getW() / 2;
                } else
                    return w - Game.SCREEN_WIDTH;
            else
                return 0;
        /*} else {
            if (player.getX() > Game.SCREEN_WIDTH * 3 / 4 - player.getW() / 2)
                if (player.getX() < w - Game.SCREEN_WIDTH  / 4 - player.getW() / 2)
                    return player.getX() - Game.SCREEN_WIDTH * 3 / 4 + player.getW() / 2;
                else
                    return w - Game.SCREEN_WIDTH;
            else
                return 0;
        }*/
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
        player.draw();
        wm.draw();
        im.draw();
        em.draw();
    }

    public void update(Input input) {
        if (isDeath) {
            restart();
            isDeath = false;
        }

        //обновление игрока
        player.update(input);
        wm.update();
        em.update();
        im.update();

        wm.collisionUpdate(this);
        em.collisionWalls(wm, this);
        em.collisionItems(im, this);
        im.collisionPlayer(player);
        im.collisionWalls(wm);
        wm.collisionPlayer(player);
    }
}
