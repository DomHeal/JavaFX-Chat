package com.client.util;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dominic
 * @since 16-Oct-16
 * Website: www.dominicheal.com
 * Github: www.github.com/DomHeal
 */
public class VoicePlayback extends VoiceUtil {
    public static void playAudio(byte[] audio) {
        try {
            InputStream input = new ByteArrayInputStream(audio);
            final AudioFormat format = getAudioFormat();
            final AudioInputStream ais = new AudioInputStream(input, format, audio.length / format.getFrameSize());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            Runnable runner = new Runnable() {
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    try {
                        int count;
                        while ((count = ais.read(
                                buffer, 0, buffer.length)) != -1) {
                            if (count > 0) {
                                line.write(buffer, 0, count);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("I/O problems: " + e);
                    } finally {
                        line.drain();
                        line.close();
                    }
                }
            };
            Thread playThread = new Thread(runner);
            playThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
        }
    }
}
