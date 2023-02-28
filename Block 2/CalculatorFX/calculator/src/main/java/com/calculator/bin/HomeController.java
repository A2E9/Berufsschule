package com.calculator.bin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.fxml.Initializable;

public class HomeController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField pwdField;

    @FXML
    private ProgressBar progressHome1;

    @FXML
    private ProgressBar progressHome2;
    

    @FXML
    private void submitPw(KeyEvent event) throws IOException{
        if(event.getCode() == KeyCode.ENTER){
            verifyPwd();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progressHome1.getStyleClass().add("progressHome1");
        progressHome2.getStyleClass().add("progressHome2");
    }

    @FXML
    private void verifyPwd() throws IOException {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), event -> {
            double progress = progressHome1.getProgress() + 0.01;
            if (progress > 1.0) {
                progress = 1.0;
            }
            progressHome1.setProgress(progress);
        }));
        timeline.setCycleCount(100);
        timeline.setOnFinished(event -> {

        });

        Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(0.01), eventt -> {

            double progress = progressHome2.getProgress() - 0.01;
            if (progress < 0.01) {
                progress = 0.01;
            }
            progressHome2.setProgress(progress);
        }));
        timeline1.setCycleCount(100);
        timeline1.setOnFinished(eventt -> {
            try {
                if (pwdField.getText().equals("vale"))
                    App.setRoot("primary");
                else
                    App.setRoot("home");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        timeline.play();
        timeline1.play();

    }

}