package com.quantenquellcode;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class UserController implements Initializable {

    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private ListView<String> userView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // CategoryAxis xAxis = new CategoryAxis();
        // NumberAxis yAxis = new NumberAxis();

        // BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // barChart.setTitle("Nicht schlecht");

        // xAxis.setLabel("Produkte");
        // yAxis.setLabel("Gekauft in Einh.");

        // // Create a series of data
        // XYChart.Series<String, Number> series = new XYChart.Series<>();
        // series.setName("Caffe Diagramm");

        // // Add data to the series
        // series.getData().add(new XYChart.Data<>("Espresso", 40));
        // series.getData().add(new XYChart.Data<>("Cafe", 30));
        // series.getData().add(new XYChart.Data<>("Capuccino", 40));
        // series.getData().add(new XYChart.Data<>("Caffe Latte", 60));
        // series.getData().add(new XYChart.Data<>("Vienna", 10));

        // // Add the series to the chart
        // barChart.getData().add(series);

        // mainAnchor.getChildren().add(barChart);

        userView.getItems().addAll("Admin A","Valentyn B.", "Kubilay C.", "Kollege D.", "Mitarbeiter E.");
    }
}
