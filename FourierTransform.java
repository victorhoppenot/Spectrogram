import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class FourierTransform {
    final static float SAMPLE_RATE = 44100.0F;
    final static double SAMPLE_TIME = 1.0/SAMPLE_RATE;


    final static int SAMPLE_SIZE = 8;

    final static int TRANSFORM_SIZE = 16;

    final static double TRANSFOM_TIME = 0.5 * SAMPLE_TIME * TRANSFORM_SIZE;  

    final static AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE,1, true, false);        

    private AudioInputStream stream;

    Buffer b = new Buffer(TRANSFORM_SIZE);
    public FourierTransform(AudioInputStream stream){
        this.stream = stream;
        b.readBegining(stream);
    }

    
    public Complex fft(double f){
        Complex sum = Complex.ZERO;
        for(int i = 0; i < TRANSFORM_SIZE; ++i){
            double n = i * SAMPLE_TIME - TRANSFOM_TIME;
            Complex sample = new Complex(b.get(i), 0);
            Complex change = sample.times(
                new Complex(Math.cos(-2*Math.PI*f*n),Math.sin(-2*Math.PI*f*n))
            ).times(SAMPLE_TIME);
            sum = sum.plus(change);
        }   
        return sum;    
    }

    public void next(){
        try {
            b.shiftIn( (byte) stream.read());
        } catch (IOException e) {
            // blah blah blah make better code
            e.printStackTrace();
        }
    }


    static TargetDataLine targetDataLine;
    static Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
    public static void main(String args[]) throws LineUnavailableException, InterruptedException, IOException{
        Mixer mixer_ = AudioSystem.getMixer(mixerInfo[0]);

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, FORMAT);

        Mixer mixer = AudioSystem.getMixer(mixerInfo[2]);
        targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
        targetDataLine.open(FORMAT);
        targetDataLine.start();
        
        AudioInputStream ai = new AudioInputStream(targetDataLine);
        FourierTransform f = new FourierTransform(ai);

        double test = 440;

        while(true){
            f.next();
            System.out.println(f.fft(test).re());
            Thread.sleep(10);
        }
    }
}
