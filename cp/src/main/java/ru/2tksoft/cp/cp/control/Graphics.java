package control;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Graphics {
    Graphics() {}

    void fill(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    void setView(int x, int y) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y,0);
    }

    void glThrowIfError(String errString) throws ControlException {
        int e = GL11.glGetError();
        if (e == GL11.GL_NO_ERROR)
            return;

        StringBuilder s = new StringBuilder();
        s.append(errString + ": OpenGL error: ");
        s.append(e);
        for (e = GL11.glGetError(); e != GL11.GL_NO_ERROR; e = GL11.glGetError()){
            s.append(", ");
            s.append(e);
        }

        throw new ControlException(s.toString());
    }

    void init(int windowWidth, int windowHeight) throws ControlException {
        GL.createCapabilities();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, windowWidth, windowHeight, 0.0, 1.0, -1.0);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        glThrowIfError("Graphics init");
    }

    void destroy() {}
}
