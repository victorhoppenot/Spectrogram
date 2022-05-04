import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

public class Buffer {
    private byte[] buffer;
    private int size;
    
    public Buffer(int size){
        buffer = new byte[size];
        this.size = size;
    }

    public void readBegining(AudioInputStream stream){
        try {
            stream.read(buffer);
        } catch (IOException e) {
            // blah blah blah do better
            e.printStackTrace();
        }
    }

    public void shiftIn(byte b){
        for(int i = 0; i < size - 1; ++i){
            buffer[i] = buffer[i - 1]; 
        }
        buffer[size - 1] = b;
    }

    public byte get(int i){
        return buffer[i];
    }

}
