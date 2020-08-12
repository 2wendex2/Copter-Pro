package control;

import org.lwjgl.opengl.GL11;

public class ColorRectSprite implements RectSprite {
    private float r, g, b;
    private int w, h;

    public ColorRectSprite(float r, float g, float b, int w, int h, Graphics graphics) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.w = w;
        this.h = h;
    }

    public void draw(int x, int y) {
        GL11.glColor3f(r, g, b);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x+w, y);
        GL11.glVertex2i(x, y+h);

        GL11.glVertex2i(x, y+h);
        GL11.glVertex2i(x+w, y);
        GL11.glVertex2i(x+w, y+h);
        GL11.glEnd();

        GL11.glColor3f(1.f, 1.f, 1.f);
    }

    @Override
    public int getH() {
        return h;
    }

    @Override
    public int getW() {
        return w;
    }
}
