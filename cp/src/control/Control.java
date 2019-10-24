package control;

import config.Config;
import config.Log;
import config.MusicPool;
import config.SpritePool;
import menu.ErrorMenu;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;

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
    private static final String WINDOW_TITLE = "Copter Pro v" + Config.version;

    private long window;
    private final double INTERVAL = 1. / TARGET_UPS;
    private Audio audio;
    private ControlState controlState = null;
    private ControlState newControlState = null;
    private SoundSource bgm;
    private double lastLoopTime;
    private double accumulator;

    private Control() {}

    void init(ControlState state) throws ControlException {
        GLFW.glfwSetErrorCallback((int error, long description) -> {
            Log.println("GLFW error callback: " + error + "\n" +
                    GLFWErrorCallback.getDescription(description));
        });

        if (!GLFW.glfwInit()) {
            throw new ControlException("Unable to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            GLFW.glfwTerminate();
            throw new ControlException("Failed to create window");
        }

        GLFW.glfwSetWindowCloseCallback(window, (long window1) -> getInstance().changeState(null));

        GLFW.glfwMakeContextCurrent(window);

        GL.createCapabilities();

        GL43.glDebugMessageCallback((int source, int type, int id, int severity, int length,
                                     long message, long userParam) -> {
            System.err.println("GLFW error callback: " + "\n" +
                    GLDebugMessageCallback.getMessage(length, message));
        }, 0);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, WINDOW_WIDTH, WINDOW_HEIGHT, 0.0, 1.0, -1.0);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GLFW.glfwSwapInterval(1);
        ControlNative.glThrowIfError();
        SpritePool.init();

        try {
            audio = new Audio();
            bgm = new SoundSource(true);
            MusicPool.getInstance().initIfAudioInit();

            controlState = state;
            newControlState = controlState;
        } catch (ControlNativeException e) {
            controlState = new ErrorMenu("Audio init error: " + e.getMessage(),
                    state);
            newControlState = controlState;
            Log.printThrowable("Audio init error", e);
        }

        changeInput();
        controlState.init();
    }

    public void changeState(ControlState state) {
        newControlState = state;
    }

    private void resetTimer() {
        accumulator = INTERVAL;
        lastLoopTime = GLFW.glfwGetTime();
    }

    //Обновляет таймер
    private void updateTimer() {
        double time = GLFW.glfwGetTime();
        accumulator += time - lastLoopTime;
        lastLoopTime = time;
    }

    //Меняет музыку, вызывать только в пуле музыки
    public void changeBgm(Sound snd) {
        bgm.changeSound(snd);
        bgm.play();
    }

    //Устанавливает инпут, вызывать при инициализации
    private void changeInput() {
        GLFW.glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
            try {
                controlState.inputCallback(key, scancode, action, mods);
            } catch (ControlException e) {
                throw new InputCallbackException(e);
            }
        });
    }

    public void changeStateNative(ControlState state) throws ControlException {
        state.init();
        controlState = state;
        newControlState = state;
        resetTimer();
    }

    public void updateInput() throws ControlException {
        try {
            GLFW.glfwPollEvents();
        } catch (InputCallbackException e) {
            throw e.getException();
        }
    }

    public ControlState getState() {
        return controlState;
    }

    void loop() throws ControlException {
        resetTimer();

        double fpsT = GLFW.glfwGetTime();
        int fps = 0;
        int ups = 0;
        for (;;) {
            if (controlState != newControlState) {
                if (newControlState == null)
                    break;
                newControlState.init();
                controlState = newControlState;
                resetTimer();
            }

            updateTimer();
            if (accumulator >= INTERVAL) {
                int lagUpdatesCount = 0;
                while (accumulator >= INTERVAL) {
                    lagUpdatesCount++;
                    controlState.input();
                    controlState.update();
                    accumulator -= INTERVAL;
                    updateTimer();
                }

                if (lagUpdatesCount > 1) {
                    System.out.println("Лаг");
                    System.out.println(lagUpdatesCount);
                    System.out.println();
                }

                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                controlState.draw();
                GLFW.glfwSwapBuffers(window);
                fps++;
            }

            {
                updateTimer();
                double sleepTime = INTERVAL - accumulator;
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

       //System.out.println(System.getProperties().getProperty("class.path"));

        if (window != 0) {
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        }

        GLFW.glfwTerminate();

        GLFWErrorCallback ec = GLFW.glfwSetErrorCallback(null);
        if (ec != null)
            ec.free();
    }
}