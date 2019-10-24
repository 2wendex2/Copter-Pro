package control;

import config.Log;
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
    private int bufferId;

    public int getBuffer() {
        return bufferId;
    }

    public Sound(String s) throws IOException {
        bufferId = AL10.alGenBuffers();
        int alError = AL10.alGetError();
        if (alError != AL10.AL_NO_ERROR)
            throw new IOException("Sound " + s + " loading error: OpenAL error: " + alError);
        STBVorbisInfo info = STBVorbisInfo.malloc();
        InputStream is = Resourse.getResourseAsInputStream("SND", s);
        if (is == null)
            throw new IOException("Sound " + s + " loading error: unable to read resourse");
        ByteBuffer vorbis = ByteBuffer.allocateDirect(4*1024*1024).order(ByteOrder.nativeOrder());
        vorbis.put(is.readAllBytes());
        is.close();
        vorbis.flip();

        IntBuffer error = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        long decoder = STBVorbis.stb_vorbis_open_memory(vorbis, error, null);
        if ( decoder == MemoryUtil.NULL )
            throw new IOException("Sound " + s + " loading error: STBVorbis error: open memory: " +
                    error.get(0));

        STBVorbis.stb_vorbis_get_info(decoder, info);
        int channels = info.channels();

        int lengthSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);

        ShortBuffer pcm = MemoryUtil.memAllocShort(lengthSamples*channels);

        pcm.limit(STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
        int error2 = STBVorbis.stb_vorbis_get_error(decoder);
        if (error2 != STBVorbis.VORBIS__no_error)
            throw new IOException("Sound " + s + " loading error: STBVorbis error: " +
                    error2);
        STBVorbis.stb_vorbis_close(decoder);

        AL10.alBufferData(bufferId, info.channels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16, pcm, info.sample_rate());
        alError = AL10.alGetError();
        if (alError != AL10.AL_NO_ERROR)
            throw new IOException("Sound " + s + " loading error: OpenAL error: " + alError);
        info.free();
        is.close();
    }

    public void clean() {
        AL10.alDeleteBuffers(bufferId);
        int alError = AL10.alGetError();
        if (alError != AL10.AL_NO_ERROR)
            Log.println("Clean sound openAL error: " + alError);
    }
}
