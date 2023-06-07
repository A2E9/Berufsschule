package com.quantenquellcode.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.quantenquellcode.App;
import com.quantenquellcode.model.Customer;
import com.quantenquellcode.model.Product;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class AlertHelper {
    
    public static void alertCustomerFound(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gefunden!");
        alert.setHeaderText(null);
        alert.setContentText("Kunde: " + customer.getFirstname() + " " + customer.getLastname());
        alert.showAndWait();
    }

    public static Boolean alertConfirmOrder(int freeCupCount, float newTotal,float totalPrice, Map<String, Product> productShopMap) {
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

    public static void alertCustomerNotFound() throws IOException {
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
