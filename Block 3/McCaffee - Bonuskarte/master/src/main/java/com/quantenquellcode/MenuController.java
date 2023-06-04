package com.quantenquellcode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

class Customer {
    // set to private getter setter
    String id;
    String customerId;
    String firstname;
    String lastname;

    public Customer(String id, String customerId, String firstname, String lastname) {
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
    private String name;
    private float small;
    private float mid;
    private float big;
    private float universal;
    private String category;

    private Map<String, Integer> counts = new HashMap<>();

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, float small, float mid, float big, float universal, String category) {
        this.name = name;
        this.small = small;
        this.mid = mid;
        this.big = big;
        this.universal = universal;
        this.category = category;
    }

    public Product(Product otherProduct) {
        this.name = otherProduct.name;
        this.small = otherProduct.small;
        this.mid = otherProduct.mid;
        this.big = otherProduct.big;
        this.universal = otherProduct.universal;
        this.category = otherProduct.category;
        this.counts = new HashMap<>(otherProduct.counts);
    }

    public float getLowestCoffePrice() {
        if (small > 0.1f) {
            return small;
        }
        if (mid > 0.1f) {
            return mid;
        }
        if (big > 0.1f) {
            return big;
        }
        return 0;
    }

    public int getCount(String size) {
        return counts.getOrDefault(size, 0);
    }

    public Map<String, Integer> getCountsMap() {
        return counts;
    }

    public void incrementCount(String size) {
        counts.put(size, getCount(size) + 1);
    }

    public void decrementCount(String size) {
        int currentCount = getCount(size);
        if (currentCount > 0) {
            counts.put(size, currentCount - 1);
        }
    }

    public void resetCountForSize(String size) {
        counts.put(size, 0);
    }

