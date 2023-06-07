package com.quantenquellcode.controller;

import com.quantenquellcode.model.Product;
import com.quantenquellcode.utils.AlertHelper;
import com.quantenquellcode.model.Customer;
import com.quantenquellcode.model.Order;
import com.quantenquellcode.dao.CustomerDAO;
import com.quantenquellcode.dao.ProductDAO;
import com.quantenquellcode.App;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
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

public class MenuController implements Initializable {

    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private Order order;

    private static MenuController instance;

    public MenuController() {
        this.customerDAO = new CustomerDAO();
        this.productDAO = new ProductDAO();
        this.order = new Order(productShopMapByPrice, productShopMap, countLabels, totalPrice);
    }

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
    private static float totalPrice;

    private Stage bonuStage;
    private Stage shopListStage;

    private Label customerLabel;
    private String customerDB_ID;

    private static ListView<String> shopListView;

    private Map<Float, Product> productShopMapByPrice = new HashMap<>();
    private Map<String, Product> productShopMap = new HashMap<>();
    private Map<String, Label> countLabels = new HashMap<>();

    ////////////////////
    //// INITIALIZE ////
    ////////////////////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        shopListView = new ListView<String>();
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

        for (String category : productDAO.fetchCategories()) {
            productsByCategory.put(category, productDAO.fetchProductsByCategory(category));

            for (ButtonBase buttonBase : displayButtons(productsByCategory.get(category)).getItems()) {
                anchorsByCategory.get(category).getChildren().add(buttonBase);
            }
        }
        canUseMidLowAnchors(false);
    }

    public void confirmOrder() {// Refactor clear for logout, newCustomer, cancel and cofirm
        if (instance.order.getTotalPrice() > 0.0f) {
            if (!order.checkCustomerBonus(customerDB_ID))
                return;
            customerDAO.updateCustomerAmount(customerDB_ID, instance.order.getTotalPrice());
            customerDAO.updateCustomerBonus(order.getCrrCoffeCount(), customerDB_ID);
            resetOrder();

            checkCustButton.setText("Kundennummer bestätigen");
            checkCustButton.setOnAction(e -> {
                try {
                    checkCustomer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            clearEverything();
            upperMenu.getChildren().set(upperMenu.getChildren().indexOf(customerLabel), customerField);

        }

    }

    private void clearEverything() {
        if (customerField != null)
            customerField.clear();
        canUseMidLowAnchors(false);
        if (bonuStage != null)
            bonuStage.close();
        if (shopListStage != null)
            shopListStage.close();
        if (productsPane != null)
            productsPane.getSelectionModel().clearAndSelect(0);
        resetOrder();
    }

    public void resetOrder() {
        if (priceLabel != null)
        priceLabel.setText("Preis: 0€");
        if (shopListView != null)
        shopListView.getItems().clear();
        
        for (Map.Entry<String, Product> entry : order.getProductShopMap().entrySet()) {
            entry.getValue().resetAllCounts();
        }
        if (order != null){
            order.setTotalPrice(0.0f);
            order.getProductShopMap().clear();
            order.getProductShopMapByPrice().clear();
        }
    }

    public static void updateTotalPrice(float price) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        instance.order.setTotalPrice(price);

        instance.priceLabel.setText("Preis: " + df.format(instance.order.getTotalPrice()) + "€");
    }

    public static void updatePrice(String label, float price) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        // totalPrice += price;
        instance.order.addTotalPrice(price);
        instance.priceLabel.setText("Preis: " + df.format(instance.order.getTotalPrice()) + "€");

        if (!shopListView.getItems().contains(label)) {
            shopListView.getItems().add(label);
        }
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
                            Product product = order.getShopItem(result[0]);
                            String countStr = Integer.toString(order.getShopItem(result[0]).getCount(result[1]));

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
                            order.getCountLabels().put(key, countLabel);

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
                            float crrPrice = (float) (Math.round(order.getShopItem(result[0]).getPrice(size) * 100.0)
                                    / 100.0);

                            plusbButton.setOnAction(event -> {
                                order.getShopItem(result[0]).incrementCount(result[1]);
                                // totalPrice += crrPrice;
                                instance.order.addTotalPrice(crrPrice);
                                countLabel.setText(String.valueOf(order.getShopItem(result[0]).getCount(result[1])));
                                priceLabel.setText("Preis: " + df.format(instance.order.getTotalPrice()) + "€");
                            });

                            minusButton.setOnAction(event -> {
                                if (order.getShopItem(result[0]).getCount(result[1]) <= 1) {
                                    order.getShopItem(result[0]).resetCountForSize(result[1]);
                                    order.getCountLabels().remove(key, countLabel);
                                    shopListView.getItems().remove(getIndex());
                                } else {
                                    order.getShopItem(result[0]).decrementCount(result[1]);
                                }
                                // totalPrice -= crrPrice;
                                instance.order.subTotalPrice(crrPrice);
                                countLabel.setText(String.valueOf(order.getShopItem(result[0]).getCount(result[1])));
                                priceLabel
                                        .setText("Preis: " + df.format(negZero(instance.order.getTotalPrice())) + "€");
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
        int bonus = customerDAO.fetchCustomerBonus(customerDB_ID);

        GridPane grid = new GridPane();
        ImageView[] coffees = new ImageView[14];

        Image coffeeImage = new Image(getClass().getResource("/com/quantenquellcode/pics/caffe_cup.png").toString());
        Image coffeeImageStamped = new Image(
                getClass().getResource("/com/quantenquellcode/pics/caffe_cup_stamp.png").toString());
        Image coffeeImageFree = new Image(
                getClass().getResource("/com/quantenquellcode/pics/caffe_cup_free.png").toString());
        Image coffeeImageFreeStamped = new Image(
                getClass().getResource("/com/quantenquellcode/pics/caffe_cup_free_stamp.png").toString());

        for (int i = 0; i < 14; i++) {
            Image chosenImage;

            if (i == 4 || i == 9 || i == 13) {
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
                            () -> order.addToShopList(product, productSize.getKey()));
                    menuBtn.getItems().add(menuItem);
                }
            }

            if (menuBtn != null) {
                btn = menuBtn;
                menuBtn.popupSideProperty().set(Side.RIGHT);
            } else if (product.getPrice("universal") != 0.0) {
                Button button = new Button(product.getName() + "");
                button.setOnAction((e) -> {
                    order.addToShopList(product, "universal");
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
        Customer customer = customerDAO.fetchCustomer(inputId);

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
            customerDB_ID = String.valueOf(customer.getID()) ; // used for updateCustomerAmount

            canUseMidLowAnchors(true);
            AlertHelper.alertCustomerFound(customer);
            replaceCustomerIdFieldWithLabel(customer);
            displayShopList();
            displayBonusCard();
        } else {
            AlertHelper.alertCustomerNotFound();
        }
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

    private void canUseMidLowAnchors(Boolean turn) {
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
        customerLabel = new Label(customer.getFirstname() + " " + customer.getLastname());
        customerLabel.setStyle("-fx-font: 24 arial;");
        customerLabel.setLayoutX(customerField.getLayoutX());
        customerLabel.setLayoutY(customerField.getLayoutY());

        upperMenu.getChildren().set(upperMenu.getChildren().indexOf(customerField), customerLabel);
        resetOrder();
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
            canUseMidLowAnchors(true);
            bonuStage.close(); // Refactor
            shopListStage.close();
        });
    }

    /////////////////////
    //// Switch View ////
    /////////////////////

    @FXML
    private void logout() throws IOException {
        clearEverything();
        App.setRoot("login");
    }

    @FXML
    private void newCustomer() throws IOException, URISyntaxException {
        clearEverything();
        App.setRoot("newcustomer");
    }

}
