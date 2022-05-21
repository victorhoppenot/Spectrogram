import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.Random;
import java.io.File;
import javafx.scene.media.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import java.nio.file.Path;

/*
https://www.youtube.com/watch?v=FeDBcKbO29M
*/

public class Spectrogram extends Application{
    private CanvasChart chart;
    private Color currentColor;
    private Stage primary;
    private GraphicsContext pen;
    static FourierTransform ft;

    public Spectrogram()
    {
        this.currentColor = Color.BLACK;
    }

    @Override
    public void start(Stage primary)
    {
        this.primary = primary;
        primary.setScene(new Scene(createContent()));
        primary.show();
        //Happy-birthday-piano-music
    }

    private void playSound(File file)
    {
        ft = new FourierTransform(FourierTransform.getAisFromFile(file));

        Media sound = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        
        chart = new CanvasChart(pen, currentColor, new FunctionDataSource());

        AnimationTimer timer = new AnimationTimer(){
            long initTime = -1;
            // int shit = 0;
            @Override
            public void handle(long now)
            {
                if(initTime == -1){
                    initTime = now;
                }
                // if(shit == 1){
                //     shit = 0;
                // }else{
                //     shit++;
                //     return;
                // }
                pen.clearRect(0, 0, 800, 600);
                pen.setFill(currentColor);
                pen.fillRect(150, 300, 500, 1);
                ft.read(now - initTime);
                chart.update();
            }
        };
        mediaPlayer.play();
        timer.start();
    }

    private Parent createContent()
    {
        BorderPane bp = new BorderPane();
        bp.setPrefSize(800, 600);
        Canvas canvas = new Canvas(800, 600);
        pen = canvas.getGraphicsContext2D();
        bp.setTop(populateMenuBar());
        bp.setCenter(canvas);
        return bp;
    }

    private static class CanvasChart
    {
        private GraphicsContext pen;
        private Color color;
        private DataSource<Point> dataSource;

        public CanvasChart(GraphicsContext pen, Color color, DataSource<Point> dataSource)
        {
            this.pen = pen;
            this.color = color;
            this.dataSource = dataSource;
        }

        public void update()
        {
            ArrayList<Point> points = dataSource.getValues();
            pen.setStroke(color);
            pen.setLineWidth(1);
            Double previousX = -1.0;
            Double previousY = -1.0;

            for (Point point : points)
            {
                //pen.fillOval(150 + point.x - 0.5, 400 - point.y - 0.5, 1, 1);
                if (previousX > -1)
                {
                    pen.setFill(new Color(Math.min(previousY/200,1), Math.min(previousY/250,1), previousX/500, 1));
                    double[] xPoints = {150+previousX,150+point.x, 150+point.x,150+previousX};
                    double[] yPoints = {300,300,300-point.y,300-previousY};
                    pen.fillPolygon(xPoints, yPoints, 4);

                    pen.setFill(new Color(Math.min(previousY/200,1), Math.min(previousY/250,1), 1 -previousX/500, 1));
                    double[] xNegPoints = {650 - previousX,650 -point.x, 650-point.x,650-previousX};
                    double[] yNegPoints = {300,300,300+point.y,300+previousY};
                    pen.fillPolygon(xNegPoints, yNegPoints, 4);
                    
                
                    

                    //pen.strokeLine(150 + previousX - 0.5, 300 - previousY - 0.5, 150 + point.x - 0.5, 300 - point.y - 0.5);
                }
                previousX = point.x;
                previousY = point.y;
            }
        }
    }

    private static class FunctionDataSource implements DataSource<Point>
    {
        private Double x = 0.0;

        ArrayList<Point> previousPoints = new ArrayList<>();
        @Override
        public ArrayList<Point> getValues()
        {
            double A0 = 27.5;
            ArrayList<Point> points = new ArrayList<>();

            /*
            for (Double i = 0.0; i < 500; i += 1)
            {
                points.add(new Point(i, 100 * (Math.sin(2.5 * (i + x)/25) + Math.sin((i + x)/25) + Math.sin(2 * (i + x)/25))));
            }
            x++;
            return points;
            */
            /*
            int spread = 40;
            int resolution = 1;
            for(double i = 0; i < 500; ++i){

                Complex sum = Complex.ZERO;

                for (double j = 0; j < spread; j += spread/resolution)
                {
                    double f = 2000*Math.pow(10,(i + j/spread)/500);
                    sum = sum.plus(ft.fft(f));
                }

                sum = sum.divides(new Complex(resolution, 0));
                points.add(new Point(i, sum.abs()/10));
                
            }
            return points;
            */

            double A1 = 55.00;
            double G$8 = 6644.88;

            for (double i = 0.0; i < 500; i += 1.0)
            {
                double f = Math.pow(2, i/500.0 * 7) * A1;
                points.add(new Point(i, ft.fft(f).abs()/250 * (Math.exp(i/250) - 0.5)));
            }

            return points;

            /*
            for (double i = 0; i < 500; ++i)
            {
                double f = 1000 * Math.pow(10,(i/500.0));
                points.add(new Point(i, ft.fft(f).abs()));
            }
            
            return points;
            */

            /*
            if(previousPoints.size() == 0){
                previousPoints.addAll(points);
                return points;
            }else{

                ArrayList<Point> avg = new ArrayList<>();
                for(int i = 0; i < points.size(); ++i){
                    Point a = points.get(i);
                    Point other = previousPoints.get(i);
                    avg.add(new Point(a.x, (a.y + other.y)/ 2));

                    previousPoints.clear();
                    previousPoints.addAll(points);
                }
                return avg;
            }*/

        }
    }

    private MenuBar populateMenuBar()
    {
        MenuBar mbar = new MenuBar();
        Menu fileMenu = new Menu("File");
        mbar.getMenus().add(fileMenu);
        MenuItem openItem = new MenuItem("Open...");
        openItem.setOnAction(e ->
        {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV Files", "*.wav"));
            fc.setTitle("Open File");
            File chosen = fc.showOpenDialog(primary);
            try {
                playSound(chosen);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        });
        fileMenu.getItems().addAll(openItem);
        return mbar;
    }

    private interface DataSource<T>
    {
        ArrayList<T> getValues();
    }
}
