package ru.w2tksoft.cp.control;

import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/*
Хранит звук
Звуки загружаются конструктором из файла .ogg
Каждая загрузка неплохо притормаживает игру, звук занимает убер дохуя памяти, поэтому быть с этим классом очень
    осторожным!
Также звук сам не удаляется, удалять с помощью clean
*/

public class Sound {
    private int bufferId = 0;

    int getOpenALBuffer() {
        return bufferId;
    }

    public Sound(InputStream is, Audio audio) throws ControlException {
        bufferId = AL10.alGenBuffers();
        int alError = AL10.alGetError();
        if (alError != AL10.AL_NO_ERROR)
            throw new ControlException("Sound load: OpenAL generating buffers error: " + alError);
        STBVorbisInfo info = STBVorbisInfo.malloc();
        ByteBuffer vorbis;
        try {
            vorbis = ControlIO.inputStreamToDirectByteBuffer(is, 8 * 1024 * 1024);
        } catch (IOException exc) {
            throw new ControlException("Sound load: read bytes error: " + exc.getMessage(), exc);
        }

        IntBuffer error = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        long decoder = STBVorbis.stb_vorbis_open_memory(vorbis, error, null);
        if ( decoder == MemoryUtil.NULL )
            throw new ControlException("Sound load: STBVorbis open memory error: " +
                    error.get(0));

        STBVorbis.stb_vorbis_get_info(decoder, info);
        int channels = info.channels();

        int lengthSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);

        ShortBuffer pcm = MemoryUtil.memAllocShort(lengthSamples*channels);

        pcm.limit(STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
        int error2 = STBVorbis.stb_vorbis_get_error(decoder);
        if (error2 != STBVorbis.VORBIS__no_error)
            throw new ControlException("Sound load: STBVorbis get samples short interleaved error: " +
                    error2);
        STBVorbis.stb_vorbis_close(decoder);

        AL10.alBufferData(bufferId, info.channels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16, pcm, info.sample_rate());
        alError = AL10.alGetError();
        if (alError != AL10.AL_NO_ERROR)
            throw new ControlException("Sound load: OpenAL buffer data error: " + alError);
        info.free();
    }

    public void clear() {
        if (bufferId != 0) {
            AL10.alDeleteBuffers(bufferId);
            bufferId = 0;
        }
    }
}
