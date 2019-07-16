import org.lwjgl.opengl.GL11;

public class Sprite {
    protected float r, g, b;

    public Sprite(float rA, float gA, float bA) {
        r = rA;
        g = gA;
        b = bA;
    }

    public void draw(int x, int y, int w, int h){
        GL11.glColor3f(r, g, b);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x+w, y);
        GL11.glVertex2i(x, y+h);
        GL11.glVertex2i(x, y+h);
        GL11.glVertex2i(x+w, y);
        GL11.glVertex2i(x+w, y+h);
    }
}
