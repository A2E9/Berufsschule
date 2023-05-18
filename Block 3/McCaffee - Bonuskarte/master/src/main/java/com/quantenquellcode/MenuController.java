package com.quantenquellcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.quantenquellcode.Database.DatabaseConnection;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;

public class MenuController implements Initializable {

    // @FXML
    // private Button newKdnBtn;
    // @FXML
    // private Button kdnApplyBtn;
    // @FXML
    // private AnchorPane anchorPane;
    // @FXML
    // private TextField customerIdField;

    @FXML
    private MenuButton menubtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String, List<Product>> productsByCategory = new HashMap<>();

        for (String category : fetchCategories()) {
            productsByCategory.put(category, readProductsByCategory(category));
        }
    }

    private List<String> fetchCategories() {
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");
        String getCategoriesSql = "SELECT DISTINCT category FROM products";

        List<String> categories = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getCategoriesSql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categories;
    }

    private List<Product> readProductsByCategory(String category) {
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");
        String getUserSql = "SELECT po.name, po.category, pi.size, pi.price "
                + " FROM products po "
                + " INNER JOIN prices pi ON pi.product_id = po.id "
                + " WHERE po.category = ?";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            Map<String, Product> productMap = new HashMap<>();

            while (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("size");
                float price = rs.getFloat("price");

                Product product = productMap.get(name);
                if (product == null) {
                    product = new Product(name);
                    productMap.put(name, product);
                }

                switch (size) {
                    case "small":
                        product.setSmallPrice(price);
                        break;
                    case "mid":
                        product.setMidPrice(price);
                        break;
                    case "big":
                        product.setBigPrice(price);
                        break;
                    case "universal":
                        product.setUniversalPrice(price);
                        break;
                }
            }
            List<Product> products = new ArrayList<>(productMap.values());

            return products;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

   

    private void displayButtons(List<Product> caffeeList) {
        double startX = 40.0; // Start position X
        double startY = 100.0; // Start position Y
        double gapX = 212.0; // Gap between each button horizontally
        double gapY = 104.0; // Gap between each button vertically
        int buttonsPerRow = 4; // Number of buttons per row

        for (int i = 0; i < caffeeList.size(); i++) {
            Product caffee = caffeeList.get(i);

            // Calculate the row and column based on the index
            int row = i / buttonsPerRow;
            int col = i % buttonsPerRow;

            Button btn = new Button(caffee.name);
            btn.setPrefHeight(70.0);
            btn.setPrefWidth(140.0);
            btn.setTextAlignment(TextAlignment.CENTER);
            btn.setLayoutX(startX + col * gapX);
            btn.setLayoutY(startY + row * gapY);

            btn.setOnAction(event -> {
                // Handle button click here
            });

            // anchorPane.getChildren().add(btn);
        }
    }






    
    @FXML
    private void logout() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void newCustomer() throws IOException, URISyntaxException {
        App.setRoot("newcustomer");
    }
    // @FXML
    // private void checkCustomer() throws IOException {
    // List<Customer> customers = readCustomersDb();
    // // String inputId = customerIdField.getText().trim();
    // String inputId = "customerIdField.getText().trim();";

    // for (Customer customer : customers) {
    // if (customer.id.equals(inputId)) {
    // Alert alert = new Alert(Alert.AlertType.INFORMATION);
    // alert.setTitle("Gefunden!");
    // alert.setHeaderText(null);
    // alert.setContentText("Kunde: " + customer.firstName + " " +
    // customer.secondName);

    // Optional<ButtonType> result = alert.showAndWait();
    // return;
    // }
    // }
    // ButtonType closeBtn = new ButtonType("Schließen", ButtonData.CANCEL_CLOSE);
    // ButtonType addButtonType = new ButtonType("Kunden erstellen",
    // ButtonData.NEXT_FORWARD);
    // Alert alert = new Alert(Alert.AlertType.ERROR, "", closeBtn, addButtonType);
    // alert.setTitle("Fehler!");
    // alert.setHeaderText(null);
    // alert.setContentText("Kunden nicht gefunden!");

    // Optional<ButtonType> result = alert.showAndWait();

    // if (result.isPresent() && result.get() == addButtonType) {
    // App.setRoot("newcustomer");
    // }

    // }

}

class Customer {
    String id;
    String firstName;
    String secondName;

    public Customer(String id, String firstName, String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " firstName: " + this.firstName + " secondName: " + this.secondName;
    }
}

class Product {

    String name;
    float small;
    float mid;
    float big;
    float universal;

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, float universal) {
        this.name = name;
        this.universal = universal;
    }

    public Product(String name, float small, float mid, float big) {
        this.name = name;
        this.small = small;
        this.mid = mid;
        this.big = big;
    }

    public void setSmallPrice(float small) {
        this.small = small;
    }

    public void setMidPrice(float mid) {
        this.mid = mid;
    }

    public void setBigPrice(float big) {
        this.big = big;
    }

    public void setUniversalPrice(float universal) {
        this.universal = universal;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nsmall: " + this.small + "\nmid: " + this.mid + "\nbig: " + this.big + "\n";
    }
}
