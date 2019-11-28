package control;

import org.lwjgl.opengl.GL11;

public class Graphics {
    public static void setBackgroundColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public static void changeView(int x, int y) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y,0);
    }

    public static void setColor(float r, float g, float b) {
        GL11.glColor3f(r, g, b);
    }

    public static void drawColorRect(int x, int y, int w, int h, float r, float g, float b) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL11.glColor3f(r, g, b);
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
}
