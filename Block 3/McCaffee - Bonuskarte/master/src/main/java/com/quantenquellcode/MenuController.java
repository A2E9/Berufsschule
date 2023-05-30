package com.quantenquellcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public int count = 1;

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

    public void setCategory(String category) {
        this.category = category;
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
    private Label priceLabel;

    private float totalPrice = 0.0f;

    private Stage bonuStage;

    private Stage shopListStage;

    private ListView<String> shopListView;

    private List<Product> shopProducts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shopProducts = new ArrayList<>();
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

        List<String> ls = new ArrayList<>();
        ls.add("Caffe 1");
        ls.add("Caffe 2");
        ls.add("Caffe 3"); // Free
        ls.add("Caffe 4");
        ls.add("Caffe 5");
        ls.add("Caffe 6"); // Free
        ls.add("Caffe 7");
        ls.add("Caffe 8"); // Free
        ls.add("Caffe 9");
        ls.add("Caffe 10");

        // Add CaffeeBonus +1
        // if cafebonus== 3
        // getBestellung niedrigester Preis (Minus betrag)

    }

    ///////////////////////
    //// Visual Change ////
    ///////////////////////

    private void canUseAnchors(Boolean turn) {
        midMenu.setMouseTransparent(turn);
        lowerMenu.setMouseTransparent(turn);
    }

    private void updatePrice(){

    }
    private void updatePrice(String label, float price) { // pass full string of data
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        totalPrice += price;
        float pricce = 0.0f;
        for (Product product : shopProducts) {
            pricce += price * product.count;
        }
        priceLabel.setText("Preis: " + df.format(pricce) + "€");


        // shopListView.getItems().add(productView);
    }

    private void addToShopList(Product product, String size) {
        int prodIndex = shopProducts.size() - 1;
        shopProducts.add(product);
        String productView = product.name + ";" + size + ";" + prodIndex;
        switch (size) {
            case "klein":
                updatePrice(productView,product.small);
                break;
            case "mittel":
                updatePrice(productView,product.mid);
                break;
            case "groß":
                updatePrice(productView,product.big);
                break;

            default:
                updatePrice(productView,product.universal);
                break;
        }

        

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
                        System.out.println("Price: " + caffee.small);
                        // updatePrice(caffee.small); // refactor to addToShopList
                        addToShopList(caffee, "klein");
                    });
                    menuBtn.getItems().add(smallItem);
                }
                if (caffee.mid != 0.0) {
                    MenuItem midItem = new MenuItem("Mittel");
                    midItem.setOnAction(event -> {
                        System.out.println("Price: " + caffee.mid);
                        // updatePrice(caffee.mid);
                        addToShopList(caffee, "mittel");
                    });
                    menuBtn.getItems().add(midItem);
                }
                if (caffee.big != 0.0) {
                    MenuItem bigItem = new MenuItem("Groß");
                    bigItem.setOnAction(event -> {
                        System.out.println("Price: " + caffee.big);
                        // updatePrice(caffee.big);
                        addToShopList(caffee, "groß");
                    });
                    menuBtn.getItems().add(bigItem);
                }
                btn = menuBtn;
            } else {
                Button button = new Button(caffee.name + "");
                button.setOnAction((e) -> {
                    System.out.println("Price: " + caffee.universal);
                    // updatePrice(caffee.universal);
                    addToShopList(caffee, "");
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
                            int crrIndex = Integer.parseInt(result[2]);
                            String countStr = Integer.toString(shopProducts.get(crrIndex).count);

                            HBox box = new HBox();
                            Label label = new Label(labelname);
                            Label count = new Label(countStr);
                            count.setMinHeight(25);
                            label.setMinHeight(25);
                            count.setAlignment(Pos.CENTER);
                            label.setAlignment(Pos.CENTER);

                            HBox.setHgrow(label, Priority.ALWAYS);

                            Button plusbButton = new Button("+");
                            Button minusButton = new Button("-");

                            // button2.getStyleClass().add("shopButtons");
                            minusButton.setStyle(
                                    "-fx-background-color:none;");
                            plusbButton.setStyle(
                                    "-fx-background-color:none;");

                            // button1.setStyle("-fx-padding: 2; -fx-background-color:none; "
                            // + "-fx-border-style: solid inside;"
                            // + "-fx-border-width: 1;"
                            // + "-fx-border-insets: 5;"
                            // + "-fx-border-radius: 5;"
                            // + "-fx-border-color: blue;");
                            
                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);


                            plusbButton.setOnAction(event -> {
                                System.out.println("Button 1 clicked for item " + crrIndex);
                                shopProducts.get(crrIndex).count++;
                                count.setText(String.valueOf(shopProducts.get(crrIndex).count));
                            });
                            minusButton.setOnAction(event -> {
                                System.out.println("Count " + shopProducts.get(crrIndex).count);
                                if (shopProducts.get(crrIndex).count <= 1) {
                                    // shopListView.getItems().remove(getIndex());
                                    shopListView.getItems().remove(crrIndex);
                                } else {
                                    shopProducts.get(crrIndex).count--;
                                    count.setText(String.valueOf(shopProducts.get(crrIndex).count));
                                    // priceLabel.setText("Preis: " + df.format(pricce) + "€");
                                }
                            });

                            box.getChildren().addAll(label, plusbButton, count, minusButton);
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

        Stage stage = new Stage();
        Scene scene = new Scene(shopListView);

        stage.setScene(scene);
        stage.setTitle("Einkaufsliste");

        double mainX = App.stage.getX();
        double mainY = App.stage.getY();

        stage.setX(mainX + App.stage.getWidth());
        stage.setY(mainY + bonuStage.getHeight());

        stage.show();
        shopListStage = stage;
    }

    ///////////////////////
    //// Database Data ////
    ///////////////////////

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
        Label label = new Label(customer.firstname + " " + customer.lastname);
        label.setStyle("-fx-font: 24 arial;");
        label.setLayoutX(customerField.getLayoutX());
        label.setLayoutY(customerField.getLayoutY());

        upperMenu.getChildren().set(upperMenu.getChildren().indexOf(customerField), label);

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
            upperMenu.getChildren().set(upperMenu.getChildren().indexOf(label), customerField);
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
