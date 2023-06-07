package com.quantenquellcode.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.quantenquellcode.App;
import com.quantenquellcode.dao.CustomerDAO;
import com.quantenquellcode.model.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CustomerController  implements Initializable {

    private final CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    @FXML
    private TextField firstnameField;
    @FXML
    private TextField secondnameField;
    @FXML
    private TextField customeridField;

    @FXML
    private Label moneyLabel;

    @FXML
    private ListView<String> customerView;
    private Map<String, Customer> customersByNameMap;
    private ObservableList<String> customersObserve;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Customer> customerList = customerDAO.fetchCustomers();
        customersByNameMap = new HashMap<>();
        customersObserve = FXCollections.observableArrayList();
        for (Customer customer : customerList) {
            customersObserve.add(customer.getFirstname());
            customersByNameMap.put(customer.getFirstname(), customer);
        }
        displayCustomers();
    }

    @FXML
    private void createCustomer() {
        String firstname = firstnameField.getText();
        String secondname = secondnameField.getText();
        String customerid = customeridField.getText();

        Customer customer = new Customer( customerid, firstname, secondname);
        boolean isInserted = customerDAO.insertCustomer(customer);

        if (isInserted) {
            customersObserve.add(customer.getFirstname());
            customersByNameMap.put(customer.getFirstname(), customer);
            try {
                App.setRoot("customer");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteCustomer() {
        String selectedCustomer = customerView.getSelectionModel().getSelectedItem().toString();
        Customer customer = customersByNameMap.get(selectedCustomer);

        boolean isUpdated = customerDAO.deleteCustomer(String.valueOf(customer.getID()));

        if (isUpdated) {
            customersByNameMap.remove(selectedCustomer);
            try {
                App.setRoot("customer");
            } catch (IOException e) {
            }
        }
    }

    @FXML
    private void updateCustomer() {
        String selectCustomer = customerView.getSelectionModel().getSelectedItem().toString();
        Customer customer = customersByNameMap.get(selectCustomer);
        if (customer != null) {
            String firstname = firstnameField.getText();
            String secondname = secondnameField.getText();
            String customerid = customeridField.getText();

            Customer cst = new Customer(customer.getID(), customerid, firstname, secondname, 0);
            boolean isUpdated = customerDAO.updateCustomer(cst);

            if (isUpdated) {
                customersByNameMap.put(selectCustomer, cst);
                try {
                    App.setRoot("customer");
                } catch (IOException e) {
                }
            }
        }
    }

    public void displayCustomers() {
        customerView.setItems(customersObserve);
        customerView.setOnMouseClicked(event -> {
            String selectedItem = customerView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Customer customer = customersByNameMap.get(selectedItem);
                firstnameField.setText(customer.getFirstname());
                secondnameField.setText(customer.getLastname());
                customeridField.setText(customer.getCustomerId());
                double roundOff = Math.round(customer.getTotalSpent() * 100.0) / 100.0;
                moneyLabel.setText("Gesamt: "+String.valueOf(roundOff)+" €");

            }
        });
    }

    @FXML
    private void resetFields() {
        customerView.getSelectionModel().clearSelection();
        firstnameField.setText("");
        secondnameField.setText("");
        customeridField.setText("");
        moneyLabel.setText("");
    }

    @FXML
    private void returnBack() throws IOException {
        App.setRoot("admin");
    }
}
