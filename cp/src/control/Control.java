package control;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

/*
Control — главный объект игры. Инициализирует все системы
- Для инициализации вызвать Control.getInstance().init(первое состояние)
    При этом init состояния вызывается в этой функции
- Для запуска цикла Control.getInstance().loop()
*/

public class Control {
    private static Control ourInstance = new Control();

    public static Control getInstance() {
        return ourInstance;
    }

    public static final int TARGET_UPS = 60;
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String WINDOW_TITLE = "Copter Pro";

    private long window;
    private ControlState controlState = null;
    private ControlState newControlState = null;

    private Control() {}

    public void init(ControlState state) throws ControlException {
        controlState = state;
        newControlState = state;

        GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwSetWindowCloseCallback(window, (long window1) -> getInstance().changeState(null));

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, WINDOW_WIDTH, WINDOW_HEIGHT, 0.0, 1.0, -1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        state.init();
    }

    public void changeState(ControlState state) {
        newControlState = state;
    }

    public void changeInput(GLFWKeyCallbackI keyCallback) {
        GLFW.glfwSetKeyCallback(window, keyCallback);
    }

    public void setBackgroundColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public ControlState getState() {
        return controlState;
    }

    public void loop() throws ControlException {
        double lastLoopTime = GLFW.glfwGetTime();
        final double interval = 1. / TARGET_UPS;
        double accumulator = interval;

        for (;;) {
            if (controlState != newControlState) {
                controlState.destroy();
                controlState = newControlState;
                if (controlState == null)
                    break;
                controlState.init();
            }

            {
                double time = GLFW.glfwGetTime();
                double delta = time - lastLoopTime;
                lastLoopTime = time;
                accumulator += delta;
            }

            while (accumulator >= interval) {
                controlState.input();
                controlState.update();
                accumulator -= interval;
            }


            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            controlState.draw();

            GLFW.glfwSwapBuffers(window);
            {
                double time = GLFW.glfwGetTime();
                double sleepTime = interval - time + lastLoopTime;
                if (sleepTime > 0)
                    try {
                        Thread.sleep((long) Math.ceil(sleepTime * 1000));
                    } catch (InterruptedException e) {
                        changeState(null);
                    }
            }
        }
    }

    public void destroy() {
        if (window != 0)
            GLFW.glfwDestroyWindow(window);
    }
}

