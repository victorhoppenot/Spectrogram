import java.io.File;
import javafx.util.Duration;
import java.util.function.BiFunction;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer.Status;

public class Instance {
    File file;
    
    private FourierTransform ft;
    private MediaPlayer mp;
    private Animator timer;
    private Duration pauseTime = new Duration(0);

    
    public Instance(File file, GraphicsContext pen){
        this.file = file;
        Media sound = new Media(file.toURI().toString());
        mp = new MediaPlayer(sound);
        ft = new FourierTransform(FourierTransform.getAisFromFile(file));
        timer = new Animator(pen, ft);
    }

    public void constructStyle(BiFunction<GraphicsContext, FourierTransform, Style> construct){
        timer.constructStyle(construct);

    }


    public void start(){
        mp.play();
        timer.start();
    }

    public void stop(){
        mp.stop();
        timer.stop();
    }

    public void unpause(){

        mp.setStartTime(pauseTime);
        mp.play();
        timer.start();
    }
    public void pause(){
        pauseTime = mp.getCurrentTime();
        mp.stop();
        timer.pause();
    }
    
}
