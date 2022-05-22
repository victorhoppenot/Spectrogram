import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.Track;

import java.io.File;
import java.net.URL;


public class FourierTransform {
    final static float SAMPLE_RATE = 44100.0F;
    final static double SAMPLE_TIME = 1.0/SAMPLE_RATE;

    final static int SAMPLE_SIZE = 8;

    final static int TRANSFORM_SIZE = 1024;

    final static double TRANSFORM_TIME = SAMPLE_TIME * TRANSFORM_SIZE;  

    final static AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE,1, false, true);        

    private AudioInputStream stream;


    int[] buffer = new int[TRANSFORM_SIZE];
    public FourierTransform(AudioInputStream stream){
        this.stream = stream;
    }
    public FourierTransform(){
        this.stream = null;
    }

    private long lastReadSample = 0;
    private double currentTime = 0;

    public void reset(){
        lastReadSample = 0;
        currentTime = 0;
        buffer = new int[TRANSFORM_SIZE];
    }

    final static double NANO = Math.pow(10,-9);
    final static double MILLI = Math.pow(10,-3);

    public long sampleAtTime(long time){
        double t = NANO * time * SAMPLE_RATE;
        long sample = (long) t;
        return sample;
    }
    public void read(long time){
        /*for(int i = 0; i < TRANSFORM_SIZE; ++i){
            buffer[i]= (int)(128*Math.sin(250*2*Math.PI*(time*NANO + i*SAMPLE_TIME)));
            currentTime = NANO*time;
        }*/

        try{
            long sample = sampleAtTime(time);
            
            if(sample < lastReadSample){
                return;
            }
            
            stream.skip(sample - lastReadSample);

            lastReadSample = sample;

            for(int i = 0; i < TRANSFORM_SIZE; ++i){
                buffer[i] = stream.read();
                lastReadSample++;
            }

        }catch(Exception e){
            //make beter code
            //e.printStackTrace();
        }

        currentTime = NANO*time;
    }

    public Complex fft(double f){
        Complex sum = Complex.ZERO;
        for(int i = 0; i < TRANSFORM_SIZE; ++i){
            double n = currentTime + i * SAMPLE_TIME;
            double v = buffer[i];
            v = (v - 128)/128.0;
            v *= Math.sin(i*Math.PI/TRANSFORM_SIZE); //Windowing
            Complex sample = new Complex(v, 0);
            Complex change = sample.times(
                new Complex(Math.cos(-2*Math.PI*f*n),Math.sin(-2*Math.PI*f*n))
            );
            sum = sum.plus(change);
        }   
        //return sum.divides(new Complex(TRANSFORM_SIZE*SAMPLE_TIME,0));    
        return sum.times(256); 
    }


    public static AudioInputStream getAisFromFile(File file){
        
        try{
            URL soundURL = file.toURI().toURL();
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            return AudioSystem.getAudioInputStream(FORMAT, ais);
        }catch(Exception e){
            // blah blah blah make better code
            e.printStackTrace();
            return null;
        }
        
    }


}
