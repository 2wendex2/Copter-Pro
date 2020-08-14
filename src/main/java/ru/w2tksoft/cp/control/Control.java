package ru.w2tksoft.cp.control;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

/**
* Главная подсистема. Управляет состояниями, игровым циклом и остальными подсистемами
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

    /**
     * Создаёт пустой объект Control
     */
    public Control() {}

    /**
     * Инициализирует объект Control
     * @param targetUPS необходимое количество обновлений в секунду
     * @param windowWidth ширина окна
     * @param windowHeight высота окна
     * @param windowTitle название окна
     * @param errorCommonManager менеджер рядовых ошибок, в который Control будет записывать нефатальные ошибки
     * @throws ControlException бросает, если инициализация не удалась, после этого Control остаётся пустым
     */
    void init(int targetUPS, int windowWidth, int windowHeight, String windowTitle, ErrorCommonManager errorCommonManager) throws ControlException {
        this.targetUPS = targetUPS;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowTitle = windowTitle;
        this.errorCommonManager = errorCommonManager;
        interval = 1.0 / targetUPS;

        GLFW.glfwSetErrorCallback((int error, long description) -> {
            errorCommonManager.add(new ErrorCommon("GLFW error callback: " + error + "\n" +
                    GLFWErrorCallback.getDescription(description)));
        });

        if (!GLFW.glfwInit()) {
            throw new ControlException("Control init: Unable to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(windowWidth, windowHeight, windowTitle, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new ControlException("Control init: Failed to create window");
        }

        try {
            GLFW.glfwMakeContextCurrent(window);
            GLFW.glfwSwapInterval(1);

            graphics = new Graphics();
            audio = new Audio();

            graphics.init(windowWidth, windowHeight);
            try {
                audio.init();
            } catch (ControlException e) {
                errorCommonManager.add(new ErrorCommon("Control init: " + e.getMessage()));
            }

            GLFW.glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
                if (action == GLFW.GLFW_PRESS)
                    this.controlState.keyboardKeyPress(key);
                else if (action == GLFW.GLFW_RELEASE)
                    this.controlState.keyboardKeyRelease(key);
            });
        } catch (Exception e) {
            window = MemoryUtil.NULL;
            throw e;
        }
    }

    /**
     * Доступ к звуковой подсистеме
     * @return возвращает объект Audio
     */
    public Audio getAudio() {
        return audio;
    }

    /**
     * Доступ к графической подсистеме
     * @return возвращает объект Graphics
     */
    public Graphics getGraphics() {
        return graphics;
    }

    /**
     * Меняет состояние
     * @param newControlState новое состояние
     */
    public void changeState(ControlState newControlState) {
        controlState = newControlState;
        resetTimer();
    }

    private void resetTimer() {
        accumulator = interval;
        lastLoopTime = GLFW.glfwGetTime();
    }

    private void updateTimer() {
        double time = GLFW.glfwGetTime();
        accumulator += time - lastLoopTime;
        lastLoopTime = time;
    }

    private void updateInput() {
        GLFW.glfwPollEvents();
    }

    /**
     * Сообщает о необходимости выхода из игрового цикла; после конца текущей итерации цикл будет покинут
     */
    public void exitFromLoop() {
        GLFW.glfwSetWindowShouldClose(window, true);
    }

    /**
     * Игровой цикл, выход из этой функции будет совершён только после его окончания.
     * Перед началом необходимо установить состояние с помощью changeState
     */
    public void loop() {
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

    /**
     * Уничтожает и объект и делает его пустым, после этого объект может быть иницаилизирован снова.
     * Может быть вызван от пустого объекта
     */
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