import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Graphics {
    private long window;

    public Graphics() {
        window = GLFW.glfwCreateWindow(800, 600, "Copter Pro", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

    }

    public void begin(){
        GL11.glClearColor(1.0f,1.0f,0.0f,1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glBegin(GL11.GL_TRIANGLES);
    }

    public void end(){
        GL11.glEnd();
        GLFW.glfwSwapBuffers(window);
    }
}
