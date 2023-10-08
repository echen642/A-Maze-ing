package visual;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/*
 * An extra class that allows us to play audio files to spice up the program a bit more.
 */
public class Sound {
    // Stores soundclip + volume control
    private Clip clip;
    private FloatControl volumeControl;
    private static final int CHECKS_PER_SECOND = 60;
    private static final int TOTAL_FADE_OUT = 3; // in seconds
    private static final int STARTER = CHECKS_PER_SECOND * TOTAL_FADE_OUT;
    private static final int ENDER = 0;
    private int tracker = STARTER;
    
    /*
     * Finds and sets up clip
     */
    public Sound() {
        try {
            File f = new File("res/sound/banger_of_a_song.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Starts playing the clip from the beginning
     */
    public void start() {
        tracker = STARTER;
        volumeControl.setValue(30f * (float) Math.log10(1.0));
        clip.setFramePosition(0);
        clip.start();
    }

    /*
     * Fades out clip for cool effect. Return true if fading and false if done fading otherwise
     */
    public boolean fade() {
        if (tracker > ENDER) {
            tracker--;
            float volume = 1.0F * tracker / STARTER;
            volumeControl.setValue(30f * (float) Math.log10(volume));
            return true;
        }
        else {
            clip.stop();
            return false;
        }
    }
}
