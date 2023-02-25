package com.calculator.bin;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class HomeController {

    @FXML
    private Button loginBtn;
    @FXML
    private PasswordField pwdField;

    @FXML
    private void verifyPwd() throws IOException{
        App.setRoot("primary");
    }

}