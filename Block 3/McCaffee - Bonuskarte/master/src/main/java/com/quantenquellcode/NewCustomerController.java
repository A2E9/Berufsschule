package com.quantenquellcode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class NewCustomerController {

    @FXML
    private Button addCustBtn;

    @FXML
    private TextField customerIdField;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField secondnameField;

    public void initialize() {
        customerIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                customerIdField.setText(newValue.replaceAll("[^\\d]", ""));
            }else if (newValue.length() > 10) {
                customerIdField.setText(oldValue);
            }
        });
    
        firstnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                firstnameField.setText(newValue.replaceAll("[^a-zA-Z]", ""));
            }else if (newValue.length() > 10) {
                firstnameField.setText(oldValue);
            }
        });
    
        secondnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                secondnameField.setText(newValue.replaceAll("[^a-zA-Z]", ""));
            }else if (newValue.length() > 10) {
                secondnameField.setText(oldValue);
            }
        });
    }

    @FXML
    private void returnBack() throws IOException {
        App.setRoot("menu");
    }

    @FXML
    private void addCustomer() throws IOException {
        String customerNr = customerIdField.getText();
        String firstname = firstnameField.getText();
        String secondname = secondnameField.getText();
    
        customerIdField.setStyle("");
        firstnameField.setStyle("");
        secondnameField.setStyle("");
    
        // Check if any of the fields are too short
        boolean invalidInput = false;
        if (customerNr == null || customerNr.length() < 4) {
            customerIdField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            invalidInput = true;
        }
        if (firstname == null || firstname.length() < 4) {
            firstnameField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            invalidInput = true;
        }
        if (secondname == null || secondname.length() < 4) {
            secondnameField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            invalidInput = true;
        }
    
        if (invalidInput) {
            return;
        }

        String newCustomerLine = customerNr + ";" + firstname + ";" + secondname + "\n";
    
        File file = new File("kunden_liste.db");
        if(!file.exists()){
            file.createNewFile();
        }
    
        try (FileWriter writer = new FileWriter(file, true)) { 
            writer.write(newCustomerLine);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erfolg!");
        alert.setHeaderText(null);
        alert.setContentText("Kunden erstellt!");
    
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) App.setRoot("menu");
    }
    
}
