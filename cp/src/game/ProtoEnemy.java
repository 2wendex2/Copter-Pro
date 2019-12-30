package game;

import java.io.IOException;

public class ProtoEnemy {
    int x, y, type;

    public ProtoEnemy(int x, int y, int type) throws IOException {
        this.x = x;
        this.y = y;
        this.type = type;
        if (type > 1 || type < 0)
            throw new IOException("Unknown enemy type");
    }

    public Enemy generate() {
        switch (type) {
            case 0:
                return new Boss1m(x, y);
            case 1:
                return new Boss1b(x, y);
        }
        return null;
    }
}