    public void resetAllCounts() {
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            entry.setValue(0);
        }
    }

    public int getTotalCount() {
        int sumCount = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            sumCount += entry.getValue();
        }
        return sumCount;
    }

    public float getTotalPrice() {
        float sum = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            sum += entry.getValue() * getPrice(entry.getKey());
        }

        return sum;
    }

    public int getLowestPriceCount() {
        if (counts.containsKey("klein"))
            return counts.get("klein");
        if (counts.containsKey("mittel"))
            return counts.get("mittel");
        if (counts.containsKey("groß"))
            return counts.get("groß");
        if (counts.containsKey("universal"))
            return counts.get("universal");

        return 0;
    }

    public void setLowestPriceCount(int newCount) {
        if (counts.containsKey("klein")) {
            counts.put("klein", newCount);
        } else if (counts.containsKey("mittel")) {
            counts.put("mittel", newCount);
        } else if (counts.containsKey("groß")) {
            counts.put("groß", newCount);
        } else if (counts.containsKey("universal")) {
            counts.put("universal", newCount);
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

    public String getName() {
        return name;
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
    public String toString() {
        return "Category " + this.category + " name: " + this.name + "\n";
    }

}

public class MenuController implements Initializable {
    @FXML
    private TabPane productsPane; 
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
    private float totalPrice;

    private Stage bonuStage;
    private Stage shopListStage;

    private Label customerLabel;
    private String customerDB_ID;

    private ListView<String> shopListView;

    private Map<Float, Product> productShopMapByPrice; 
    private Map<String, Product> productShopMap;
    private Map<String, Label> countLabels;

    ////////////////////
    //// INITIALIZE ////
    ////////////////////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shopListView = new ListView<String>();
        productShopMapByPrice = new HashMap<>();
        productShopMap = new HashMap<>();
        countLabels = new HashMap<>();
        totalPrice = 0.0f;

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
            productsByCategory.put(category, fetchProductsByCategory(category));

            for (ButtonBase buttonBase : displayButtons(productsByCategory.get(category)).getItems()) {
                anchorsByCategory.get(category).getChildren().add(buttonBase);
            }
        }
        canUseAnchors(false);
    }

    /////////////////////////
    ///// Order Controll ////
    /////////////////////////

    public void confirmOrder() {// Refactor clear for logout, newCustomer, cancel and cofirm
        if (totalPrice > 0.0f) {
            if(!checkCustomerBonus()) return;
            updateCustomerAmount();
            updateCustomerBonus();
            cancelOrder();

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
            canUseAnchors(false);
            bonuStage.close();
            shopListStage.close();
            productsPane.getSelectionModel().clearAndSelect(0);

        }

    }

    public void cancelOrder() {
        priceLabel.setText("Preis: 0€");
        shopListView.getItems().clear();
        totalPrice = 0.0f;
        for (Map.Entry<String, Product> entry : productShopMap.entrySet()) {
            entry.getValue().resetAllCounts();
        }
        productShopMap.clear();
        productShopMapByPrice.clear();
    }

    private Boolean checkCustomerBonus() {
        int crrBonusCount = getCrrCoffeCount();
        int dbBonusCount = fetchCustomerBonus();

        int bonusCount = crrBonusCount + dbBonusCount;

        int db = 0;
        if (dbBonusCount >= 5) {
            db++;
            if (dbBonusCount >= 10) {
                db++;
                if (dbBonusCount >= 14) {
                    db++;
                }
            }
        }
        int crr = 0;
        if (bonusCount >= 5) {
            crr++;
            if (bonusCount >= 10) {
                crr++;
                if (bonusCount >= 14) {
                    crr++;
                }
            }
        }
        int xBonus = Math.abs(crr - db);
        if (bonusCount > 14) {
            bonusCount = bonusCount - 14;

            if (bonusCount >= 5) {
                crr++;
                if (bonusCount >= 10) {
                    crr++;
                    if (bonusCount >= 14) {
                        crr++;
                    }
                }
            }
            xBonus = Math.abs(crr - db);
        }

        int freeCupCount = xBonus;
        float smallestPrice = Float.MAX_VALUE;
        String name = "";
        int lowestPriceCount = 0;
        // copy to save count for updateBonus
        Map<String, Product> productShopMapByPriceCopy = new HashMap<>();
        for (Map.Entry<String, Product> entry : productShopMap.entrySet()) {
            productShopMapByPriceCopy.put(entry.getKey(), new Product(entry.getValue()));
        }

        while (xBonus > 0 && !productShopMapByPrice.isEmpty()) {
            for (Map.Entry<Float, Product> product : productShopMapByPrice.entrySet()) {
                float price = product.getKey();
                if (smallestPrice > price && product.getValue().getCategory().equals("coffee")) {
                    smallestPrice = price;
                    name = product.getValue().getName();
                    lowestPriceCount = productShopMapByPriceCopy.get(name).getLowestPriceCount();
                }
            }

            if (lowestPriceCount <= xBonus) {
                xBonus -= lowestPriceCount;
                productShopMapByPriceCopy.get(name).setLowestPriceCount(0);
            } else {
                productShopMapByPriceCopy.get(name).setLowestPriceCount(lowestPriceCount - xBonus);
                xBonus = 0;
            }

            if (productShopMapByPriceCopy.get(name).getLowestPriceCount() == 0) {
                productShopMapByPrice.remove(smallestPrice);
            }

            smallestPrice = Float.MAX_VALUE;
        }

        // calc new totalPrice

        float newPrice = 0;
        for (Map.Entry<String, Product> prod : productShopMapByPriceCopy.entrySet()) {
            newPrice += prod.getValue().getTotalPrice();
        }
        if(alertConfirmOrder(freeCupCount, newPrice)){
            updateTotalPrice(newPrice);
            return true;
        }
        return false; // cancel
    }

    private void updateTotalPrice(float price) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        totalPrice = price;
        priceLabel.setText("Preis: " + df.format(totalPrice) + "€");
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
        Product existingProduct = productShopMap.get(product.getName());

        if (existingProduct == null) {
            addShopItem(product);
            existingProduct = product;

        }
        existingProduct.incrementCount(size);
        String key = product.getName() + ";" + size;
        Label countLabel = countLabels.get(key);
        if (countLabel != null) {
            countLabel.setText(String.valueOf(existingProduct.getCount(size)));
        }

        String productView = existingProduct.getName() + ";" + size + ";";
        updatePrice(productView, existingProduct.getPrice(size));

        productShopMapByPrice.put(existingProduct.getPrice(size), existingProduct); // TODO: get the price in cart some
                                                                                    // other way cz duplicating
                                                                                    // shop-products-list
    }

    ///////////////////////
    //// Visual Change ////
    ///////////////////////

    private void displayShopList() {
        shopListView.setMaxHeight(App.stage.getHeight() - 230);

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
                            Product product = getShopItem(result[0]);
                            String countStr = Integer.toString(getShopItem(result[0]).getCount(result[1]));

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
                            float crrPrice = (float) (Math.round(getShopItem(result[0]).getPrice(size) * 100.0)
                                    / 100.0);

                            plusbButton.setOnAction(event -> {
                                getShopItem(result[0]).incrementCount(result[1]);
                                totalPrice += crrPrice;
                                countLabel.setText(String.valueOf(getShopItem(result[0]).getCount(result[1])));
                                priceLabel.setText("Preis: " + df.format(totalPrice) + "€");
                            });

                            minusButton.setOnAction(event -> {
                                if (getShopItem(result[0]).getCount(result[1]) <= 1) {
                                    getShopItem(result[0]).resetCountForSize(result[1]);
                                    countLabels.remove(key, countLabel);
                                    shopListView.getItems().remove(getIndex());
                                } else {
                                    getShopItem(result[0]).decrementCount(result[1]);
                                }
                                totalPrice -= crrPrice;
                                countLabel.setText(String.valueOf(getShopItem(result[0]).getCount(result[1])));
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

        if (shopListStage == null) {
            shopListStage = new Stage();
            Scene scene = new Scene(shopListView);

            shopListStage.setScene(scene);
            shopListStage.setTitle("Einkaufsliste");

            double mainX = App.stage.getX();
            double mainY = App.stage.getY();

            shopListStage.setX(mainX + App.stage.getWidth());
            shopListStage.setY(mainY);
        }

        shopListStage.show();
    }

    private void displayBonusCard() {
        int bonus = fetchCustomerBonus();

        GridPane grid = new GridPane();
        ImageView[] coffees = new ImageView[14];

        Image coffeeImage = new Image(getClass().getResource("pics/caffe_cup.png").toString());
        Image coffeeImageStamped = new Image(getClass().getResource("pics/caffe_cup_stamp.png").toString());
        Image coffeeImageFree = new Image(getClass().getResource("pics/caffe_cup_free.png").toString());
        Image coffeeImageFreeStamped = new Image(getClass().getResource("pics/caffe_cup_free_stamp.png").toString());

        for (int i = 0; i < 14; i++) {
            Image chosenImage;

            if (i == 4 || i == 9 || i == 13) {
                // if (i < bonus) {
                // chosenImage = coffeeImageFreeStamped;
                // } else {
                // chosenImage = coffeeImageFree;
                // }
                chosenImage = i < bonus ? coffeeImageFreeStamped : coffeeImageFree;
            } else {
                chosenImage = i < bonus ? coffeeImageStamped : coffeeImage;
            }

            coffees[i] = new ImageView(chosenImage);
            coffees[i].setFitWidth(75);
            coffees[i].setFitHeight(75);
            int row = i / 7;
            int col = i % 7;

            grid.add(coffees[i], col, row);

            GridPane.setHalignment(coffees[i], HPos.CENTER);
            GridPane.setValignment(coffees[i], VPos.CENTER);

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
        stage.setY(mainY + shopListStage.getHeight());

        stage.show();
        bonuStage = stage;

        App.stage.setOnCloseRequest(event -> {
            bonuStage.close();
            shopListStage.close();
        });

    }

    private ListView<ButtonBase> displayButtons(List<Product> products) {
        ListView<ButtonBase> listView = new ListView<>();
        double startX = 30.0;
        double startY = 30.0;
        double gapX = 210.0;
        double gapY = 80.0;
        int buttonsPerRow = 3;

        Map<String, String> sizes = new HashMap<>() {
            {
                put("klein", "Klein");
                put("mittel", "Mittel");
                put("groß", "Groß");
                put("universal", "Universal");
            }
        };

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            int row = i / buttonsPerRow;
            int col = i % buttonsPerRow;

            ButtonBase btn = new ButtonBase() {
                @Override
                public void fire() {
                    throw new UnsupportedOperationException("Unimplemented method 'fire'");
                }
            };

            MenuButton menuBtn = null;

            for (Map.Entry<String, String> productSize : sizes.entrySet()) {
                if (!productSize.getKey().equals("universal") && product.getPrice(productSize.getKey()) != 0.0) {
                    if (menuBtn == null) {
                        menuBtn = new MenuButton(product.getName());
                    }
                    MenuItem menuItem = createMenuItem(productSize.getValue(),
                            () -> addToShopList(product, productSize.getKey()));
                    menuBtn.getItems().add(menuItem);
                }
            }

            if (menuBtn != null) {
                btn = menuBtn;
                menuBtn.popupSideProperty().set(Side.RIGHT);
            } else if (product.getPrice("universal") != 0.0) {
                Button button = new Button(product.getName() + "");
                button.setOnAction((e) -> {
                    addToShopList(product, "universal");
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
            customerDB_ID = customer.id; // used for updateCustomerAmount

            canUseAnchors(true);
            alertCustomerFound(customer);
            replaceCustomerIdFieldWithLabel(customer);
            displayShopList();
            displayBonusCard();
        } else {
            alertCustomerNotFound();
        }
    }

    ///////////////////////
    //// Database Func ////
    ///////////////////////

    private int fetchCustomerBonus() {
        int currentBonus = 0;
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");

        String selectAmountSql = "SELECT count FROM bonus WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectAmountSql)) {

            selectStmt.setString(1, customerDB_ID);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                currentBonus = resultSet.getInt("count");
            }

        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
        return currentBonus;
    }

    private void updateCustomerBonus() {

        int bonusAmount = getCrrCoffeCount();

        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");

        String selectAmountSql = "SELECT count FROM bonus WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";
        String updateAmountSql = "UPDATE bonus SET count = ? WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectAmountSql);
                PreparedStatement updateStmt = connection.prepareStatement(updateAmountSql);) {

            selectStmt.setString(1, customerDB_ID);
            ResultSet resultSet = selectStmt.executeQuery();

            int currentBonus = 0;
            if (resultSet.next()) {
                currentBonus = resultSet.getInt("count");
            }

            int newBonus = currentBonus + bonusAmount;
            newBonus = newBonus >= 14 ? newBonus - 14 : newBonus;

            updateStmt.setInt(1, newBonus);
            updateStmt.setString(2, customerDB_ID);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
    }

    private void updateCustomerAmount() {
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");

        String selectAmountSql = "SELECT amount FROM payment WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";
        String updateAmountSql = "UPDATE payment SET amount = ? WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectAmountSql);
                PreparedStatement updateStmt = connection.prepareStatement(updateAmountSql);) {

            selectStmt.setString(1, customerDB_ID);
            ResultSet resultSet = selectStmt.executeQuery();

            BigDecimal currentAmount = BigDecimal.ZERO;
            if (resultSet.next()) {
                currentAmount = resultSet.getBigDecimal("amount");
            }

            BigDecimal totalPriceDecimal = BigDecimal.valueOf(totalPrice);
            BigDecimal newAmount = currentAmount.add(totalPriceDecimal);

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

    private List<Product> fetchProductsByCategory(String category) {
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
    //// Helper Func ////
    /////////////////////
    @FXML
    private void submitCustomer(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            checkCustomer();
        }
    }

    private void canUseAnchors(Boolean turn) {
        midMenu.setMouseTransparent((!turn));
        lowerMenu.setMouseTransparent((!turn));
    }

    private MenuItem createMenuItem(String sizeName, Runnable action) {
        MenuItem item = new MenuItem(sizeName);
        item.setOnAction(e -> action.run());
        return item;
    }

    private float negZero(float value) {
        if (value <= 0.0f)
            return 0.0f;
        return value;
    }

    private void replaceCustomerIdFieldWithLabel(Customer customer) {
        customerLabel = new Label(customer.firstname + " " + customer.lastname);
        customerLabel.setStyle("-fx-font: 24 arial;");
        customerLabel.setLayoutX(customerField.getLayoutX());
        customerLabel.setLayoutY(customerField.getLayoutY());

        upperMenu.getChildren().set(upperMenu.getChildren().indexOf(customerField), customerLabel);
        cancelOrder();
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

    private int getCrrCoffeCount() {
        int bonusAmount = 0;
        for (Map.Entry<String, Product> product : productShopMap.entrySet()) {
            if (product.getValue().getCategory().equals("coffee")) {
                bonusAmount += product.getValue().getTotalCount();
            }
        }
        return bonusAmount;
    }

    private void addShopItem(Product product) {
        productShopMap.put(product.getName(), product);
    }

    private Product getShopItem(String name) {
        return productShopMap.get(name);
    }

    ////////////////
    //// Alerts ////
    ////////////////

    private void alertCustomerFound(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gefunden!");
        alert.setHeaderText(null);
        alert.setContentText("Kunde: " + customer.firstname + " " + customer.lastname);
        alert.showAndWait();
    }

    private Boolean alertConfirmOrder(int freeCupCount, float newTotal) {
        ButtonType cancelBtn = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
        ButtonType confirmBtn = new ButtonType("Bestätigen", ButtonData.NEXT_FORWARD);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", cancelBtn, confirmBtn);

        alert.setTitle("Bestätigung");
        alert.setHeaderText("Abrechnung");
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Product> entry : productShopMap.entrySet()) {
            Product product = entry.getValue();
            for (Map.Entry<String, Integer> sizeEntry : product.getCountsMap().entrySet()) {
                String size = sizeEntry.getKey();
                int count = sizeEntry.getValue();
                if(count == 0) break;
                float price = product.getPrice(size);

                sb.append(product.getName())
                        .append(" ").append(size)
                        .append(" - ").append(count)
                        .append("x ").append(String.format("%.2f", price))
                        .append("€\n");
            }
        }

        sb.append("----------------------------------------------\n");
        sb.append("Gesamt:  ").append(String.format("%.2f", totalPrice)).append("€\n");
        sb.append("----------------------------------------------\n");
        sb.append(freeCupCount).append(" Tasse(n) kostenlos - Gesamt:  ").append(String.format("%.2f", newTotal))
                .append("€");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        Label label = new Label();
        label.setText(sb.toString());
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);

        vbox.getChildren().add(label);

        alert.getDialogPane().setContent(vbox);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == cancelBtn) {
            return false;
        }
        return true;
    }

    private void alertCustomerNotFound() throws IOException {
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

}
