import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class Sound {
    private int bufferId;

    public int getBuffer() {
        return bufferId;
    }

    public Sound(String s) {
        bufferId = AL10.alGenBuffers();
        StringBuilder path = new StringBuilder(17);
        path.append("/DATA/SND/");
        path.append(s);
        path.append(".dat");
        STBVorbisInfo info = STBVorbisInfo.malloc();
        try {
            InputStream is = Main.class.getResourceAsStream(path.toString());
            ByteBuffer vorbis = ByteBuffer.allocateDirect(4*1024*1024).order(ByteOrder.nativeOrder());
            vorbis.put(is.readAllBytes());
            is.close();
            vorbis.flip();

            IntBuffer error = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
            long decoder = STBVorbis.stb_vorbis_open_memory(vorbis, error, null);
            if ( decoder == MemoryUtil.NULL )
                            throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));

            STBVorbis.stb_vorbis_get_info(decoder, info);
            int channels = info.channels();

            int lengthSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);

            System.out.println(lengthSamples);
            ShortBuffer pcm = MemoryUtil.memAllocShort(lengthSamples*channels);

            pcm.limit(STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            float lengthSeconds = STBVorbis.stb_vorbis_stream_length_in_seconds(decoder);
            System.out.println(lengthSeconds);
            STBVorbis.stb_vorbis_close(decoder);

            AL10.alBufferData(bufferId, info.channels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16, pcm, info.sample_rate());
            //AL10.alBufferData(bufferId, AL10.AL_FORMAT_MONO16, pcm, info.sample_rate());
            info.free();
        } catch (Exception e) {
            throw new RuntimeException("SOUND CORRUPTED", e);
        }
    }

    public void clean() {
        AL10.alDeleteBuffers(bufferId);
    }
}
