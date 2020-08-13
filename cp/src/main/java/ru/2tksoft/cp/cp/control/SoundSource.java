package control;

import org.lwjgl.openal.AL10;

/*
Источник звука, ну тут ничо сложного
 */

public class SoundSource {
    private int sourceId = 0;

    public SoundSource(boolean loop, Audio audio) throws ControlException {
        this.sourceId = AL10.alGenSources();
        if (loop)
            AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_TRUE);
        int alError = AL10.alGetError();
        if (alError != AL10.AL_NO_ERROR)
            throw new ControlException("SoundSource create: OpenAL generating sources error: " + alError);
    }

    public void play() {
        AL10.alSourcePlay(sourceId);
    }

    public void changeSound(Sound sound) {
        AL10.alSourceStop(sourceId);
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, sound.getOpenALBuffer());
    }

    public void stop() {
        AL10.alSourceStop(sourceId);
    }

    public void clear() {
        if (sourceId != 0) {
            stop();
            AL10.alDeleteSources(sourceId);
            sourceId = 0;
        }
    }
}