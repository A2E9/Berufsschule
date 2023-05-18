package com.quantenquellcode;

import java.io.IOException;

import javafx.fxml.FXML;

public class ArtikelController {
    

    @FXML
    private void returnBack() throws IOException{
        App.setRoot("admin");
    }
}
