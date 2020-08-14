package ru.w2tksoft.cp.control;

import org.lwjgl.opengl.GL11;

/**
 * Спрайт, рисующий закрашенный прямоугольник
 */
public class ColorRectSprite implements RectSprite {
    private float r, g, b;
    private int w, h;

    /**
     * Создаёт спрайт
     * @param r красная компонента цвета
     * @param g зелёная компонента цвета
     * @param b синяя компонента цвета
     * @param w ширина
     * @param h высота
     * @param graphics графическая подсистема
     */
    public ColorRectSprite(float r, float g, float b, int w, int h, Graphics graphics) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.w = w;
        this.h = h;
    }

    /**
     * Рисует прямоугольник
     * @param x координата x левого верхнего угла
     * @param y координата y правого верхнего угла
     */
    public void draw(int x, int y) {
        GL11.glColor3f(r, g, b);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x+w, y);
        GL11.glVertex2i(x, y+h);
        GL11.glVertex2i(x+w, y);
        GL11.glVertex2i(x+w, y+h);
        GL11.glEnd();

        GL11.glColor3f(1.f, 1.f, 1.f);
    }

    @Override
    public int getH() {
        return h;
    }

    @Override
    public int getW() {
        return w;
    }
}
