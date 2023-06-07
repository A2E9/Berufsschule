package com.quantenquellcode.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.quantenquellcode.App;
import com.quantenquellcode.dao.ProductDAO;
import com.quantenquellcode.model.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class  ArtikelController implements Initializable {

    private final ProductDAO productDAO;

    public ArtikelController() {
        this.productDAO = new ProductDAO();
    }

    @FXML
    private ListView<String> productsView;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private MenuButton categoryMenu;
    @FXML
    private TextField nameField;
    @FXML
    private TextField smallSizeField;
    @FXML
    private TextField midSizeField;
    @FXML
    private TextField bigSizeField;
    @FXML
    private TextField uniSizeField;
    @FXML
    private Button saveButton;
    @FXML
    private Button newProdButton;
    @FXML
    private Button resetButton;

    private List<String> categorieList;
    private Map<String, Product> prodsByNameMap;
    private ObservableList<String> productsObserve;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categorieList = productDAO.fetchCategories();
        List<Product> productsList = productDAO.fetchProducts();

        prodsByNameMap = new HashMap<>();
        productsObserve = FXCollections.observableArrayList();

        for (Product product : productsList) {
            productsObserve.add(product.getName());
            prodsByNameMap.put(product.getName(), product);
        }

        displayProducts();

        addCategoriesMenuButton();

        restrictToNumbers(smallSizeField);
        restrictToNumbers(midSizeField);
        restrictToNumbers(bigSizeField);
        restrictToNumbers(uniSizeField);
    }

   

    private void updateProduct() throws IOException {
        nameField.setStyle("");
        categoryMenu.setStyle("");

        if (prodsByNameMap.containsKey(nameField.getText())) {
            Product product = getProductFromFields();
            if(product == null) return;
            productDAO.updateProduct(getProductFromFields());
            prodsByNameMap.put(product.getName(), product);
            reloadController();
        } else {
            nameField.setStyle("-fx-border-color: red;");
        }

    }

    @FXML
    private void deleteProduct() throws IOException {
        nameField.setStyle("");
        String prodName = nameField.getText();
        if (prodsByNameMap.containsKey(prodName)) {
            productDAO.deleteProductByName(prodName);
            prodsByNameMap.remove(prodName);
            reloadController();
        } else {
            nameField.setStyle("-fx-border-color: red;");
        }
    }

    private void addProduct() throws SQLException, IOException {
        nameField.setStyle("");
        if (!prodsByNameMap.containsKey(nameField.getText())) {
           
            Product product = getProductFromFields();
            if(product == null) return;
            productDAO.createProduct(product);
            prodsByNameMap.put(product.getName(), product);
            reloadController();
        } else {
            nameField.setStyle("-fx-border-color: red;");
        }
    }

    @FXML
    private void resetFields() {
        nameField.setStyle("");
        smallSizeField.setStyle("");
        midSizeField.setStyle("");
        bigSizeField.setStyle("");
        uniSizeField.setStyle("");

        nameField.setText("");
        smallSizeField.setText("");
        midSizeField.setText("");
        bigSizeField.setText("");
        uniSizeField.setText("");

        categoryMenu.setText("Kategorie");
    }

    @FXML
    private void validateFields(ActionEvent e) throws IOException, SQLException {
        boolean hasName = nameField.getText().isEmpty();
        boolean isUniversal = !uniSizeField.getText().isEmpty() && Float.parseFloat(uniSizeField.getText()) > 0.0f;
        boolean isSmall = !smallSizeField.getText().isEmpty() && Float.parseFloat(smallSizeField.getText()) > 0.0f;
        boolean isMid = !midSizeField.getText().isEmpty() && Float.parseFloat(midSizeField.getText()) > 0.0f;
        boolean isBig = !bigSizeField.getText().isEmpty() && Float.parseFloat(bigSizeField.getText()) > 0.0f;

        nameField.setStyle("");
        smallSizeField.setStyle("");
        midSizeField.setStyle("");
        bigSizeField.setStyle("");
        uniSizeField.setStyle("");

        if (!hasName) {
            nameField.setStyle("-fx-border-color: red;");
        }

        if ((isUniversal && (isSmall || isMid || isBig)) || (!isUniversal && !(isSmall || isMid || isBig))) {
            if (isUniversal) {
                uniSizeField.setStyle("-fx-border-color: red;");
            } else {
                smallSizeField.setStyle("-fx-border-color: red;");
                midSizeField.setStyle("-fx-border-color: red;");
                bigSizeField.setStyle("-fx-border-color: red;");
            }
        } else {
            Button sourceButton = (Button) e.getSource();
            String buttonName = sourceButton.getText();
            if(buttonName.equals("Speichern"))
            updateProduct();
            else if (buttonName.equals("Neu"))
            addProduct();
        }
    }

    private void restrictToNumbers(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    public void addCategoriesMenuButton() {

        List<MenuItem> menuItems = new ArrayList<>();

        for (String categoryStr : categorieList) {
            MenuItem item = new MenuItem(categoryStr);
            menuItems.add(item);
            item.setOnAction(event -> {
                categoryMenu.setText(item.getText());
            });
        }

        categoryMenu.getItems().addAll(menuItems);

    }

    public void displayProducts() {
        productsView.setItems(productsObserve);

        productsView.setOnMouseClicked(event -> {
            String selectedItem = productsView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {

                Product prod = prodsByNameMap.get(selectedItem);

                nameField.setText(prod.getName());
                smallSizeField.setText(String.valueOf(prod.getPrice("klein")));
                midSizeField.setText(String.valueOf(prod.getPrice("mittel")));
                bigSizeField.setText(String.valueOf(prod.getPrice("groß")));
                uniSizeField.setText(String.valueOf(prod.getPrice("universal")));
                categoryMenu.setText(prod.getCategory());

            }
        });
    }


    private Product getProductFromFields(){
        String prodName = nameField.getText();
        String category = categoryMenu.getText();
        if (category.equals("Kategorie")) {
            categoryMenu.setStyle("-fx-border-color: red;");
            return null;
        }
        float small = 0.0f;
        if (smallSizeField.getText() != null && !smallSizeField.getText().isEmpty())
            small = Float.parseFloat(smallSizeField.getText());

        float mid = 0.0f;
        if (midSizeField.getText() != null && !midSizeField.getText().isEmpty())
            mid = Float.parseFloat(midSizeField.getText());

        float big = 0.0f;
        if (bigSizeField.getText() != null && !bigSizeField.getText().isEmpty())
            big = Float.parseFloat(bigSizeField.getText());

        float uni = 0.0f;
        if (uniSizeField.getText() != null && !uniSizeField.getText().isEmpty())
            uni = Float.parseFloat(uniSizeField.getText());

        return new Product(prodName, small, mid, big, uni, category);
    }


    @FXML
    private void returnBack() throws IOException {
        App.setRoot("admin");
    }

    public void reloadController() throws IOException {
        productsView.getItems().clear();
        App.setRoot("artikel");
    }

}
