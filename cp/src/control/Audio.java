package control;

import menu.MenuException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/*
Audio — класс для инициализации звука
Использовать исключительно для инициализации и уничтожения в классе Control
Также в некоторых других звуковых классах, таких как Sound и SoundSource
Данный класс → не обязателен для игрового процесса, поэтому он бросает MenuException лишь при инициализации
Все остальные методы никогда не сообщают пользователю,
    была ли ошибка при инициализации
Их использование в такой ситуации ни производят никаких действий
Это нужно для того, чтобы можно было отдельно не обрабатывать случай неудачной инициализация
Ну неудачная она, ну и хуй с ним, сильно нам звуковой класс нужен что-ли
Мы будем считать, что удачная
*/

class Audio {
    private long device;
    private long context;

    public void init() throws MenuException {
        device = ALC10.alcOpenDevice((ByteBuffer) null);
        if (device == MemoryUtil.NULL) {
            throw new MenuException("Failed to open the default OpenAL device.");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = ALC10.alcCreateContext(device, (IntBuffer) null);
        if (context == MemoryUtil.NULL) {
            ALC10.alcCloseDevice(device);
            throw new MenuException("Failed to create OpenAL context.");
        }
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    public boolean isWorking() {
        return context != 0;
    }

    public void destroy() {
        if (isWorking()) {
            ALC10.alcDestroyContext(context);
            ALC10.alcCloseDevice(device);
        }
    }
}
