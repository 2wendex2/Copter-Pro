package control;

import org.lwjgl.opengl.GL11;

public class ControlNative {
    @
    static void glThrowIfError() throws ControlException {
        int e = GL11.glGetError();
        if (e == GL11.GL_NO_ERROR)
            return;

        StringBuilder s = new StringBuilder();
        s.append("OpenGL error: ");
        s.append(e);
        for (e = GL11.glGetError(); e != GL11.GL_NO_ERROR; e = GL11.glGetError()){
            s.append(", ");
            s.append(e);
        }

        throw new ControlException(s.toString());
    }
}
