package com.quantenquellcode;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;


    @FXML
    private void submitPw(KeyEvent event) throws IOException{
        if(event.getCode() == KeyCode.ENTER){
            checkCredentials();
        }
    }

    @FXML
    private void checkCredentials() throws IOException {
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");

        String username = usernameField.getText();
        String password = passwordField.getText();

        usernameField.setStyle("");
        passwordField.setStyle("");

        String getUserSql = "SELECT id, isadmin, username, password "
                + "FROM users WHERE username == ? AND password == ?";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

            pstmt.setString(1, usernameField.getText());
            pstmt.setString(2, passwordField.getText());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt("isadmin") != 0) {
                    App.setRoot("admin");
                } else {
                    App.setRoot("menu");
                }
            } else {

                boolean invalidInput = false;
                if (username == null ) {
                    usernameField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
                    invalidInput = true;
                }
                if (password == null ) {
                    passwordField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
                    invalidInput = true;
                }

                if (invalidInput) {
                    return;
                }
                usernameField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
                passwordField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    
}
