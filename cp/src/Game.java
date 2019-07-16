import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Game {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 576;

    private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    private long window;
    private Input input;
    private Level level;
    private int levelId = 1;
    private Audio audio;
    private Sound curMusic;
    private SoundSource curSource;
    static final private int TARGET_UPS = 60;

    public Game() {
        GLFW.glfwSetErrorCallback(errorCallback);
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(800, 600, "Copter Pro", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        input = new Input(window);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, 800.0, 600.0, 0.0, 1.0, -1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        /*player = new Player(64, 64, 150, 100, 0.0f, 0.0f, 1.0f);
        drawQueue = new ArrayList<>();
        drawQueue.add(new RectModelObject(0, 0, 2000, 32, 1.0f, 0.0f, 0.0f));
        drawQueue.add(new RectModelObject(0, 0, 32, 600, 1.0f, 0.0f, 0.0f));
        drawQueue.add(new RectModelObject(0, 568, 2000, 32, 1.0f, 0.0f, 0.0f));
        drawQueue.add(new RectModelObject(1968, 0, 32, 220, 1.0f, 0.0f, 0.0f));
        drawQueue.add(new RectModelObject(1968, 380, 32, 220, 1.0f, 0.0f, 0.0f));*/
        //drawQueue.add(player);
        input = new Input(window);
        level = new Level(levelId);
        audio = new Audio();
        /*lvlW = 2000;
        lvlH = 600;*/
        /*level = new Level(20, 10, 2, 4, "1111\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "1\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\11" +
                "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
                "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0111111");*/
        System.out.println("12345");
        if (levelId < 9)
            curMusic = new Sound("1l");
        else if (levelId == 9)
            curMusic = new Sound("1m");
        else if (levelId > 9 && levelId < 18)
            curMusic = new Sound("1h");
        else if (levelId == 18)
            curMusic = new Sound("1b");
        curSource = new SoundSource(curMusic, true);
        curSource.play();
    }

    public void end() {
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        input.end();
        errorCallback.free();
    }

    private void render(double alpha){
        GL11.glClearColor(1.0f,1.0f,0.0f,1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(-level.updViewX() , -level.updViewY() + 24 , 0);
        GL11.glBegin(GL11.GL_TRIANGLES);
        level.draw();
        GL11.glEnd();
        GLFW.glfwSwapBuffers(window);
    }

    public void loop()
    {
        double lastLoopTime = GLFW.glfwGetTime();
        double delta;
        final double interval = 1. / TARGET_UPS;
        double accumulator = interval;
        double alpha;

        while (!GLFW.glfwWindowShouldClose(window)) {
            {
                double time = GLFW.glfwGetTime();
                delta = time - lastLoopTime;
                lastLoopTime = time;
                accumulator += delta;
            }

            while (accumulator >= interval) {
                if (level.isEnded()) {
                    levelId++;
                    if (levelId == 9) {
                        curMusic = new Sound("1m");
                        curSource.changeSound(curMusic);
                        curSource.play();
                    } else if (levelId == 10) {
                        curMusic = new Sound("1h");
                        curSource.changeSound(curMusic);
                        curSource.play();
                    } else if (levelId == 18) {
                        curMusic = new Sound("1b");
                        curSource.changeSound(curMusic);
                        curSource.play();
                    }
                    level = new Level(levelId);
                }
                input.update();
                level.update(input);
                accumulator -= interval;
            }

            alpha = accumulator / interval;
            render(alpha);
        }
    }
}
