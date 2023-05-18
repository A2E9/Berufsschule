package com.quantenquellcode;

import java.io.IOException;

import javafx.fxml.FXML;

public class AdminController {
    
    @FXML
    private void artikelMenu() throws IOException{
        App.setRoot("artikel");
    }
    @FXML
    private void usermMenu() throws IOException{
        App.setRoot("user");
    }
    @FXML
    private void customerMenu() throws IOException{
        App.setRoot("customer");
    }
    @FXML
    private void logout() throws IOException{
        App.setRoot("login");
    }
    
    
}
