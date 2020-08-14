package ru.w2tksoft.cp.control;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
* Звуковая подсистема
*/

public class Audio {
    private long device = MemoryUtil.NULL;
    private long context = MemoryUtil.NULL;

    /**!package-private!
     * Создаёт пустой объект Audio
     */
    Audio() {}

    /**!package-private!
     * Инициализирует Audio
     * @throws ControlException бросает если не удалось инициализировать. При такой ситуации объект остаётся пустым
     */
    void init() throws ControlException {
        device = ALC10.alcOpenDevice((ByteBuffer) null);
        if (device == MemoryUtil.NULL) {
            throw new ControlException("Audio init: Failed to open the default OpenAL device");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = ALC10.alcCreateContext(device, (IntBuffer) null);
        if (context == MemoryUtil.NULL) {
            ALC10.alcCloseDevice(device);
            device = MemoryUtil.NULL;
            throw new ControlException("Audio init: Failed to create OpenAL context");
        }
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    /**!package-private!
     * Проверяет, инициализирован ли класс
     * @return true, если инициализирован, false, если нет
     */
    boolean isWorking() {
        return context != MemoryUtil.NULL;
    }

    /**!package-private!
     * Уничтожает объект и делает его пустым. После вызова объект может быть повторно инициализирован.
     * Функция работает всегда, даже если объект не инициализрован (в такой ситуации она просто ничего не делает)
     */
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
