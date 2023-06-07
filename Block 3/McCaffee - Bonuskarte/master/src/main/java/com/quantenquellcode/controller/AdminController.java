package com.quantenquellcode.controller;

import java.io.IOException;

import com.quantenquellcode.App;

import javafx.fxml.FXML;

public class AdminController {
    
    @FXML
    private void artikelMenu() throws IOException{
        App.setRoot("artikel");
    }
    @FXML
    private void userMenu() throws IOException{
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
