

import javafx.scene.canvas.GraphicsContext;

public abstract class Style {
    
    protected GraphicsContext pen;
    protected FourierTransform ft;

    public Style(GraphicsContext pen, FourierTransform ft){
        this.pen = pen;
        this.ft = ft;
    }

    public abstract void draw();
    public abstract String getName();
}
