package ru.w2tksoft.cp.control;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 * Графическая подсистема
 */
public class Graphics {
    /**
     * Создаёт пустой объект
     */
    Graphics() {}

    /**
     * Закрашивает экран
     * @param r красная компонента
     * @param g зелёная компонента
     * @param b синяя компонента
     * @param a непрозрачная компонента
     */
    public void fill(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Устанавливает вид
     * @param x левый верхний угол вида
     * @param y правые верхний угол вида
     */
    public void setView(int x, int y) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y,0);
    }

    /**!package-private!
     * Проверяет наличие ошибок OpenGL
     * @param errString строка, которая будет приписана слева к сообщению исключения (через : и пробел)
     * @throws ControlException бросится, если ошибка есть
     */
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

    /**!package-private!
     * Инициализирует класс Graphics. Данная функция инициализирует OpenGL, поэтому перед этим должен быть
     * проинициализирован GLFW и установлен текущий контекст
     * @param windowWidth ширина окна
     * @param windowHeight высота окна
     * @throws ControlException если произошла ошибка при инициализации
     */
    void init(int windowWidth, int windowHeight) throws ControlException {
        GL.createCapabilities();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, windowWidth, windowHeight, 0.0, 1.0, -1.0);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        glThrowIfError("Graphics init");
    }

    /**!package-private!
     * Уничтожает объект, делает его пустым, может быть вызвана всегда
     */
    void destroy() {}
}
