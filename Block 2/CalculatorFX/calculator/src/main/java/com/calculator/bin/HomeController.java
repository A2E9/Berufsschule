package com.calculator.bin;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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