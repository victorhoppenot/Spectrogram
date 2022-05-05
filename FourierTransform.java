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

import java.net.URL;


public class FourierTransform {
    final static float SAMPLE_RATE = 44100.0F;
    final static double SAMPLE_TIME = 1.0/SAMPLE_RATE;


    final static int SAMPLE_SIZE = 8;

    final static int TRANSFORM_SIZE = 128;

    final static double TRANSFOM_TIME = 0.5 * SAMPLE_TIME * TRANSFORM_SIZE;  

    final static AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE,1, true, false);        

    private AudioInputStream stream;

    int[] buffer = new int[TRANSFORM_SIZE];
    public FourierTransform(AudioInputStream stream){
        this.stream = stream;
    }

    private long lastReadSample = 0;

    public void reset(){
        lastReadSample = 0;
    }

    final double NANO = Math.pow(10,-9);
    public long sampleAtTime(long time){
        double t = NANO * time * SAMPLE_RATE;
        long sample = (long) t;
        return sample;
    }

    public void read(long time){
        try{
            long sample = sampleAtTime(time);
            if(sample < lastReadSample){
                return;
            }
            stream.skip(sample - lastReadSample);

            for(int i = 0; i < TRANSFORM_SIZE; ++i){
                buffer[i] = stream.read();
            }

            lastReadSample += TRANSFORM_SIZE;
        }catch(Exception e){
            //make beter code
            e.printStackTrace();
        }
    }

    public Complex fft(double f){
        Complex sum = Complex.ZERO;
        for(int i = 0; i < TRANSFORM_SIZE; ++i){
            double n = i * SAMPLE_TIME - TRANSFOM_TIME;
            Complex sample = new Complex(buffer[i], 0);
            Complex change = sample.times(
                new Complex(Math.cos(-2*Math.PI*f*n),Math.sin(-2*Math.PI*f*n))
            ).times(SAMPLE_TIME);
            sum = sum.plus(change);
        }   
        return sum;    
    }

    public static AudioInputStream getAisFromFile(String url){
        
        try{
            URL soundURL = ClassLoader.getSystemResource(url);
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            return AudioSystem.getAudioInputStream(FORMAT, ais);
        }catch(Exception e){
            // blah blah blah make better code
            e.printStackTrace();
            return null;
        }
        
    }





    // static TargetDataLine targetDataLine;
    // static Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();


    public static void main(String args[]) throws LineUnavailableException, InterruptedException, IOException, UnsupportedAudioFileException{
        // Mixer mixer_ = AudioSystem.getMixer(mixerInfo[0]);

        // DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, FORMAT);

        // Mixer mixer = AudioSystem.getMixer(mixerInfo[2]);
        // targetDataLine = AudioSystem.getTargetDataLine(FORMAT);//(TargetDataLine) mixer.getLine(dataLineInfo);
        // targetDataLine.open(FORMAT);
        // targetDataLine.start();
        
        // AudioInputStream ai = new AudioInputStream(targetDataLine);
        // FourierTransform f = new FourierTransform(ai);

        // double test = 440;

        // while(true){
        //     f.next();
        //     System.out.println(f.fft(test).phase());
        //     Thread.sleep(100);
        // }
        AudioInputStream ais = getAisFromFile("./SoundFile.wav");
        boolean go = true;
        int count = 0;
        System.out.println(ais.getFormat());
        while(go){
            if(ais.read() == -1){
                go = false;
            }
            //System.out.println(cringe[0]);
            count++;
            //Thread.sleep(10);
        }
        System.out.println(count);
    }
}
