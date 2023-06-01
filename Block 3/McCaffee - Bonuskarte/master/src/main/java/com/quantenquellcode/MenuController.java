package com.quantenquellcode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;

class Customer {
    String id;
    String customerId;
    String firstname;
    String lastname;

    public Customer(String id,String customerId, String firstname, String lastname) {
        this.id = id;
        this.customerId = customerId;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "ID: " + this.customerId + " firstName: " + this.firstname + " secondName: " + this.lastname;
    }
}

class Product {
    private static Map<String, Product> instances = new HashMap<>();
    Map<String, Integer> counts = new HashMap<>();
    String name;
    float small;
    float mid;
    float big;
    float universal;
    String category;

    // int count = 0;

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

    public static Product getInstance(String name) {
        if (!instances.containsKey(name)) {
            instances.put(name, new Product(name));
        }
        return instances.get(name);
    }

    public int getCount(String size) {
        return counts.getOrDefault(size, 1);
    }

    public void addCount(String size) {
        counts.put(size, getCount(size) + 1);
    }

    public void resetCount(String size) {
        counts.put(size, 0);
    }

    public void resetAllCounts() {
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            entry.setValue(1);
        }
    }

    public void subCount(String size) {
        int currentCount = getCount(size);
        if (currentCount > 0) {
            counts.put(size, currentCount - 1);
        }
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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice(String size) {
        switch (size) {
            case "klein":
                return small;
            case "mittel":
                return mid;
            case "groß":
                return big;
            case "universal":
                return universal;
            default:
                return 0.0f;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product product = (Product) obj;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Category " + this.category + " name: " + this.name + " small: " + this.small + " mid: " + this.mid
                + " big: " + this.big + " universal " + this.universal + "\n";
    }
}

public class MenuController implements Initializable {

    @FXML
    private AnchorPane kaltgetraenkAnchor;
    @FXML
    private AnchorPane icedrinkAnchor;
    @FXML
    private AnchorPane gebaeckAnchor;
    @FXML
    private AnchorPane dessertAnchor;
    @FXML
    private AnchorPane frappesAnchor;
    @FXML
    private AnchorPane coffeeAnchor;
    @FXML
    private AnchorPane vitalAnchor;

    @FXML
    private AnchorPane upperMenu;
    @FXML
    private AnchorPane midMenu;
    @FXML
    private AnchorPane lowerMenu;

    @FXML
    private TextField customerField;
    @FXML
    private Button checkCustButton;
    @FXML
    private Button confirmOrder;
    @FXML
    private Button cancelOrder;

    @FXML
    private Label priceLabel;

    private static float totalPrice = 0.0f;

    private Stage bonuStage;

    private Stage shopListStage;

    private ListView<String> shopListView;

    private Label customerLabel;
    private String customerDB_ID;

    Map<String, Product> availableProducts = new HashMap<>();
    private Map<String, Label> countLabels = new HashMap<>();

    void addProduct(Product product) {
        availableProducts.put(product.name, product);
    }

    Product getProduct(String name) {
        return availableProducts.get(name);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shopListView = new ListView<String>();

        TEST_BONUS();
        // Display menu Items

        canUseAnchors(true);
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

    @FXML
    private void submitCustomer(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            checkCustomer();
        }
    }

    private void TEST_BONUS() {

        // List<String> ls = new ArrayList<>();
        // ls.add("Caffe 1");
        // ls.add("Caffe 2");
        // ls.add("Caffe 3"); // Free
        // ls.add("Caffe 4");
        // ls.add("Caffe 5");
        // ls.add("Caffe 6"); // Free
        // ls.add("Caffe 7");
        // ls.add("Caffe 8"); // Free
        // ls.add("Caffe 9");
        // ls.add("Caffe 10");

        // Add CaffeeBonus +1
        // if cafebonus== 3
        // getBestellung niedrigester Preis (Minus betrag)

    }

    /////////////////////////
    ///// Order Controll ////
    /////////////////////////

    public void confirmOrder() {
        if (totalPrice > 0.0f) {
            // priceLabel.setText("Preis: 0€");
            checkCustButton.setText("Kundennummer bestätigen");
            checkCustButton.setOnAction(e -> {
                try {
                    checkCustomer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            customerField.clear();
            upperMenu.getChildren().set(upperMenu.getChildren().indexOf(customerLabel), customerField);
            canUseAnchors(true);
            bonuStage.close(); // Refactor
            shopListStage.close();
            // totalPrice = 0.0f;
            updateCustomerAmount();
            cancelOrder();
        }

    }

    public void cancelOrder() {
        priceLabel.setText("Preis: 0€");
        shopListView.getItems().clear();
        totalPrice = 0.0f;
        for (Map.Entry<String, Product> entry : availableProducts.entrySet()) {
            entry.getValue().resetAllCounts();
        }
        availableProducts.clear();
    }

    ///////////////////////
    //// Visual Change ////
    ///////////////////////

    private void canUseAnchors(Boolean turn) {
        midMenu.setMouseTransparent(turn);
        lowerMenu.setMouseTransparent(turn);
    }

    private void updatePrice(String label, float price) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        totalPrice += price;
        priceLabel.setText("Preis: " + df.format(totalPrice) + "€");

        if (!shopListView.getItems().contains(label)) {
            shopListView.getItems().add(label);
        }
    }

    private void addToShopList(Product product, String size) {
        Product existingProduct = availableProducts.get(product.name);

        if (existingProduct == null) {
            addProduct(product);
            existingProduct = product;
        } else
            // {
            // existingProduct.resetCount(size);
            // }

            existingProduct.addCount(size);
        String key = product.name + ";" + size;
        Label countLabel = countLabels.get(key);
        if (countLabel != null) {
            countLabel.setText(String.valueOf(existingProduct.getCount(size)));
        }

        String productView = existingProduct.name + ";" + size + ";";
        updatePrice(productView, existingProduct.getPrice(size));
    }

    private void displayShopList() {
        shopListView.setMaxHeight(App.stage.getHeight() - bonuStage.getHeight() - 40);

        shopListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {

                            String[] result = item.split(";");
                            String labelname = result[0] + " " + result[1];
                            String size = result[1];
                            Product product = getProduct(result[0]);
                            String countStr = Integer.toString(getProduct(result[0]).getCount(result[1]));

                            if (product == null) {
                                setGraphic(null);
                                return;
                            }

                            HBox box = new HBox();
                            Label prodLabel = new Label(labelname);
                            Label countLabel = new Label(countStr);
                            countLabel.setMinHeight(25);
                            prodLabel.setMinHeight(25);
                            countLabel.setAlignment(Pos.CENTER);
                            prodLabel.setAlignment(Pos.CENTER);

                            String key = result[0] + ";" + result[1];
                            countLabels.put(key, countLabel);

                            HBox.setHgrow(prodLabel, Priority.ALWAYS);

                            Button plusbButton = new Button("+");
                            Button minusButton = new Button("-");

                            // button2.getStyleClass().add("shopButtons");
                            minusButton.setStyle(
                                    "-fx-background-color:none;");
                            plusbButton.setStyle(
                                    "-fx-background-color:none;");

                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);
                            float crrPrice = (float) (Math.round(getProduct(result[0]).getPrice(size) * 100.0)
                                    / 100.0);

                            plusbButton.setOnAction(event -> {
                                getProduct(result[0]).addCount(result[1]);
                                totalPrice += crrPrice;
                                countLabel.setText(String.valueOf(getProduct(result[0]).getCount(result[1])));
                                priceLabel.setText("Preis: " + df.format(totalPrice) + "€");
                            });

                            minusButton.setOnAction(event -> {
                                if (getProduct(result[0]).getCount(result[1]) <= 1) {
                                    getProduct(result[0]).resetCount(result[1]);
                                    countLabels.remove(key, countLabel);
                                    shopListView.getItems().remove(getIndex());
                                } else {
                                    getProduct(result[0]).subCount(result[1]);
                                }
                                totalPrice -= crrPrice;
                                countLabel.setText(String.valueOf(getProduct(result[0]).getCount(result[1])));
                                priceLabel.setText("Preis: " + df.format(negZero(totalPrice)) + "€");
                            });

                            box.getChildren().addAll(prodLabel, plusbButton, countLabel, minusButton);
                            box.setSpacing(2);
                            setGraphic(box);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        });

        shopListView.getItems().clear();

        // Create a new stage if it doesn't exist
        if (shopListStage == null) {
            shopListStage = new Stage();
            Scene scene = new Scene(shopListView);

            shopListStage.setScene(scene);
            shopListStage.setTitle("Einkaufsliste");

            double mainX = App.stage.getX();
            double mainY = App.stage.getY();

            shopListStage.setX(mainX + App.stage.getWidth());
            shopListStage.setY(mainY + bonuStage.getHeight());
        }

        // Show the stage
        shopListStage.show();
    }

    public float negZero(float value) {
        if (value <= 0.0f)
            return 0.0f;
        return value;
    }

    private void displayBonusCard() {
        GridPane grid = new GridPane();
        ImageView[] coffees = new ImageView[10];

        Image coffeeImage = new Image(getClass().getResource("pics/caffe_cup.png").toString());

        for (int i = 0; i < 10; i++) {

            // pray for chatgpt
            coffees[i] = new ImageView(coffeeImage);
            coffees[i].setFitWidth(75); // Set the width of the ImageView
            coffees[i].setFitHeight(75);
            int row = i / 5; // Calculate the row index: 0 for the first 5 images, 1 for the next 5
            int col = i % 5; // Calculate the column index: 0-4 for the first row, 0-4 for the second row

            grid.add(coffees[i], col, row);

            GridPane.setHalignment(coffees[i], HPos.CENTER);
            GridPane.setValignment(coffees[i], VPos.CENTER);

            // Make the cell grow to fill the available space
            GridPane.setHgrow(coffees[i], Priority.ALWAYS);
            GridPane.setVgrow(coffees[i], Priority.ALWAYS);

        }

        grid.setStyle("-fx-background-color:  #5a2f28");
        Stage stage = new Stage();
        Scene scene = new Scene(grid);

        stage.setScene(scene);
        stage.setTitle("Bonus Card");

        double mainX = App.stage.getX();
        double mainY = App.stage.getY();

        stage.setX(mainX + App.stage.getWidth());
        stage.setY(mainY);

        stage.show();
        bonuStage = stage;

        App.stage.setOnCloseRequest(event -> {
            bonuStage.close();
            shopListStage.close();
        });

        displayShopList();
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

            // TODO: refactor REFACTOR
            if (caffee.universal == 0.0) {
                MenuButton menuBtn = new MenuButton(caffee.name);

                menuBtn.popupSideProperty().set(Side.RIGHT);
                if (caffee.small != 0.0) {
                    MenuItem smallItem = new MenuItem("Klein");
                    smallItem.setOnAction(event -> {
                        // System.out.println("Price: " + caffee.small);
                        // updatePrice(caffee.small); // refactor to addToShopList
                        addToShopList(caffee, "klein");
                    });
                    menuBtn.getItems().add(smallItem);
                }
                if (caffee.mid != 0.0) {
                    MenuItem midItem = new MenuItem("Mittel");
                    midItem.setOnAction(event -> {
                        // System.out.println("Price: " + caffee.mid);
                        // updatePrice(caffee.mid);
                        addToShopList(caffee, "mittel");
                    });
                    menuBtn.getItems().add(midItem);
                }
                if (caffee.big != 0.0) {
                    MenuItem bigItem = new MenuItem("Groß");
                    bigItem.setOnAction(event -> {
                        // System.out.println("Price: " + caffee.big);
                        // updatePrice(caffee.big);
                        addToShopList(caffee, "groß");
                    });
                    menuBtn.getItems().add(bigItem);
                }
                btn = menuBtn;
            } else {
                Button button = new Button(caffee.name + "");
                button.setOnAction((e) -> {
                    // System.out.println("Price: " + caffee.universal);
                    // updatePrice(caffee.universal);
                    addToShopList(caffee, "universal");
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

    ///////////////////////
    //// Database Data ////
    ///////////////////////

    private void updateCustomerAmount() {
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");
    
        String selectAmountSql = "SELECT amount FROM payment WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";
        String updateAmountSql = "UPDATE payment SET amount = ? WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";
    
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectAmountSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateAmountSql);) {
            // Retrieve current amount from the database
            selectStmt.setString(1, customerDB_ID);
            ResultSet resultSet = selectStmt.executeQuery();
    
            BigDecimal currentAmount = BigDecimal.ZERO;
            if (resultSet.next()) {
                currentAmount = resultSet.getBigDecimal("amount");
            }
    
            // Calculate new amount by adding totalPrice to currentAmount
            BigDecimal totalPriceDecimal = BigDecimal.valueOf(totalPrice);
            BigDecimal newAmount = currentAmount.add(totalPriceDecimal);
    
            // Update the database with the new amount
            updateStmt.setBigDecimal(1, newAmount);
            updateStmt.setString(2, customerDB_ID);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
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

    private Customer fetchCustomer(String customerId) {
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");
        String getUserSql = "SELECT id, customerId, firstname, lastname FROM customers WHERE customerid = ?";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getString("id"),
                        rs.getString("customerId"),
                        rs.getString("firstname"),
                        rs.getString("lastname"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
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
                product.setCategory(category);

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

    /////////////////////
    //// Switch View ////
    /////////////////////

    @FXML
    private void logout() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void newCustomer() throws IOException, URISyntaxException {
        App.setRoot("newcustomer");
    }

    ////////////////////////
    //// Customer Check ////
    ////////////////////////

    @FXML
    private void checkCustomer() throws IOException {
        String inputId = customerField.getText().trim();
        Customer customer = fetchCustomer(inputId);

        customerDB_ID = customer.id;

        customerField.setStyle("");
        boolean invalidInput = false;
        if (inputId.length() == 0) {
            customerField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
            invalidInput = true;
        }
        if (invalidInput) {
            return;
        }

        if (customer != null) {
            canUseAnchors(false);
            displayCustomerFoundAlert(customer);
            replaceCustomerIdFieldWithLabel(customer);
            displayBonusCard();
        } else {
            displayCustomerNotFoundAlert();
        }
    }

    private void replaceCustomerIdFieldWithLabel(Customer customer) {
        customerLabel = new Label(customer.firstname + " " + customer.lastname);
        customerLabel.setStyle("-fx-font: 24 arial;");
        customerLabel.setLayoutX(customerField.getLayoutX());
        customerLabel.setLayoutY(customerField.getLayoutY());

        upperMenu.getChildren().set(upperMenu.getChildren().indexOf(customerField), customerLabel);

        checkCustButton.setText("Neuer Kunde");
        checkCustButton.setOnAction(event -> {
            priceLabel.setText("Preis: 0€");
            checkCustButton.setText("Kundennummer bestätigen");
            checkCustButton.setOnAction(e -> {
                try {
                    checkCustomer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            customerField.clear();
            upperMenu.getChildren().set(upperMenu.getChildren().indexOf(customerLabel), customerField);
            canUseAnchors(true);
            bonuStage.close(); // Refactor
            shopListStage.close();
        });
    }

    private void displayCustomerFoundAlert(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gefunden!");
        alert.setHeaderText(null);
        alert.setContentText("Kunde: " + customer.firstname + " " + customer.lastname);
        alert.showAndWait();
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
}
