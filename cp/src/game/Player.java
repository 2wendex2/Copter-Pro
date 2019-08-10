package game;

import control.Drawable;
import control.Graphics;
import game.Level;

public class Player extends Collidable.Rect implements Drawable {
    private int vspeed; //fixed 1 = 1/32
    private final static int GRAVITY = 9;
    protected int yGrav;
    private int xStart, yStart;
    private int xp, yp;
    private int bulletTimer = 0;
    private int bulletCount, bombCount;
    private int bulletStart, bombStart;

    public static final int WRAPPING_V = 1 << 0;
    public static final int NOT_MOVING = 1 << 1;
    public static final int WRAP_NOT_WIN = 1 << 2;
    public static final int BULLET = 1 << 3;

    public static final int RIGHT_LEFT = 1 << 12;
    public static final int UP_N_DOWN = 1 << 7;
    public static final int GRAVITY_NEG = 1 << 4;
    public static final int REVERSE_DIRECTION = 1 << 5;
    public static final int BOOST = 1 << 6;

    public static final int BOMB = 1 << 8;
    public static final int WRAPPING_H = 1 << 9;
    public static final int REVERSE = 1 << 10;
    public static final int NEG_MOVE = 1 << 11;


    private int state;

    public Player(int xA, int yA, int state, int bulletCount, int bombCount) {
        super(xA, yA, 150, 96);
        this.state = state;
        yGrav = y * 32;
        xStart = xA;
        yStart = yA;
        xp = xA;
        yp = yA;
        this.bulletCount = bulletCount;
        this.bombCount = bombCount;
        bulletStart = bulletCount;
        bombStart = bombCount;
    }

    public void restart() {
        x = xStart;
        y = yStart;
        xp = x;
        yp = y;
        yGrav = y*32;
        vspeed = 0;
        bulletCount = bulletStart;
        bombCount = bombStart;
    }

    public void update(){
        xp = x;
        yp = y;
        if (bulletTimer > 0)
            bulletTimer--;
        if ((state & NOT_MOVING) == 0) {
            if ((state & REVERSE_DIRECTION) == 0) {
                if (((state & NEG_MOVE) == 0))
                    x += 4;
                else
                    x -= 4;
            }
            else {
                if (((state & NEG_MOVE) == 0))
                    y += 4;
                else
                    y -= 4;
            }
        }
    }

    public void updateRelease(){
        if ((state & GRAVITY_NEG) == 0) {
            vspeed += GRAVITY;
            if (vspeed > 1024)
                vspeed = 1024;
        } else {
            vspeed -= GRAVITY;
            if (vspeed < -1024)
                vspeed = -1024;
        }
        yGrav += vspeed;
        if ((state & REVERSE_DIRECTION) == 0)
            y = yGrav / 32;
        else
            x = yGrav / 32;
    }

    public void updatePress(){
        if ((state & UP_N_DOWN) == 0) {
            if ((state & GRAVITY_NEG) == 0) {
                vspeed = -96;
                yGrav -= 128;
            } else {
                vspeed = 96;
                yGrav += 128;
            }
            if ((state & REVERSE_DIRECTION) == 0)
                y = yGrav / 32;
            else
                x = yGrav / 32;
        } else
            updateRelease();
    }

    public void draw() {
        Graphics.drawColorRect(x, y, w, h, 0.f, 0.f, 1.f);
    }

    public void onCollisionSolidNK(SolidActor solid) {
        int yd = y, xd = x;
        if ((state & REVERSE_DIRECTION) == 0) {
            y = yp;
            if (testCollision(solid)) {
                if (x - xp < 0)
                    x = solid.x + solid.w;
                else
                    x = solid.x - w;
            }

            y = yd;
            if (testCollision(solid)) {
                if (y - yp < 0) {
                    y = solid.y + solid.h;
                    vspeed = 0;
                }
                else {
                    x = xp;
                    y = solid.y - h;
                }
            }

            yGrav = y * 32;
        } else {
            x = xp;
            if (testCollision(solid)) {
                if (y - yp < 0)
                    y = solid.y + solid.h;
                else
                    y = solid.y - h;
            }

            x = xd;
            if (testCollision(solid)) {
                if (x - xp < 0) {
                    x = solid.x + solid.w;
                    vspeed = 0;
                }
                else {
                    y = yp;
                    x = solid.x - w;
                }
            }

            yGrav = x * 32;
        }
    }

    public int getState() {
        return state;
    }

    public void shoot(Level level) {
        if (bulletCount != 0 && bulletTimer == 0) {
            level.addBullet(new Bullet(x + w, y + (h - 32) / 2, 32, 32, false));
            bulletTimer = 5;
            if (bulletCount > 0)
                bulletCount--;
        }
    }

    public void addBullet(int c) {
        bulletCount += c;
    }
}