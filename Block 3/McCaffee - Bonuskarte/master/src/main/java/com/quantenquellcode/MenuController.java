package com.quantenquellcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import com.quantenquellcode.Database.DatabaseConnection;

import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;

class Customer {
    String id;
    String firstname;
    String lastname;

    public Customer(String id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " firstName: " + this.firstname + " secondName: " + this.lastname;
    }
}

class Product {

    String name;
    float small;
    float mid;
    float big;
    float universal;
    String category;

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, float universal) {
        this.name = name;
        this.universal = universal;
    }

    public Product(String name, float small, float mid, float big, float universal, String category) {
        this.name = name;
        this.small = small;
        this.mid = mid;
        this.big = big;
        this.universal = universal;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Category " + this.category + " name: " + this.name + " small: " + this.small + " mid: " + this.mid
                + " big: " + this.big + " universal " + this.universal + "\n";
    }
}

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
    private AnchorPane coffeeAnchor;
    @FXML
    private AnchorPane gebaeckAnchor;
    @FXML
    private AnchorPane frappesAnchor;
    @FXML
    private AnchorPane icedrinkAnchor;
    @FXML
    private AnchorPane vitalAnchor;
    @FXML
    private AnchorPane kaltgetraenkAnchor;
    @FXML
    private AnchorPane dessertAnchor;

    @FXML
    private AnchorPane upperMenu;

    @FXML
    private TextField customeridField;
    @FXML
    private Button checkCustButton;

    @FXML
    private Label priceLabel;

    private static float totalPrice = 0.0f;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String, List<Product>> productsByCategory = new HashMap<>();
        Map<String, AnchorPane> anchorsByCategory = new HashMap<>();

        anchorsByCategory.put("coffee", coffeeAnchor);
        anchorsByCategory.put("gebaeck", gebaeckAnchor);
        anchorsByCategory.put("frappes", frappesAnchor);
        anchorsByCategory.put("iceDrink", icedrinkAnchor);
        anchorsByCategory.put("vital", vitalAnchor);
        anchorsByCategory.put("kaltgetraenk", kaltgetraenkAnchor);
        anchorsByCategory.put("dessert", dessertAnchor);

        for (String category : fetchCategories()) {
            productsByCategory.put(category, readProductsByCategory(category));

            for (ButtonBase buttonBase : displayButtons(productsByCategory.get(category)).getItems()) {
                anchorsByCategory.get(category).getChildren().add(buttonBase);
            }
        }
    }

    private void updatePrice(float price) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        totalPrice += price;
        priceLabel.setText("Preis: " + df.format(totalPrice) + "€");
    }

    private ListView<ButtonBase> displayButtons(List<Product> products) {
        ListView<ButtonBase> listView = new ListView<>();
        double startX = 30.0;
        double startY = 30.0;
        double gapX = 210.0;
        double gapY = 80.0;
        int buttonsPerRow = 3;

        for (int i = 0; i < products.size(); i++) {
            Product caffee = products.get(i);
            int row = i / buttonsPerRow;
            int col = i % buttonsPerRow;

            ButtonBase btn = new ButtonBase() {
                @Override
                public void fire() {
                    throw new UnsupportedOperationException("Unimplemented method 'fire'");
                }
            };

            // TODO: refactor
            if (caffee.universal == 0.0) {
                MenuButton menuBtn = new MenuButton(caffee.name);

                menuBtn.popupSideProperty().set(Side.RIGHT);
                if (caffee.small != 0.0) {
                    MenuItem smallItem = new MenuItem("Klein");
                    smallItem.setOnAction(event -> {
                        System.out.println("Price: " + caffee.small);
                        updatePrice(caffee.small);
                    });
                    menuBtn.getItems().add(smallItem);
                }
                if (caffee.mid != 0.0) {
                    MenuItem midItem = new MenuItem("Mittel");
                    midItem.setOnAction(event -> {
                        System.out.println("Price: " + caffee.mid);
                        updatePrice(caffee.mid);
                    });
                    menuBtn.getItems().add(midItem);
                }
                if (caffee.big != 0.0) {
                    MenuItem bigItem = new MenuItem("Groß");
                    bigItem.setOnAction(event -> {
                        System.out.println("Price: " + caffee.big);
                        updatePrice(caffee.big);
                    });
                    menuBtn.getItems().add(bigItem);
                }
                btn = menuBtn;
            } else {
                Button button = new Button(caffee.name);
                button.setOnAction((e) -> {
                    System.out.println("Price: " + caffee.universal);
                    updatePrice(caffee.universal);
                });
                btn = button;
            }

            btn.setPrefHeight(40.0);
            btn.setPrefWidth(130.0);
            btn.setContentDisplay(ContentDisplay.CENTER);
            btn.setAlignment(Pos.CENTER);
            btn.setLayoutX(startX + col * gapX);
            btn.setLayoutY(startY + row * gapY);
            btn.getStyleClass().add("defButton");

            listView.getItems().add(btn);
        }
        return listView;
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

    @FXML
    private void logout() throws IOException {
        App.setRoot("login");
    }
    @FXML
    private void newCustomer() throws IOException, URISyntaxException {
        App.setRoot("newcustomer");
    }


    
    ////////////////////////////
    ///// Checking Customer/////
    ////////////////////////////

    @FXML
    private void checkCustomer() throws IOException {
        String inputId = customeridField.getText().trim();
        Customer customer = fetchCustomer(inputId);

        customeridField.setStyle("");
        boolean invalidInput = false;
        if (inputId.length() == 0) {
            customeridField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            invalidInput = true;
        }
        if (invalidInput) {
            return;
        }

        if (customer != null) {
            displayCustomerFoundAlert(customer);
            replaceCustomerIdFieldWithLabel(customer);
        } else {
            displayCustomerNotFoundAlert();
        }
    }

    private Customer fetchCustomer(String customerId) {
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");
        String getUserSql = "SELECT customerId, firstname, lastname FROM customers WHERE customerid = ?";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getString("customerId"),
                        rs.getString("firstname"),
                        rs.getString("lastname"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    private void displayCustomerFoundAlert(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gefunden!");
        alert.setHeaderText(null);
        alert.setContentText("Kunde: " + customer.firstname + " " + customer.lastname);
        alert.showAndWait();
    }

    private void replaceCustomerIdFieldWithLabel(Customer customer) {
        Label label = new Label(customer.firstname + " " + customer.lastname);
        label.setStyle("-fx-font: 24 arial;");
        label.setLayoutX(customeridField.getLayoutX());
        label.setLayoutY(customeridField.getLayoutY());

        upperMenu.getChildren().set(upperMenu.getChildren().indexOf(customeridField), label);

        checkCustButton.setText("Neuer Kunde");
        checkCustButton.setOnAction(event -> {
            checkCustButton.setText("Kundennummer bestätigen");
            checkCustButton.setOnAction(e -> {
                try {
                    checkCustomer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            customeridField.clear();
            upperMenu.getChildren().set(upperMenu.getChildren().indexOf(label), customeridField);
        });
    }

    private void displayCustomerNotFoundAlert() throws IOException {
        ButtonType closeBtn = new ButtonType("Schließen", ButtonData.CANCEL_CLOSE);
        ButtonType addButtonType = new ButtonType("Kunden erstellen", ButtonData.NEXT_FORWARD);

        Alert alert = new Alert(Alert.AlertType.ERROR, "", closeBtn, addButtonType);
        alert.setTitle("Fehler!");
        alert.setHeaderText(null);
        alert.setContentText("Kunden nicht gefunden!");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == addButtonType) {
            App.setRoot("newcustomer");
        }
    }

    // List<Product> productList = yuseinFunc();
    // List<Product> coffeeList = productList.stream().filter(c ->
    // c.getCategory().equals("coffee")).collect(Collectors.toList());
    // private List<Product> yuseinFunc() throws SQLException {
    // DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");
    // String getUserSql = "SELECT DISTINCT "
    // + "name, "
    // + "category, "
    // + "COALESCE((SELECT CASE WHEN price = NULL THEN 0 ELSE price END FROM prices
    // WHERE size = 'small' AND products.id = prices.product_id), 0) || ',' || "
    // + "COALESCE((SELECT CASE WHEN price = NULL THEN 0 ELSE price END FROM prices
    // WHERE size = 'mid' AND products.id = prices.product_id), 0) || ',' || "
    // + "COALESCE((SELECT CASE WHEN price = NULL THEN 0 ELSE price END FROM prices
    // WHERE size = 'big' AND products.id = prices.product_id), 0) || ',' || "
    // + "COALESCE((SELECT CASE WHEN price = NULL THEN 0 ELSE price END FROM prices
    // WHERE size = 'universal' AND products.id = prices.product_id), 0) AS
    // price_size "
    // + "FROM "
    // + "products "
    // + "INNER JOIN prices ON products.id = prices.product_id ";

    // List<Product> prList = new ArrayList<Product>();

    // try (Connection connection = dbConnection.getConnection();
    // PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

    // ResultSet rs = pstmt.executeQuery();

    // String[] result = {};

    // while (rs.next()) {
    // String name = rs.getString("name");
    // String category = rs.getString("category");
    // String price_size = rs.getString("price_size");

    // result = price_size.split(",");

    // float small = Float.parseFloat(result[0]);
    // float mid = Float.parseFloat(result[1]);
    // float big = Float.parseFloat(result[2]);
    // float universal = Float.parseFloat(result[3]);

    // Product pr = new Product(name, small, mid, big, universal, category);
    // prList.add(pr);
    // }

    // return prList;
    // } catch(SQLException e) {

    // }

    // return null;
    // }

}
