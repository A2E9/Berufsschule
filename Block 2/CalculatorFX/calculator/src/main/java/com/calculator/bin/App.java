package com.calculator.bin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    
    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image("https://icons-for-free.com/iconfiles/png/512/calculator-131964752453326292.png",128, 128, true, true);
        stage.getIcons().add(icon);
        
        scene = new Scene(loadFXML("home"), 351, 265);
        stage.setTitle("CALCULATOR");
        
        // scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}