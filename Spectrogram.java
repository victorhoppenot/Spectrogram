import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.Random;

/*
https://www.youtube.com/watch?v=FeDBcKbO29M
*/

public class Spectrogram extends Application{
    private CanvasChart chart;
    private Color currentColor;

    public Spectrogram()
    {
        this.currentColor = Color.BLACK;
    }

    @Override
    public void start(Stage stage)
    {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent()
    {
        Pane root = new Pane();
        root.setPrefSize(800, 600);
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext pen = canvas.getGraphicsContext2D();

        chart = new CanvasChart(pen, currentColor, new FunctionDataSource());

        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now)
            {
                pen.clearRect(0, 0, 800, 600);
                pen.setFill(currentColor);
                pen.fillRect(150, 300, 500, 1);
                chart.update();
            }
        };
        timer.start();
        root.getChildren().add(canvas);
        return root;
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
            Double previousX = -1.0;
            Double previousY = -1.0;

            for (Point point : points)
            {
                //pen.fillOval(150 + point.x - 0.5, 400 - point.y - 0.5, 1, 1);
                if (previousX > -1)
                {
                    pen.strokeLine(150 + previousX - 0.5, 300 - previousY - 0.5, 150 + point.x - 0.5, 300 - point.y - 0.5);
                }
                previousX = point.x;
                previousY = point.y;
            }
        }
    }

    private static class FunctionDataSource implements DataSource<Point>
    {
        private Double x = 0.0;

        @Override
        public ArrayList<Point> getValues()
        {
            ArrayList<Point> points = new ArrayList<>();
            for (Double i = 0.0; i < 500; i += 1)
            {
                points.add(new Point(i, 100 * (Math.sin(2.5 * (i + x)/25) + Math.sin((i + x)/25) + Math.sin(2 * (i + x)/25))));
            }
            x++;
            return points;
        }
    }

    private interface DataSource<T>
    {
        ArrayList<T> getValues();
    }
}
