
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.io.File;
import java.util.function.BiConsumer;

import javafx.scene.media.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
/*
https://www.youtube.com/watch?v=FeDBcKbO29M
*/

public class Spectrogram extends Application{
    BiConsumer<GraphicsContext,FourierTransform> animator =  Animators::YELLOW_BLUE_3D; 
    private Stage primary;
    private Canvas canvas;
    private Instance playingInstance;

    @Override
    public void start(Stage primary)
    {
        this.primary = primary;
        primary.setScene(new Scene(createContent()));
        primary.show();
    }

    private void playSound(File file)
    {
        if(file == null){
            return;
        }
        if(playingInstance != null){
            playingInstance.stop();
        }
        playingInstance = new Instance(file, canvas.getGraphicsContext2D());
        playingInstance.start();
    }

    private Parent createContent()
    {
        BorderPane bp = new BorderPane();
        bp.setPrefSize(800, 600);
        canvas = new Canvas(800, 600);
        bp.setTop(populateMenuBar());
        bp.setCenter(canvas);
        return bp;
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
}
