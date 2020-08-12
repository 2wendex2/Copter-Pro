package control;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
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
    private int targetUPS;
    private int windowWidth;
    private int windowHeight;
    private String windowTitle;

    private double interval;
    private double lastLoopTime;
    private double accumulator;

    private Graphics graphics;
    private Audio audio;

    private long window;

    private ErrorCommonManager errorCommonManager;

    private ControlState controlState = null;

    public Control() {}

    void init(int targetUPS, int windowWidth, int windowHeight, String windowTitle, ErrorCommonManager errorCommonManager) throws ControlException {
        this.targetUPS = targetUPS;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowTitle = windowTitle;
        this.errorCommonManager = errorCommonManager;
        interval = 1.0 / targetUPS;

        GLFW.glfwSetErrorCallback((int error, long description) -> {
            errorCommonManager.push(new ErrorCommon("GLFW error callback: " + error + "\n" +
                    GLFWErrorCallback.getDescription(description)));
        });

        if (!GLFW.glfwInit()) {
            throw new ControlException("Control init: Unable to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(windowWidth, windowHeight, windowTitle, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new ControlException("Control init: Failed to create window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);

        graphics = new Graphics();
        audio = new Audio();
        graphics.init(windowWidth, windowHeight);
        try {
            audio.init();
        } catch (ControlException e) {
            errorCommonManager.push(new ErrorCommon("Control init: " + e.getMessage()));
        }

        GLFW.glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
            if (action == GLFW.GLFW_PRESS)
                this.controlState.keyboardKeyPress(key);
            else if (action == GLFW.GLFW_RELEASE)
                this.controlState.keyboardKeyRelease(key);
        });
    }

    public Audio getAudio() {
        return audio;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void changeState(ControlState newControlState) {
        controlState = newControlState;
        resetTimer();
    }

    private void resetTimer() {
        accumulator = interval;
        lastLoopTime = GLFW.glfwGetTime();
    }

    //Обновляет таймер
    private void updateTimer() {
        double time = GLFW.glfwGetTime();
        accumulator += time - lastLoopTime;
        lastLoopTime = time;
    }

    private void updateInput() {
        GLFW.glfwPollEvents();
    }

    public void exitFromLoop() {
        GLFW.glfwSetWindowShouldClose(window, true);
    }

    public void loop() throws ControlException {
        resetTimer();
        while (!GLFW.glfwWindowShouldClose(window)) {
            updateTimer();
            if (accumulator >= interval) {
                while (accumulator >= interval) {
                    updateInput();
                    controlState.update();
                    accumulator -= interval;
                    updateTimer();
                }

                controlState.draw();
                GLFW.glfwSwapBuffers(window);
            }

            {
                updateTimer();
                double sleepTime = interval - accumulator;
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
        if (audio != null)
            audio.destroy();

        if (graphics != null)
            graphics.destroy();

        if (window != 0) {
            Callbacks.glfwFreeCallbacks(window);
            GLFW.glfwDestroyWindow(window);
        }

        GLFW.glfwTerminate();

        GLFWErrorCallback ec = GLFW.glfwSetErrorCallback(null);
        if (ec != null)
            ec.free();
    }
}