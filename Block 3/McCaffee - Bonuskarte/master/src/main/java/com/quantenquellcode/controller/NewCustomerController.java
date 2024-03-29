package com.quantenquellcode.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import com.quantenquellcode.App;
import com.quantenquellcode.utils.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

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
            } else if (newValue.length() > 10) {
                customerIdField.setText(oldValue);
            }
        });

        firstnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                firstnameField.setText(newValue.replaceAll("[^a-zA-Z]", ""));
            } else if (newValue.length() > 10) {
                firstnameField.setText(oldValue);
            }
        });

        secondnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                secondnameField.setText(newValue.replaceAll("[^a-zA-Z]", ""));
            } else if (newValue.length() > 10) {
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
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");

        String insertCustomerSql = "INSERT INTO customers (firstname, lastName, customerId) VALUES (?,?,?); ";
        String insertBonusSql = "INSERT INTO bonus (customer_id, count) VALUES "
                + "((SELECT id from customers WHERE customerId=?), 0);";
        String insertPaymentSql = "INSERT INTO payment (customer_id, amount) VALUES "
                + "((SELECT id from customers WHERE customerId=?), 0.0);";
    
        try (Connection connection = dbConnection.getConnection();) {
            // Insert the customer record
            PreparedStatement pstmt = connection.prepareStatement(insertCustomerSql);
            pstmt.setString(1, firstname);
            pstmt.setString(2, secondname);
            pstmt.setString(3, customerNr);
            pstmt.executeUpdate();
    
            // Insert the bonus record
            PreparedStatement pstmt2 = connection.prepareStatement(insertBonusSql);
            pstmt2.setString(1, customerNr);
            pstmt2.executeUpdate();
    
            // Insert the payment record with initial amount as 0
            PreparedStatement pstmt3 = connection.prepareStatement(insertPaymentSql);
            pstmt3.setString(1, customerNr);
            pstmt3.executeUpdate();
    
            showAlert(Alert.AlertType.INFORMATION, "Erfolg!", null, "Kunden erstellt!");
        } catch (SQLException e) {
            String errorMessage = e.getErrorCode() == 19 ? "Ein Kunde mit dieser ID existiert bereits!"
                    : "Unbekannter Fehler!";
            showAlert(Alert.AlertType.ERROR, "Fehler!", null, errorMessage);
            System.out.println(e.getSQLState());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText)
            throws IOException {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Optional<ButtonType> rs = alert.showAndWait();
        if (rs.get() == ButtonType.OK && alertType == AlertType.INFORMATION) // REPAIR
            App.setRoot("menu");
    }
}
