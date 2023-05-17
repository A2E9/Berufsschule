package com.quantenquellcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;

public class MenuController implements Initializable {

    @FXML
    private Button newKdnBtn;
    @FXML
    private Button kdnApplyBtn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField customerIdField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customerIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                customerIdField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() > 10) {
                customerIdField.setText(oldValue);
            }
        });

        try {
            displayButtons(readCaffeeDb());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void checkCustomer() throws IOException {
        List<Customer> customers = readCustomersDb();
        String inputId = customerIdField.getText().trim();

        for (Customer customer : customers) {
            if (customer.id.equals(inputId)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Gefunden!");
                alert.setHeaderText(null);
                alert.setContentText("Kunde: " + customer.firstName + " " + customer.secondName);

                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
        }
        ButtonType closeBtn = new ButtonType("Schließen", ButtonData.CANCEL_CLOSE);
        ButtonType addButtonType = new ButtonType("Kunden erstellen", ButtonData.NEXT_FORWARD);
        Alert alert = new Alert(Alert.AlertType.ERROR,"",closeBtn,addButtonType);
        alert.setTitle("Fehler!");
        alert.setHeaderText(null);
        alert.setContentText("Kunden nicht gefunden!");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == addButtonType) {
            App.setRoot("newcustomer");
        }

    }

    @FXML
    private void switchNewCustomer() throws IOException, URISyntaxException {
        App.setRoot("newcustomer");
    }

    private List<Caffee> readCaffeeDb() throws IOException, URISyntaxException {
        URL url = App.class.getResource("data/caffee_liste.db");
        URI uri = url.toURI();
        File file = new File(uri);
        Scanner sc = new Scanner(file);

        List<Caffee> caffeeList = new ArrayList<Caffee>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");

            String name = parts[0].trim();
            float small = (parts.length > 1 && !parts[1].isEmpty()) ? Float.parseFloat(parts[1]) : 0.0f;
            float mid = (parts.length > 2 && !parts[2].isEmpty()) ? Float.parseFloat(parts[2]) : 0.0f;
            float big = (parts.length > 3 && !parts[3].isEmpty()) ? Float.parseFloat(parts[3]) : 0.0f;

            Caffee caffe = new Caffee(name, small, mid, big);

            caffeeList.add(caffe);
        }
        sc.close();

        return caffeeList;
    }

    private List<Customer> readCustomersDb() {
        File file = new File("kunden_liste.db");
        if (!file.exists()) {
            // TODO
        }
        List<Customer> customers = new ArrayList<Customer>();
        try (Scanner sc = new Scanner(file)) {

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(";");

                String id = parts[0].trim();
                String firstName = parts[1].trim();
                String secondName = parts[2].trim();

                Customer customer = new Customer(id, firstName, secondName);

                customers.add(customer);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND");
        }
        return customers;
    }

    private void displayButtons(List<Caffee> caffeeList) {
        double startX = 40.0; // Start position X
        double startY = 100.0; // Start position Y
        double gapX = 212.0; // Gap between each button horizontally
        double gapY = 104.0; // Gap between each button vertically
        int buttonsPerRow = 4; // Number of buttons per row

        for (int i = 0; i < caffeeList.size(); i++) {
            Caffee caffee = caffeeList.get(i);

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

            anchorPane.getChildren().add(btn);
        }
    }

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

class Caffee {
    String name;
    float small;
    float mid;
    float big;

    public Caffee(String name, float small, float mid, float big) {
        this.name = name;
        this.small = small;
        this.mid = mid;
        this.big = big;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nsmall: " + this.small + "\nmid: " + this.mid + "\nbig: " + this.big + "\n";
    }
}