package control;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class FileSprite implements RectSprite {
    protected int w, h, texture;

    public FileSprite(byte[] bytes, Graphics graphics) throws ControlException {
        ByteBuffer data;
        data = ByteBuffer.allocateDirect(bytes.length);
        data.put(bytes);
        data.flip();

        MemoryStack stack = MemoryStack.stackPush();
        IntBuffer w = stack.mallocInt(1);
        IntBuffer h = stack.mallocInt(1);
        IntBuffer channels = stack.mallocInt(1);

        ByteBuffer image = STBImage.stbi_load_from_memory(data, w, h, channels, 4);
        if (image == null)
            throw new ControlException("FileSprite load error: STBImage load from memory error: " +
                    STBImage.stbi_failure_reason());

        int internalFormat, format;
        internalFormat = GL11.GL_RGBA8;
        format = GL11.GL_RGBA;

        this.w = w.get(0);
        this.h = h.get(0);
        this.texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, this.w, this.h, 0,
                format, GL11.GL_UNSIGNED_BYTE, image);
        graphics.glThrowIfError("FileSprite loading error");
    }

    public void draw(int x, int y) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);

        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2i(x, y);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2i(x+w, y);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2i(x, y+h);

        GL11.glTexCoord2i(0, 1);            GL11.glVertex2i(x, y+h);
        GL11.glTexCoord2i(1, 0);            GL11.glVertex2i(x+w, y);
        GL11.glTexCoord2i(1, 1);            GL11.glVertex2i(x+w, y+h);
        GL11.glEnd();
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void clear() {
        if (texture != 0){
            GL11.glDeleteTextures(texture);
            texture = 0;
        }
    }
}
