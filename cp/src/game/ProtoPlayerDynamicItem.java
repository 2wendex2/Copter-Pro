package game;

import java.io.IOException;

public class ProtoPlayerDynamicItem {
    int x, y, state;

    public ProtoPlayerDynamicItem(int x, int y, int state) throws IOException {
        this.x = x;
        this.y = y;
        this.state = state;
        if (state > 0 || state < 0)
            throw new IOException("Unknown player dynamic item type");
    }

    public PlayerDynamicItem generate() {
        return null;
    }
}
