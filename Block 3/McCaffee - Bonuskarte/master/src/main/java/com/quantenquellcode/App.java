package com.quantenquellcode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("menu"));
        // Prevent Caching
        String cssPath = getClass().getResource("css/styles.css").toExternalForm();
        scene.getStylesheets().remove(cssPath);
        scene.getStylesheets().add(cssPath);
        // End
        stage.setScene(scene);
        stage.show();
        App.stage = stage;

        DatabaseUtils.createNewTable();
    }

    static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
        scene.getWindow().sizeToScene();
        stage.centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}