package com.quantenquellcode;

import java.io.IOException;

import javafx.fxml.FXML;

public class AdminController {
    
    @FXML
    private void logout() throws IOException{
        App.setRoot("login");
    }
}
