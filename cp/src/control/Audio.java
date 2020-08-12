package control;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/*
Audio - класс, для манипуляций со звуком (да ладно, правда что-ли?)
Сам Audio без инициализации не обязан работать
Публичных методов нет

Пакетно-приватные методы:
Конструктор просто создаёт объект Audio без его инициализации
init инициализирует Audio. В случае неудачной инициализации бросает исключение ControlException
isWorking проверяет, инициализирован ли объект
destroy - уничтожает его, может быть вызван даже если объект не инициалиирован
*/

public class Audio {
    private long device = MemoryUtil.NULL;
    private long context = MemoryUtil.NULL;

    Audio() {}

    void init() throws ControlException {
        device = ALC10.alcOpenDevice((ByteBuffer) null);
        if (device == MemoryUtil.NULL) {
            throw new ControlException("Audio init: Failed to open the default OpenAL device");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = ALC10.alcCreateContext(device, (IntBuffer) null);
        if (context == MemoryUtil.NULL) {
            ALC10.alcCloseDevice(device);
            throw new ControlException("Audio init: Failed to create OpenAL context");
        }
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    boolean isWorking() {
        return context != MemoryUtil.NULL;
    }

    void destroy() {
        if (context != MemoryUtil.NULL) {
            ALC10.alcDestroyContext(context);
            context = MemoryUtil.NULL;
        }

        if (device != MemoryUtil.NULL) {
            ALC10.alcCloseDevice(device);
            device = MemoryUtil.NULL;
        }
    }
}
