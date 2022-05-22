import java.util.function.BiFunction;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Styles {

    public static final ArrayList<BiFunction<GraphicsContext, FourierTransform, Style>> getStyleConstructors(){
        ArrayList<BiFunction<GraphicsContext, FourierTransform, Style>> styles = new ArrayList<>();
        styles.add(Styles::YELLOW_BLUE_3D);
        styles.add(Styles::BLACK_LINE);
        styles.add(Styles::BOX_SPECTRUM);
        styles.add(Styles::Circle);
        return styles;
    }

    public static BiFunction<GraphicsContext, FourierTransform, Style> GenerateSolidStyleConstructor(Color c){
        BiFunction<GraphicsContext, FourierTransform, Style> out = (pen, ft) -> {
            return new LogStyle(pen, ft) {
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
                        
                        pen.setFill(c);
                        double[] xPoints = {150+previousX,150+x, 150+x,150+previousX};
                        double[] yPoints = {300,300,300-absY,300-previousY};
                        pen.fillPolygon(xPoints, yPoints, 4);
                    }
                previousX = x;
                previousY = absY;
                    
                }
                @Override
                public String getName(){
                    return "Solid Color";
                }
            };
        };
        return out;
    }

    public static Style Circle(GraphicsContext pen, FourierTransform ft){
        return new LogStyle(pen, ft) {
            double previousX;
            double previousY;
            double firstX;
            double firstY;

            @Override
            void initFrame() {
                pen.clearRect(0,0,800,600);
                previousX = -1.0;
                previousY = -1.0;
            }

            @Override
            void drawLog(double x,Complex y) {
                double absY = y.abs() / 2.0;
                double curX = Math.cos(2*Math.PI*x/500.0) * 125 + Math.cos(2*Math.PI*x/500.0) * absY;
                double curY = Math.sin(2*Math.PI*x/500.0) * 125 + Math.sin(2*Math.PI*x/500.0) * absY;
                if (previousX != -1){
                    pen.setFill(Color.BLACK);
                    double[] xPoints = {400, 400+previousX, 400+curX};
                    double[] yPoints = {300, 300+previousY, 300+curY};
                    pen.fillPolygon(xPoints, yPoints, 3);
                    if (x == 499)
                    {
                        xPoints = new double[]{400, 400+curX, 400+firstX};
                        yPoints = new double[]{300, 300+curY, 300+firstY};
                        pen.fillPolygon(xPoints, yPoints, 3);
                    }
                }
                else
                {
                    firstX = curX;
                    firstY = curY;
                }
                previousX = curX;
                previousY = curY;
            }
            @Override
            public String getName(){
                return "Circle";
            }
        };
        

    }

    public static Style BLACK_LINE(GraphicsContext pen, FourierTransform ft){
        return new LogStyle(pen, ft) {
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
            
            
            @Override
            public String getName(){
                return "Simple";
            }
        };

    }
    
    public static Style BOX_SPECTRUM(GraphicsContext pen, FourierTransform ft){
        return new LogStyle(pen, ft) {
            @Override
            void initFrame() {
            }

            @Override
            void drawLog(double x, Complex y) {
                double absY = y.abs();
                Color c = new Color(
                    Math.min(absY/200,1),
                    Math.min(absY/200,1),
                    Math.min(absY/200,1),
                    1
                );
                pen.setStroke(c);
                pen.strokeLine(150 + x, 150, 150 + x, 450);

            }
            
            
            @Override
            public String getName(){
                return "Spectrum";
            }
        };
    }

    public static Style YELLOW_BLUE_3D(GraphicsContext pen, FourierTransform ft){
        return new LogStyle(pen, ft) {
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

            @Override
            public String getName(){
                return "3D";
            }
        };
    }
}
