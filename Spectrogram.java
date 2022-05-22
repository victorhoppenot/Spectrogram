
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.io.File;
import java.util.function.BiFunction;

import javafx.scene.media.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
/*
https://www.youtube.com/watch?v=FeDBcKbO29M
*/

public class Spectrogram extends Application{
    BiFunction<GraphicsContext, FourierTransform, Style> styleConstructor =  Styles::BLACK_LINE; 
    private Stage primary;
    private Canvas canvas;
    private Instance playingInstance;
    private boolean paused = false;

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
        playingInstance.constructStyle(styleConstructor);
        playingInstance.start();
        paused = false;
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

        MenuItem pause = new MenuItem("Pause/Play");
        pause.setOnAction(e -> {
            if(playingInstance != null){
                if(paused){
                    playingInstance.unpause();
                }else{
                    playingInstance.pause();
                }
                paused = !paused;

            }
        });


        fileMenu.getItems().addAll(openItem, pause);

        Menu styleMenu = new Menu("Style");
        mbar.getMenus().add(styleMenu);
        
        for(BiFunction<GraphicsContext, FourierTransform, Style> constructor : Styles.getStyleConstructors()){
            String name = constructor.apply(null,null).getName();
            MenuItem style = new MenuItem(name);
            style.setOnAction(e -> {
                styleConstructor = constructor;
                if(playingInstance != null){
                    playingInstance.constructStyle(styleConstructor);
                }
            });
            styleMenu.getItems().add(style);
        }
        Menu colorMenu = new Menu("Solid Color");

        ColorPicker colorssPicker = new ColorPicker();
        colorssPicker.setStyle("-fx-background-color: white;");
        MenuItem colorPicker = new MenuItem(null, colorssPicker);

        colorPicker.setOnAction(e ->{
            styleConstructor = Styles.GenerateSolidStyleConstructor(colorssPicker.getValue());
            if(playingInstance != null){
                playingInstance.constructStyle(styleConstructor);
            }
        });
        colorMenu.getItems().add(colorPicker);
        styleMenu.getItems().add(colorMenu);
        
        



        return mbar;
    }
}
