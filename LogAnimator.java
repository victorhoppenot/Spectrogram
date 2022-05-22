import javafx.scene.canvas.GraphicsContext;

public abstract class LogAnimator extends Animator{
    private static final double A1 = 55.00;
    
    public LogAnimator(GraphicsContext pen, FourierTransform ft) {
        super(pen, ft);
    }

    public final void draw(){
        initFrame();
        for (double i = 0.0; i < 500; i += 1.0)
        {
            double f = Math.pow(2, i/500.0 * 7) * A1;
            
            drawLog(i, ft.fft(f).times(1.0/250.0 * (Math.exp(i/250) - 0.5)));
        }
    }

    abstract void initFrame();
    abstract void drawLog(double x, Complex y);
    
}

