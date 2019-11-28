package control;

import control.Sound;
import org.lwjgl.openal.AL10;

/*
Источник звука, ну тут ничо сложного
 */

public class SoundSource {
    private int sourceId;

    public SoundSource(boolean loop) {
        this.sourceId = AL10.alGenSources();
        if (loop)
            AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_TRUE);
    }

    public void play() {
        AL10.alSourcePlay(sourceId);
    }

    public void changeSound(Sound sound) {
        AL10.alSourceStop(sourceId);
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, sound.getBuffer());
    }



    public void stop() {
        AL10.alSourceStop(sourceId);
    }

    public void clean() {
        stop();
        AL10.alDeleteSources(sourceId);
    }
}