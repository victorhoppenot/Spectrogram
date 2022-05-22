import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Animator extends AnimationTimer{
    protected GraphicsContext pen;
    protected FourierTransform ft;
    private long initTime = -1;
    private Color backgroundColor = Color.WHITE;
    public Animator(GraphicsContext pen, FourierTransform ft){
        super();
        this.pen = pen;
        this.ft = ft;
    }
    @Override
    public final void handle(long now) {
        if(initTime == -1){
            initTime = now;
        }
        pen.clearRect(0, 0, 800, 600);
        pen.setFill(backgroundColor);
        pen.fillRect(150, 300, 500, 1);
        ft.read(now - initTime);
        draw();
    }


    abstract void draw();
}