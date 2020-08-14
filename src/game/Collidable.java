package game;

public interface Collidable {
    boolean testCollision(Collidable other);
    boolean testCollisionRect(Rect other);

    interface RectI extends Collidable {
        int getH();
        int getW();
        int getX();
        int getY();
        boolean outerRect(int ws, int hs);
    }

    class Rect implements RectI {
        public boolean testCollision(Collidable other) {
            return other.testCollisionRect(this);
        }

        protected int x, y, w, h;

        public boolean testCollisionRect(Rect other) {
            int r = x + w - 1, b = y + h - 1;
            int r2 = other.x + other.w - 1, b2 = other.y + other.h - 1;

            if (r2 >= x && r >= other.x && b2 >= y && b >= other.y) {
                return true;
            }
            return false;
        }

        public Rect(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public int getH() {
            return h;
        }

        public int getW() {
            return w;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean outerRect(int ws, int hs) {
            return x > ws || x + w < 0 || y > hs || y + h < 0;
        }
    }
}
