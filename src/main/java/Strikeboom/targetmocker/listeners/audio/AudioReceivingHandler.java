package Strikeboom.targetmocker.listeners.audio;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class AudioReceivingHandler extends ListenerAdapter implements AudioReceiveHandler {
    public static byte[] audio;
    Member user;
    AudioFormat highPitchedFormat = new AudioFormat(OUTPUT_FORMAT.getSampleRate() * 1.5f,OUTPUT_FORMAT.getSampleSizeInBits(),OUTPUT_FORMAT.getChannels() ,true,OUTPUT_FORMAT.isBigEndian());
    public AudioReceivingHandler(Member user) {
        this.user = user;
    }

    @Override
    public boolean canReceiveUser() {
        return true;
    }

    @Override
    public void handleUserAudio(@NotNull UserAudio userAudio){
        audio = null;
        if (userAudio.getUser() == user.getUser()) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(userAudio.getAudioData(1f)),highPitchedFormat,userAudio.getAudioData(1f).length);
                AudioSystem.write(AudioSystem.getAudioInputStream(OUTPUT_FORMAT,ais), AudioFileFormat.Type.AU,baos);
                audio = Arrays.copyOfRange(baos.toByteArray(),24,baos.toByteArray().length);
                if (allElementsTheSame(audio)) {
                    audio = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean allElementsTheSame(byte[] array) {
        if (array.length != 0) {
            int first = array[0];
            for (int element : array) {
                if (element != first) {
                    return false;
                }
            }
        }
        return true;
    }
}

