import java.io.File;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.*;

public class Instance {
    File file;
    
    private FourierTransform ft;
    private MediaPlayer mp;
    private Animator timer;

    
    public Instance(File file, GraphicsContext pen){
        this.file = file;
        Media sound = new Media(file.toURI().toString());
        mp = new MediaPlayer(sound);
        ft = new FourierTransform(FourierTransform.getAisFromFile(file));
        timer = Animators.YELLOW_BLUE_3D(pen, ft);
    }

    public void start(){
        mp.play();
        timer.start();
    }

    public void stop(){
        mp.stop();
        timer.stop();
    }

    public void pause(){
        mp.pause();
        timer.stop();
    }
}
