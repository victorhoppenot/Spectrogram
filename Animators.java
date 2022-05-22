import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Animators {
    public static Animator YELLOW_BLUE_3D(GraphicsContext pen, FourierTransform ft){
        return new LogAnimator(pen, ft) {
            double previousX;
            double previousY;

            @Override
            void initFrame() {
                previousX = -1.0;
                previousY = -1.0;
            }

            @Override
            void drawLog(double x,Complex y) {
                double absY = y.abs();
                if (previousX > -1){
                    
                    pen.setFill(new Color(Math.min(previousY/200,1), Math.min(previousY/250,1), previousX/500, 1));
                    double[] xPoints = {150+previousX,150+x, 150+x,150+previousX};
                    double[] yPoints = {300,300,300-absY,300-previousY};
                    pen.fillPolygon(xPoints, yPoints, 4);

                    pen.setFill(new Color(Math.min(previousY/200,1), Math.min(previousY/250,1), 1 -previousX/500, 1));
                    double[] xNegPoints = {650 - previousX,650 -x, 650-x,650-previousX};
                    double[] yNegPoints = {300,300,300+absY,300+previousY};
                    pen.fillPolygon(xNegPoints, yNegPoints, 4);
                }
            previousX = x;
            previousY = absY;
                
            }  
        };
    }

    public static Animator BLACK_LINE(GraphicsContext pen, FourierTransform ft){
        return new LogAnimator(pen, ft) {
            double previousX;
            double previousY;
            @Override
            void initFrame() {
                previousX = -1;
                previousY = -1;
            }

            @Override
            void drawLog(double x, Complex y) {
                pen.strokeLine(previousX + 150, 300 - previousY, x + 150, 300 - y.abs());

                previousX = x;
                previousY = y.abs();
            }
            
        };
    }
    
}
