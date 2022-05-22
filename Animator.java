import java.util.function.BiFunction;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Animator extends AnimationTimer{
    protected GraphicsContext pen;
    protected FourierTransform ft;
    private long initTime = -1;
    private long currentTime = 0;
    private long pausedTime = 0;
    private boolean pause;
    private Color lineColor = Color.BLACK;
    private Style style;
    private boolean skip;

    public Animator(GraphicsContext pen, FourierTransform ft){
        super();
        this.pen = pen;
        this.ft = ft;
        this.skip = false;
    }

    public void constructStyle(BiFunction<GraphicsContext, FourierTransform, Style> construct){
        style = construct.apply(pen, ft);
    }

    @Override
    public void handle(long now) {
        if(pause){
            pausedTime += now - currentTime;
            pause = false;
        }
        if(initTime == -1){
            initTime = now;
        }
        if (skip)
        {
            skip = !skip;
            return;
        }
        pen.clearRect(0, 0, 800, 600);
        pen.setFill(lineColor);
        pen.fillRect(150, 300, 500, 1);
        ft.read(now - initTime - pausedTime);
        style.draw();
        currentTime = now;
    }
    public void pause(){
        stop();
        pause = true;
    }
}
